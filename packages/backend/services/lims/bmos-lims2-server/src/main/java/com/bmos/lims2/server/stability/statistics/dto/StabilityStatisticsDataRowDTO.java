package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性统计数据行（一个批号对应一项，内含多个时间点行）
 */
@Getter
@Setter
public class StabilityStatisticsDataRowDTO {

    private String batchNo;
    private List<StabilityStatisticsTimepointRowDTO> rows;
}
