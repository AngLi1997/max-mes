package com.bmos.mes.service.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 发起业务流
 */
@Getter
@Setter
@ToString
@ApiModel("发起流程DTO")
@Builder
public class StartWorkflowDTO {
    @Tolerate
    public StartWorkflowDTO(){}

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotEmpty
    private String processVersion;

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

}
