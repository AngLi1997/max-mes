package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性统计分析项表头
 */
@Getter
@Setter
public class StabilityStatisticsParameterHeaderDTO {

    private Long parameterId;
    private Long schemeVersionId;
    private String parameterCode;
    private String parameterName;
    private List<StabilityStatisticsDataPointHeaderDTO> dataPoints;
}
