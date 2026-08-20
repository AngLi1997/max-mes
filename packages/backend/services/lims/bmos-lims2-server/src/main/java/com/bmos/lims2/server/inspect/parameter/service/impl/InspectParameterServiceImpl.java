package com.bmos.lims2.server.inspect.parameter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.constants.BasicConstants;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.convert.InspectParameterConvert;
import com.bmos.lims2.server.inspect.item.mapper.InspectItemParameterMapper;
import com.bmos.lims2.server.inspect.parameter.dto.*;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameter;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterDataPoint;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointService;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterListDTO;

/**
 * 分析项Service实现类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Slf4j
@Service
public class InspectParameterServiceImpl implements InspectParameterService {

    @Autowired
    private InspectParameterMapper parameterMapper;

    @Autowired
    private InspectItemParameterMapper inspectItemParameterMapper;

    @Autowired
    private InspectParameterDataPointService dataPointService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InspectParameterDTO dto) {
        // 验证编码是否已存在
        if (parameterMapper.existByCode(dto.getCode())) {
            throw new BmosException(LimsResponseCode.PARAMETER_CODE_EXISTED);
        }

        // 保存分析项
        InspectParameter parameter = BeanUtil.copyProperties(dto, InspectParameter.class);
        parameterMapper.insert(parameter);

        // 保存数据点
        if (CollectionUtil.isNotEmpty(dto.getDataPoints())) {
            dataPointService.saveDataPoints(parameter.getId(), dto.getDataPoints());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, InspectParameterDTO dto) {
        // 验证分析项是否存在
        InspectParameter parameter = parameterMapper.selectById(id);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.PARAMETER_NOT_FOUND);
        }

        // 验证编码是否已存在
        if (!parameter.getCode().equals(dto.getCode()) && parameterMapper.existByCode(dto.getCode())) {
            throw new BmosException(LimsResponseCode.PARAMETER_CODE_EXISTED);
        }

        // 更新分析项
        parameter = BeanUtil.copyProperties(dto, InspectParameter.class);
        parameter.setId(id);
        parameterMapper.updateById(parameter);

        // 更新数据点
        dataPointService.saveDataPoints(id, dto.getDataPoints());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 验证分析项是否存在
        InspectParameter parameter = parameterMapper.selectById(id);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.PARAMETER_NOT_FOUND);
        }
        // 校验是否被检验项目绑定
        List<String> boundInspectItemNames = inspectItemParameterMapper.selectInspectItemNamesByParameterId(id);
        if (CollectionUtil.isNotEmpty(boundInspectItemNames)) {
            String joinedNames = String.join("，", boundInspectItemNames);
            throw new BmosException(LimsResponseCode.PARAMETER_BIND_INSPECT_ITEMS, joinedNames);
        }

        // 删除分析项
        parameterMapper.deleteById(id);

        // 删除数据点及其关联数据
        dataPointService.deleteByParameterId(id);
    }

    @Override
    public InspectParameterDTO getById(Long id) {
        // 查询分析项
        InspectParameter parameter = parameterMapper.selectById(id);
        if (parameter == null) {
            throw new BmosException(LimsResponseCode.PARAMETER_NOT_FOUND);
        }

        // 转换为DTO
        InspectParameterDTO dto = BeanUtil.copyProperties(parameter, InspectParameterDTO.class);

        // 查询数据点列表
        List<InspectParameterDataPointDTO> dataPoints = dataPointService.getDataPointsByParameterId(id);
        dto.setDataPoints(dataPoints);

        return dto;
    }

    @Override
    public CommonPage<InspectParameterDTO> getPage(InspectParameterPageReqDTO dto) {
        // 构建查询条件（按分析项编码正序排序）
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), "code asc");
        ParameterParamDTO param = InspectParameterConvert.INSTANCE.convert2Param(dto);
        List<InspectParameter> analyzeList = parameterMapper.selectParam(param);
        CommonPage<InspectParameter> commonPage = CommonPage.convertPage(analyzeList);
        CommonPage<InspectParameterDTO> inspectParameterDTOCommonPage = InspectParameterConvert.INSTANCE.convert2PageRespVO(commonPage);

        if (CollectionUtil.isEmpty(inspectParameterDTOCommonPage.getList())) {
            return inspectParameterDTOCommonPage;
        }

        // 查询数据点列表
        for (InspectParameterDTO parameterDTO : inspectParameterDTOCommonPage.getList()) {
            List<InspectParameterDataPointDTO> dataPoints = dataPointService.getDataPointsByParameterId(parameterDTO.getId());
            parameterDTO.setDataPoints(dataPoints);
        }

        return inspectParameterDTOCommonPage;
    }

    @Override
    public List<InspectParameterDTO> selectByIdList(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            return Collections.emptyList();
        }
        List<InspectParameter> entityList = parameterMapper.selectBatchIds(idList);
        return BeanUtil.copyToList(entityList, InspectParameterDTO.class);
    }

    @Override
    public List<InspectParameterListDTO> getList() {
        LambdaQueryWrapper<InspectParameter> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(InspectParameter::getId, InspectParameter::getCode, InspectParameter::getName)
               .eq(InspectParameter::getDeleted, false)
               .orderByDesc(InspectParameter::getCreateTime);
        
        List<InspectParameter> entityList = parameterMapper.selectList(wrapper);
        return BeanUtil.copyToList(entityList, InspectParameterListDTO.class);
    }

    @Override
    public List<InspectParameterDTO> selectFullInfoByIdList(List<Long> idList) {
        if (CollectionUtil.isEmpty(idList)) {
            return Collections.emptyList();
        }
        
        List<InspectParameter> entityList = parameterMapper.selectBatchIds(idList);
        List<InspectParameterDTO> dtoList = BeanUtil.copyToList(entityList, InspectParameterDTO.class);
        
        // 为每个分析项查询数据点信息
        for (InspectParameterDTO dto : dtoList) {
            List<InspectParameterDataPointDTO> dataPoints = dataPointService.getDataPointsByParameterId(dto.getId());
            dto.setDataPoints(dataPoints);
        }
        
        return dtoList;
    }
}
