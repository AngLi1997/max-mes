package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性统计表头信息
 */
@Getter
@Setter
public class StabilityStatisticsHeaderDTO {

    private Long schemeId;
    private String materialName;
    private String materialCode;
    private String materialSpec;
    private String experimentType;
    private String experimentTypeName;
    private String storageCondition;
    private String schemeName;

    private List<StabilityStatisticsParameterHeaderDTO> parameters;
}
