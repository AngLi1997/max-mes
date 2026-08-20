package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性统计查询结果（表头+数据分离）
 */
@Getter
@Setter
public class StabilityStatisticsResultDTO {

    private StabilityStatisticsHeaderDTO header;

    private List<StabilityStatisticsDataRowDTO> data;
}
