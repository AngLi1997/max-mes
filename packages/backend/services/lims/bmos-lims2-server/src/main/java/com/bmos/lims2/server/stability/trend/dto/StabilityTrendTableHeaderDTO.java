package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询表格表头（基础信息）
 */
@Getter
@Setter
public class StabilityTrendTableHeaderDTO {

    // 检品信息
    private String materialName;
    private String materialCode;
    private String materialSpec;

    // 试验信息
    private String experimentType;
    private String experimentTypeName;
    private String storageCondition;

    // 分析项 & 数据点
    private String parameterName;
    private Long dataPointId;
    private String dataPointName;
    private String pointType;
}
