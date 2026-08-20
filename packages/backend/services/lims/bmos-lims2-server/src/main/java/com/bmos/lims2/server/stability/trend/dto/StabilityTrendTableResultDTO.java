package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性趋势查询表格结果（基础信息+时间点列+数据行）
 */
@Getter
@Setter
public class StabilityTrendTableResultDTO {

    private StabilityTrendTableHeaderDTO basicInfo;
    private List<StabilityTrendTimepointColumnDTO> timepointColumns;
    private List<StabilityTrendDataRowDTO> dataRows;
}
