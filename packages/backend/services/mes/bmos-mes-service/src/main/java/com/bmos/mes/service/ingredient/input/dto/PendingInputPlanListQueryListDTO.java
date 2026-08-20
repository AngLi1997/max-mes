package com.bmos.mes.service.ingredient.input.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("待投料配料单查询DTO")
@Data
public class PendingInputPlanListQueryListDTO {

    @ApiModelProperty("生产批次/生产计划id")
    @NotNull
    private Long productPlanId;

}
