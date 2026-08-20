package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("复制记录项DTO")
public class RecordCopyQueryDTO {

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull
    private Long recordItemId;

    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull
    private Boolean reuse;

    @ApiModelProperty(value = "记录版本号",required = true)
    @NotNull
    private Long recordVersionId;

    @ApiModelProperty(value = "工序换班次数",required = true)
    @NotNull
    private Integer procedureChangeNumber;

    @ApiModelProperty(value = "工艺换班次数",required = true)
    @NotNull
    private Integer processChangeNumber;

    @ApiModelProperty("工步模型id")
    @NotNull
    private Long procedureStepModelId;
}
