package com.bmos.mes.service.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.enums.inpspect.InspectConfigDataCodeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.utils.CompactSnowflake;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDataVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigPageVO;
import com.bmos.mes.service.inspect.convert.InspectConfigConvert;
import com.bmos.mes.service.inspect.mapper.InspectConfigDataMapper;
import com.bmos.mes.service.inspect.mapper.InspectConfigMapper;
import com.bmos.mes.service.inspect.mapper.InspectConfigMaterialMapper;
import com.bmos.mes.service.inspect.mapper.InspectMapper;
import com.bmos.mes.service.inspect.model.Inspect;
import com.bmos.mes.service.inspect.model.InspectConfig;
import com.bmos.mes.service.inspect.model.InspectConfigData;
import com.bmos.mes.service.inspect.model.InspectConfigMaterial;
import com.bmos.mes.service.inspect.service.InspectConfigService;
import com.bmos.mes.service.inspect.service.dto.*;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InspectConfigServiceImpl implements InspectConfigService {

    @Autowired
    InspectConfigMapper inspectConfigMapper;

    @Autowired
    InspectMapper inspectMapper;

    @Autowired
    InspectConfigDataMapper inspectConfigDataMapper;

    @Autowired
    InspectConfigMaterialMapper inspectConfigMaterialMapper;

    @Autowired
    ProductFormulaConfigureService productFormulaConfigureService;

    @Autowired
    private com.bmos.mes.service.product.service.ProductMaterialService productMaterialService;

    @Autowired
    private com.bmos.mes.service.inspect.lims.LimsGatewaySelector limsGatewaySelector;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectConfigSaveDTO dto) {
        // 请验单配置数据不能为空
        if (CollUtil.isEmpty(dto.getDataList())){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_DATA_EMPTY);
        }
        // 请验单名称存在
        if (Objects.nonNull(inspectConfigMapper.selectByName(dto.getName()))){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_NAME_EXIST, dto.getName());
        }
        // 校验请验单code是否重复
        this.validDataDuplicate(dto.getDataList());
        InspectConfig inspectConfig = InspectConfigConvert.INSTANCE.convert2InspectConfig(dto);
        inspectConfig.setEnable(Boolean.FALSE);
        SysUser user = SysUserHolder.getUser();
        inspectConfig.setUpdateShowName(StrUtil.format("{}-{}", user.getUserName(), user.getLoginName()));
        inspectConfigMapper.insert(inspectConfig);
        List<InspectConfigData> inspectConfigDataList = InspectConfigConvert.INSTANCE.convert2InspectConfigDataList(dto.getDataList(), inspectConfig.getId());
        inspectConfigDataMapper.insertBatch(inspectConfigDataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InspectConfigUpdateDTO dto) {
        // 请验单配置数据不能为空
        if (CollUtil.isEmpty(dto.getDataList())){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_DATA_EMPTY);
        }
        InspectConfig inspectConfig = inspectConfigMapper.selectById(dto.getId());
        if (Objects.isNull(inspectConfig)){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_NOT_EXIST);
        }
        // 请验单名称存在
        InspectConfig nameInspectConfig = inspectConfigMapper.selectByName(dto.getName());
        if (Objects.nonNull(nameInspectConfig) && !Objects.equals(nameInspectConfig.getId(), dto.getId())){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_NAME_EXIST, dto.getName());
        }
        // 校验请验单是否停用
        if (inspectConfig.getEnable()){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_ENABLE_NOT_OPERATE);
        }
        // 校验请验单code是否重复
        this.validDataDuplicate(dto.getDataList());
        inspectConfig.setName(dto.getName());
        inspectConfig.setRemark(dto.getRemark());
        SysUser user = SysUserHolder.getUser();
        inspectConfig.setUpdateShowName(StrUtil.format("{}-{}", user.getUserName(), user.getLoginName()));
        inspectConfigMapper.updateById(inspectConfig);
        inspectConfigDataMapper.deleteByConfigId(inspectConfig.getId());
        List<InspectConfigData> inspectConfigDataList = InspectConfigConvert.INSTANCE.convert2InspectConfigDataList(dto.getDataList(), inspectConfig.getId());
        inspectConfigDataMapper.insertBatch(inspectConfigDataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 校验请验单是否停用
        InspectConfig inspectConfig = inspectConfigMapper.selectById(id);
        // 校验请验单是否停用
        if (inspectConfig.getEnable()){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_ENABLE_NOT_OPERATE);
        }
        inspectConfigMapper.deleteById(id);
        inspectConfigDataMapper.deleteByConfigId(id);
        inspectConfigMaterialMapper.deleteByConfigId(id);
    }

    @Override
    public void enable(Long id) {
        this.operateEnable(id, Boolean.TRUE);
    }

    @Override
    public void disable(Long id) {
        // 判断当前请验单配置是否已经发起请验，是否已经绑定物料
        List<InspectConfigMaterial> inspectConfigMaterials = inspectConfigMaterialMapper.selectByConfigId(id);
        if (CollUtil.isNotEmpty(inspectConfigMaterials)){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_BIND_MATERIAL);
        }
        List<Inspect> inspects = inspectMapper.selectByInspectConfigId(id);
        if (CollUtil.isNotEmpty(inspects)){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_HAS_INSPECT);
        }
        this.operateEnable(id, Boolean.FALSE);
    }

    @Override
    public InspectConfigDetailVO queryDetail(Long id) {
        // 查询请验单详情
        InspectConfig inspectConfig = inspectConfigMapper.selectById(id);
        if (Objects.isNull(inspectConfig)){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_NOT_EXIST);
        }
        List<InspectConfigData> inspectConfigDataList = inspectConfigDataMapper.selectByConfigId(id);
        InspectConfigDetailVO inspectConfigDetailVO = InspectConfigConvert.INSTANCE.convert2InspectConfigDetailVO(inspectConfig, inspectConfigDataList);
        List<InspectConfigDataVO> inspectConfigDataVOList = InspectConfigConvert.INSTANCE.convert2InspectConfigDataVOList(inspectConfigDataList);
        inspectConfigDetailVO.setDataList(inspectConfigDataVOList);
        return inspectConfigDetailVO;
    }

    @Override
    public CommonPage<InspectConfigPageVO> queryList(InspectConfigPageDTO dto) {
        if (Objects.isNull(dto.getOrderSql())){
            dto.setDir("desc");
            dto.setOrderBy("createTime");
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<InspectConfig> inspectConfigList = inspectConfigMapper.selectPageList(dto);
        return CommonPage.convertPage(inspectConfigList, InspectConfigConvert.INSTANCE::convert2ConfigPageList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindMaterial(InspectConfigBindMaterialDTO dto) {
        // 物料绑定
        InspectConfig inspectConfig = inspectConfigMapper.selectById(dto.getId());
        if (Objects.isNull(inspectConfig)){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_NOT_EXIST);
        }
        // 物料只能在一个请验单中，自动解绑物料与所有请验单的绑定关系
        inspectConfigMaterialMapper.deleteByConfigId(inspectConfig.getId());
        if (CollUtil.isEmpty(dto.getMaterialIdList())){
            return ;
        }
        inspectConfigMaterialMapper.deleteByMaterialIds(dto.getMaterialIdList());
        List<InspectConfigMaterial> inspectConfigMaterialList = InspectConfigConvert.INSTANCE.convert2InspectConfigMaterialList(dto);
        inspectConfigMaterialMapper.insertBatch(inspectConfigMaterialList);
    }

    @Override
    public List<Long> queryBindMaterial(Long id) {
        List<InspectConfigMaterial> inspectConfigMaterials = inspectConfigMaterialMapper.selectBindMaterial(id);
        if (CollUtil.isEmpty(inspectConfigMaterials)){
            return new ArrayList<>();
        }
        return inspectConfigMaterials.stream().map(InspectConfigMaterial::getMaterialId).collect(Collectors.toList());
    }

    @Override
    public java.util.List<InspectConfigDetailVO> queryConfigByFormulaMaterialId(Long formulaMaterialId) {
        ProductFormulaMaterial formulaMaterial = productFormulaConfigureService.selectById(formulaMaterialId);
        if (Objects.isNull(formulaMaterial)){
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        // 自研路径：请验单配置来自 LIMS（一个物料可能对应多个请验单）
        if (limsGatewaySelector.isBmos()) {
            // 通过 MES 物料拿到平台物料id（全系统统一标识），再传给 LIMS
            com.bmos.mes.service.product.model.ProductMaterial productMaterial = productMaterialService.selectById(formulaMaterial.getMaterialId());
            if (Objects.isNull(productMaterial) || Objects.isNull(productMaterial.getPlatformMaterialId())) {
                throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
            }
            java.util.List<InspectConfigDetailVO> limsConfigs = limsGatewaySelector.current()
                    .queryConfig(productMaterial.getPlatformMaterialId());
            if (CollUtil.isEmpty(limsConfigs)) {
                throw new BmosException(MesResponseCode.MATERIAL_NOT_BIND_INSPECT_CONFIG, formulaMaterial.getMaterialName());
            }
            return limsConfigs;
        }
        // 三方/本地路径：保留原有本地逻辑（本地仅绑定一个请验单配置，包成 List 返回以统一接口）
        InspectConfigMaterial inspectConfigMaterial = inspectConfigMaterialMapper.selectByMaterialId(formulaMaterial.getMaterialId());
        if (Objects.isNull(inspectConfigMaterial)){
            throw new BmosException(MesResponseCode.MATERIAL_NOT_BIND_INSPECT_CONFIG, formulaMaterial.getMaterialName());
        }
        InspectConfigDetailVO inspectConfigDetailVO = this.queryDetail(inspectConfigMaterial.getConfigId());
        List<InspectConfigDataVO> dataList = inspectConfigDetailVO.getDataList();
        for (InspectConfigDataVO data : dataList) {
            if (StrUtil.equals(data.getCode(), InspectConfigDataCodeEnum.PLEASE_CHECK_NO.getValue())){
                CompactSnowflake generator = new CompactSnowflake(5); // 机器ID=5
                data.setDefaultValue(String.valueOf(generator.nextId()));
            }
        }
        return java.util.Collections.singletonList(inspectConfigDetailVO);
    }

    /**
     * 校验请验单code是否重复
     * @param dataList
     */
    private void validDataDuplicate(List<InspectConfigDataSaveDTO> dataList) {
        // 校验dataList中的code是否重复
        Set<String> codeSet = new HashSet<>();
        for (InspectConfigDataSaveDTO data : dataList) {
            if (codeSet.contains(data.getCode())){
                throw new BmosException(MesResponseCode.INSPECT_CONFIG_DATA_CODE_EXIST);
            }
            codeSet.add(data.getCode());
        }
    }

    private void operateEnable(Long id, Boolean enable){
        InspectConfig inspectConfig = inspectConfigMapper.selectById(id);
        if (Objects.isNull(inspectConfig)){
            throw new BmosException(MesResponseCode.INSPECT_CONFIG_NOT_EXIST);
        }
        inspectConfig.setEnable(enable);
        inspectConfigMapper.updateById(inspectConfig);
    }
}
