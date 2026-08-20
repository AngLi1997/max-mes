package com.bmos.lims2.server.stability.statistics.dto;

import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性考察计划下拉选项DTO
 */
@Getter
@Setter
public class StabilityPlanOptionDTO {

    @ApiModelProperty("计划ID")
    private Long id;

    @ApiModelProperty("稳定性考察编号")
    private String code;

    @ApiModelProperty("计划状态")
    private StabilityInspectPlanStatusEnum status;
}
