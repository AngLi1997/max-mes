package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO;

import java.util.List;

/**
 * 分析项趋势线配置Service接口
 */
public interface InspectParameterDataPointTrendService {

    /**
     * 保存分析项趋势线配置
     */
    void saveTrends(Long parameterId, List<InspectParameterDataPointTrendDTO> trends);


    void deleteByDataPointId(Long dataPointId);

    List<InspectParameterDataPointTrendDTO> getTrendsByDataPointId(Long dataPointId);
} 