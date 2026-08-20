package com.bmos.lims2.server.inspect.document.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.convert.InspectionDocumentConfigConvert;
import com.bmos.lims2.server.inspect.document.dto.*;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfig;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfigField;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfigMaterial;
import com.bmos.lims2.server.inspect.document.mapper.DocumentConfigFieldMapper;
import com.bmos.lims2.server.inspect.document.mapper.DocumentConfigMapper;
import com.bmos.lims2.server.inspect.document.mapper.DocumentConfigMaterialMapper;
import com.bmos.lims2.server.inspect.document.service.DocumentConfigService;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.lims2.server.util.PageUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentConfigServiceImpl implements DocumentConfigService {

    @Resource
    private DocumentConfigMapper documentConfigMapper;

    @Resource
    private DocumentConfigFieldMapper documentConfigFieldMapper;

    @Resource
    private DocumentConfigMaterialMapper documentConfigMaterialMapper;

    @Resource
    private InspectionOrderMapper inspectionOrderMapper;

    @Override
    public CommonPage<DocumentConfigDTO> inspectionConfigPage(DocumentConfigPageReqDTO dto) {
        // 当未传入排序时，默认按创建时间降序
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), PageUtils.getOrderByOrDefaultByCreateTimeDesc(dto));
        List<DocumentConfig> list = documentConfigMapper.page(dto);
        return InspectionDocumentConfigConvert.INSTANCE.convert2Page(CommonPage.convertPage(list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionConfig(DocumentConfigSaveDTO dto) {
        // 名称重复性校验
        if (documentConfigMapper.nameExists(dto.getName(), null)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NAME_EXISTS);
        }
        // 新增请验单
        DocumentConfig inspectionConfig = InspectionDocumentConfigConvert.INSTANCE.convert2DO(dto);
        inspectionConfig.setStatus(Boolean.FALSE);
        documentConfigMapper.insert(inspectionConfig);
        // 请验单数据
        List<DocumentConfigField> dataList = InspectionDocumentConfigConvert.INSTANCE.convert2InspectConfigDataList(dto.getDataList(), inspectionConfig.getId());
        documentConfigFieldMapper.insertBatch(dataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInspectionConfig(DocumentConfigUpdateDTO dto) {
        // 名称重复性校验
        if (documentConfigMapper.nameExists(dto.getName(), dto.getId())) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NAME_EXISTS);
        }
        DocumentConfig documentConfig = documentConfigMapper.selectById(dto.getId());
        if (Objects.isNull(documentConfig)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NOT_EXISTS);
        }
        // 校验请验单是否停用
        if (documentConfig.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_ENABLE_CANT_OPERATE);
        }
        // 配置更新
        documentConfig.setName(dto.getName());
        documentConfig.setRemark(dto.getRemark());
        documentConfigMapper.updateById(documentConfig);
        // 请验单数据更新
        documentConfigFieldMapper.deleteByConfigId(documentConfig.getId());
        List<DocumentConfigField> dataList = InspectionDocumentConfigConvert.INSTANCE.convert2InspectConfigDataList(dto.getDataList(), documentConfig.getId());
        documentConfigFieldMapper.insertBatch(dataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionConfig(Long id) {
        // 校验请验单是否停用
        DocumentConfig documentConfig = documentConfigMapper.selectById(id);
        // 校验请验单是否停用
        if (documentConfig.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_ENABLE_CANT_OPERATE);
        }
        // 删除请验单配置
        documentConfigMapper.deleteById(id);
        // 删除配置数据
        documentConfigFieldMapper.deleteByConfigId(id);
        // 删除检品绑定关系
        documentConfigMaterialMapper.deleteByConfigId(id);
    }

    @Override
    public void enableInspectionConfig(Long id) {
        changeStatus(id, Boolean.TRUE);
    }

    @Override
    public void disableInspectionConfig(Long id) {
        // 若已有请验单使用该模板，则不允许停用
        if (inspectionOrderMapper.existsByTemplateId(id)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_USED_CANT_DISABLE);
        }
        changeStatus(id, Boolean.FALSE);
    }

    private void changeStatus(Long id, Boolean enable) {
        DocumentConfig documentConfig = documentConfigMapper.selectById(id);
        if (Objects.isNull(documentConfig)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NOT_EXISTS);
        }
        documentConfig.setStatus(enable);
        documentConfigMapper.updateById(documentConfig);
    }

    @Override
    public DocumentConfigWithFieldDTO queryDetail(Long id) {
        DocumentConfig documentConfig = documentConfigMapper.selectById(id);
        if (Objects.isNull(documentConfig)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NOT_EXISTS);
        }
        List<DocumentConfigField> dataList = documentConfigFieldMapper.selectByConfigId(id);
        return InspectionDocumentConfigConvert.INSTANCE.convert2ConfigDetail(documentConfig, dataList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindProduct(DocumentConfigBindProductDTO dto) {
        // 检品绑定
        DocumentConfig inspectionConfig = documentConfigMapper.selectById(dto.getId());
        if (Objects.isNull(inspectionConfig)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NOT_EXISTS);
        }
        // 多对多绑定：仅替换当前请验单模板下的绑定关系，不影响检品与其它模板的绑定
        documentConfigMaterialMapper.deleteByConfigId(dto.getId());
        if (CollUtil.isEmpty(dto.getMaterialIdList())) {
            return;
        }
        List<DocumentConfigMaterial> inspectConfigMaterialList = InspectionDocumentConfigConvert.INSTANCE.convert2InspectionConfigProductList(dto);
        documentConfigMaterialMapper.insertBatch(inspectConfigMaterialList);
    }

    @Override
    public List<Long> queryBindProductIdList(Long id) {
        List<DocumentConfigMaterial> list = documentConfigMaterialMapper.selectByConfigId(id);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return CollectionUtils.convertList(list, DocumentConfigMaterial::getProductId);
    }

    @Override
    public List<DocumentConfigWithFieldDTO> getInspectionConfigListByProductId(Long productId) {
        List<DocumentConfigMaterial> configIds = documentConfigMaterialMapper.selectConfigsByProductId(productId);
        if (CollUtil.isEmpty(configIds)) {
            return new ArrayList<>();
        }
        List<DocumentConfig> configs = documentConfigMapper.selectBatchIds(CollectionUtils.convertList(configIds, DocumentConfigMaterial::getConfigId));
        return InspectionDocumentConfigConvert.INSTANCE.convert2VOList(configs);
    }

    @Override
    public List<DocumentConfigWithFieldDTO> getEnabledInspectionConfigListByProductId(Long productId) {
        List<DocumentConfigMaterial> configIds = documentConfigMaterialMapper.selectConfigsByProductId(productId);
        if (CollUtil.isEmpty(configIds)) {
            return new ArrayList<>();
        }
        List<Long> ids = CollectionUtils.convertList(configIds, DocumentConfigMaterial::getConfigId);
        if (CollUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        // 过滤启用状态
        List<DocumentConfig> configs = documentConfigMapper.selectBatchIds(ids);
        if (CollUtil.isEmpty(configs)) {
            return new ArrayList<>();
        }
        List<DocumentConfig> enabled = new java.util.ArrayList<>();
        for (DocumentConfig c : configs) {
            if (Boolean.TRUE.equals(c.getStatus())) {
                enabled.add(c);
            }
        }
        return InspectionDocumentConfigConvert.INSTANCE.convert2VOList(enabled);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFieldSort(DocumentConfigFieldSortUpdateDTO dto) {
        DocumentConfig documentConfig = documentConfigMapper.selectById(dto.getId());
        if (Objects.isNull(documentConfig)) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NOT_EXISTS);
        }
        if (documentConfig.getStatus()) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_ENABLE_CANT_OPERATE);
        }
        if (CollUtil.isEmpty(dto.getSorts())) {
            return;
        }
        // 批量更新字段的 sort
        for (FieldSortDTO sort : dto.getSorts()) {
            DocumentConfigField field = new DocumentConfigField();
            field.setId(sort.getFieldId());
            field.setSort(sort.getSort());
            documentConfigFieldMapper.updateById(field);
        }
    }
}
