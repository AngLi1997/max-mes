package com.bmos.mes.service.workflow.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "强制激活步骤dto")
public class CoerceActivateStepDTO {

    @ApiModelProperty("任务实例id")
    @NotBlank
    private String executionId;

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;

    @ApiModelProperty("工序步骤id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("强制开启人")
    @NotBlank
    private String userId;
}
