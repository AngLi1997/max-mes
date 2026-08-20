package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性统计数据点表头
 */
@Getter
@Setter
public class StabilityStatisticsDataPointHeaderDTO {

    private Long dataPointId;
    private String dataPointName;
    private String pointType;
}
