package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性趋势查询数据行（一个批次一行）
 */
@Getter
@Setter
public class StabilityTrendDataRowDTO {

    private String batchNo;
    private List<TimepointValue> timepointValues;

    @Getter
    @Setter
    public static class TimepointValue {
        private Integer timeValue;
        private String timeUnit;
        private String value;
    }
}
