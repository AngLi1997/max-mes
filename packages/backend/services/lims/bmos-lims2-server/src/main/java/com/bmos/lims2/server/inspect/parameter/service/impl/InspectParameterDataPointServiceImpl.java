package com.bmos.lims2.server.inspect.parameter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterDataPoint;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterDataPointMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointService;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointOptionService;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointTrendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 分析项数据点Service实现类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Service
@Slf4j
public class InspectParameterDataPointServiceImpl implements InspectParameterDataPointService {

    @Autowired
    private InspectParameterDataPointMapper dataPointMapper;

    @Autowired
    private InspectParameterDataPointOptionService optionService;

    @Autowired
    private InspectParameterDataPointTrendService trendService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDataPoints(Long parameterId, List<InspectParameterDataPointDTO> dataPoints) {
        if (CollectionUtil.isEmpty(dataPoints)) {
            return;
        }

        // 验证数据点名称在同一分析项中的唯一性
        Set<String> nameSet = new HashSet<>();
        for (InspectParameterDataPointDTO dataPoint : dataPoints) {
            if (nameSet.contains(dataPoint.getName())) {
                throw new BmosException(LimsResponseCode.DATA_POINT_NAME_DUPLICATE);
            }
            nameSet.add(dataPoint.getName());
        }

        // 删除原有数据点及其关联数据
        deleteByParameterId(parameterId);

        // 保存数据点
        for (InspectParameterDataPointDTO dataPoint : dataPoints) {
            // 设置分析项id
            dataPoint.setParameterId(parameterId);
            
            // 保存数据点
            InspectParameterDataPoint entity = BeanUtil.copyProperties(dataPoint, InspectParameterDataPoint.class);
            entity.setId(IdUtils.getSnowflake());
            dataPointMapper.insert(entity);

            // 保存选项配置
            if (AnalyzeResultTypeEnum.OPTION.equals(dataPoint.getResultType()) 
                && CollectionUtil.isNotEmpty(dataPoint.getOptions())) {
                optionService.saveOptions(entity.getId(), dataPoint.getOptions());
            }

            // 保存趋势线配置
            if (AnalyzeResultTypeEnum.NUMBER.equals(dataPoint.getResultType()) 
                && CollectionUtil.isNotEmpty(dataPoint.getTrends())) {
                trendService.saveTrends(entity.getId(), dataPoint.getTrends());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByParameterId(Long parameterId) {
        // 查询数据点列表
        List<InspectParameterDataPoint> dataPoints = dataPointMapper.selectByParameterId(parameterId);
        if (CollectionUtil.isEmpty(dataPoints)) {
            return;
        }

        // 删除数据点关联的选项和趋势线配置
        for (InspectParameterDataPoint dataPoint : dataPoints) {
            optionService.deleteByDataPointId(dataPoint.getId());
            trendService.deleteByDataPointId(dataPoint.getId());
        }

        // 删除数据点
        dataPointMapper.deleteBatchIds(dataPoints.stream().map(InspectParameterDataPoint::getId).collect(Collectors.toList()));
    }

    @Override
    public List<InspectParameterDataPointDTO> getDataPointsByParameterId(Long parameterId) {
        // 查询数据点列表
        List<InspectParameterDataPoint> dataPoints = dataPointMapper.selectByParameterId(parameterId);
        if (CollectionUtil.isEmpty(dataPoints)) {
            return Collections.emptyList();
        }

        // 转换为DTO
        List<InspectParameterDataPointDTO> dtos = BeanUtil.copyToList(dataPoints, InspectParameterDataPointDTO.class);

        // 查询关联的选项和趋势线配置
        for (InspectParameterDataPointDTO dto : dtos) {
            if (AnalyzeResultTypeEnum.OPTION.equals(dto.getResultType())) {
                dto.setOptions(optionService.getOptionsByDataPointId(dto.getId()));
            } else if (AnalyzeResultTypeEnum.NUMBER.equals(dto.getResultType())) {
                dto.setTrends(trendService.getTrendsByDataPointId(dto.getId()));
            }
        }

        return dtos;
    }
} 