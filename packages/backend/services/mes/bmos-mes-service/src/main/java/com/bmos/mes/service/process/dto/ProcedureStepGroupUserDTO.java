package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("工序步骤班组人员")
public class ProcedureStepGroupUserDTO {

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤节点id",required = true)
    @NotNull
    private String nodeId;

    @ApiModelProperty(value = "换班类型")
    private String nodeFunction;

    @ApiModelProperty(value = "工艺换班次数")
    @NotNull
    private Integer processChangeNumber;

    @ApiModelProperty(value = "工序换班次数")
    @NotNull
    private Integer procedureChangeNumber;
}
