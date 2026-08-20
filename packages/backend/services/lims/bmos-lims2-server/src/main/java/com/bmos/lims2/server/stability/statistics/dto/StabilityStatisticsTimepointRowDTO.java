package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 稳定性统计时间点行（某批号下单个时间点的数据）
 */
@Getter
@Setter
public class StabilityStatisticsTimepointRowDTO {

    private Integer timeValue;
    private String timeUnit;
    private Map<Long, String> dataPointValues;
}
