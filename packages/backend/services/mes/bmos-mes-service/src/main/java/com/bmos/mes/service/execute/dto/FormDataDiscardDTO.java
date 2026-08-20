package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("作废记录项DTO")
public class FormDataDiscardDTO {

    @ApiModelProperty("生产计划id")
    @NonNull
    private Long productPlanId;

    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty("工序步骤id")
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty("复制版本号")
    @NotNull
    private Long copyVersion;

    @ApiModelProperty("是否复用")
    @NotNull
    private Boolean reuse;
}
