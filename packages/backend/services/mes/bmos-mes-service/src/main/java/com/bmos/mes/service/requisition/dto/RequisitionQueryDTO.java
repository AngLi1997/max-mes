package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("领料单查询dto")
@Data
public class RequisitionQueryDTO {

    @ApiModelProperty("工序步骤模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("组件标识id")
    @NotNull
    private Long fieldId;

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("组件类型/领料类型")
    @NotEmpty
    private String componentType;

    @ApiModelProperty("复制版本")
    private Long copyVersion;

}
