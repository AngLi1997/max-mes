package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("复制记录项DTO")
public class RecordCopySaveDTO {

    @ApiModelProperty(value = "批号",required = true)
    @NotEmpty
    private String batchNo;

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "工艺版本",required = true)
    @NotEmpty
    private String processVersion;

    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull
    private Long recordItemId;

    @ApiModelProperty(value = "记录项版本id",required = true)
    @NotNull
    private Long recordVersionId;

    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull
    private Boolean reuse;

    @ApiModelProperty(value = "复制版本号",required = true)
    @NotNull
    private Long copyVersion;

    @ApiModelProperty(value = "工序换班次数",required = true)
    @NotNull
    private Integer procedureChangeNumber;

    @ApiModelProperty(value = "工艺换班次数",required = true)
    @NotNull
    private Integer processChangeNumber;
}
