package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointDTO;

import java.util.List;

/**
 * 分析项数据点Service接口
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
public interface InspectParameterDataPointService {

    /**
     * 保存数据点
     */
    void saveDataPoints(Long parameterId, List<InspectParameterDataPointDTO> dataPoints);

    /**
     * 根据分析项id删除数据点
     */
    void deleteByParameterId(Long parameterId);

    /**
     * 根据分析项id获取数据点列表
     */
    List<InspectParameterDataPointDTO> getDataPointsByParameterId(Long parameterId);
} 