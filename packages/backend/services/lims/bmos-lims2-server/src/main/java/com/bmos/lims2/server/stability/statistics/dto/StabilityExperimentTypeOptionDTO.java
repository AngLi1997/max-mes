package com.bmos.lims2.server.stability.statistics.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性试验类型下拉选项DTO
 */
@Getter
@Setter
public class StabilityExperimentTypeOptionDTO {

    @ApiModelProperty("试验类型（字典值）")
    private String experimentType;

    @ApiModelProperty("试验类型名称（字典标签）")
    private String experimentTypeName;

    @ApiModelProperty("储存条件")
    private String storageCondition;
}
