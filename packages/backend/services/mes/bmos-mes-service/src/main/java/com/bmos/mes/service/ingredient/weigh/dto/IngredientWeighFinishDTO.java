package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 完成称量DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 17:20
 */
@Data
@ApiModel("完成称量DTO")
public class IngredientWeighFinishDTO {

    /**
     * 配料计划id
     */
    @ApiModelProperty("配料计划id")
    @NotNull
    private Long ingredientPlanId;

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 完成人id
     */
    @ApiModelProperty(value = "完成人id", example = "1")
    @NotNull
    private String finisherId;
}
