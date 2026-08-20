package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("工艺下的工序步骤VO")
public class ProcessStepVO {

    private Long id;

    @ApiModelProperty("工序步骤名称")
    private String name;

    @ApiModelProperty("历史工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("历史工序id")
    private Long procedureId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("是否复用")
    private Boolean reusable;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

}
