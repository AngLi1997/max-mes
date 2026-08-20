package com.bmos.lims2.server.stability.scheme.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 稳定性方案检验计划响应DTO
 */
@Data
public class StabilitySchemePlanDTO {

    private Long id;

    private Long schemeId;

    private Long versionId;

    private String experimentType;

    private String storageCondition;

    private String totalSampleAmount;

    private String totalSampleUnit;

    private Integer sort;

    private List<StabilitySchemePlanTimepointDTO> timepoints;
}
