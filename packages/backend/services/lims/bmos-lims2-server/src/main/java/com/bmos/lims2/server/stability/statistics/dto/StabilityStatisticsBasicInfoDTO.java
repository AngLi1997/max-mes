package com.bmos.lims2.server.stability.statistics.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性统计基本信息
 */
@Getter
@Setter
public class StabilityStatisticsBasicInfoDTO {

    private String materialName;
    private String materialCode;
    private String materialSpec;
    private Long schemeId;
    private String schemeName;
}
