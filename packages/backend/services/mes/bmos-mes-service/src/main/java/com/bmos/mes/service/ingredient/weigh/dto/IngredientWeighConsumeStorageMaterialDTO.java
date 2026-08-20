package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 称量消耗物料件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("称量消耗物料件")
@Data
public class IngredientWeighConsumeStorageMaterialDTO {

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1", required = true)
    @NotNull
    private Long ingredientPlanId;

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 消耗物料件id列表
     */
    @ApiModelProperty(value = "消耗物料件id列表")
    private List<Long> consumeStorateMaterialIdList = new ArrayList<>();
}
