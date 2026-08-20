package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("领料计划预定物料查询DTO")
@Data
public class RequisitionMaterialQueryDTO {

    @ApiModelProperty("领料计划id")
    @NotNull
    private Long requisitionPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

    @ApiModelProperty("领料计划类型/组件类型")
    @NotEmpty
    private String componentType;
}
