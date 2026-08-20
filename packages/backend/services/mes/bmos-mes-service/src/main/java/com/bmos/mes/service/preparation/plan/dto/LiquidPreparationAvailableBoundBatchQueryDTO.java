package com.bmos.mes.service.preparation.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("配液计划获取可用及已添加批次查询DTO")
public class LiquidPreparationAvailableBoundBatchQueryDTO {

    @ApiModelProperty("配料计划id")
    @NotNull
    private Long preparationPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

}
