package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterOptionDTO;

import java.util.List;

/**
 * 分析项数据点选项Service接口
 */
public interface InspectParameterDataPointOptionService {

    /**
     * 保存分析项选项
     */
    void saveOptions(Long parameterId, List<InspectParameterOptionDTO> options);

    void deleteByDataPointId(Long dataPointId);


    List<InspectParameterOptionDTO> getOptionsByDataPointId(Long dataPointId);
} 