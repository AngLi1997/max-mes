package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel("记录项VO")
public class ProcessRecordVO {

    @ApiModelProperty("工序步骤模型id")
    private Long id;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;

    @ApiModelProperty("记录项名称")
    private String recordItemName;

    @ApiModelProperty("工序步骤名称")
    private String procedureStepName;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty("是否复用")
    private Boolean reusable;

}
