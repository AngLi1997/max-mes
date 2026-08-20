package com.bmos.mes.service.ingredient.weigh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 17:52
 */
@Data
@ApiModel("配料称量信息")
public class IngredientWeighProcessVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty(value = "工序步骤模型id", example = "1")
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    @ApiModelProperty(value = "拷贝版本", example = "1")
    private Long copyVersion;

    /**
     * 配料计划id
     */
    @ApiModelProperty(value = "配料计划id", example = "1")
    private Long ingredientPlanId;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1")
    private Long componentId;

    /**
     * 称量中的批次id
     */
    @ApiModelProperty(value = "称量批次id", example = "1")
    private Long pendingStorageMaterialBatchId;

    /**
     * 称量中的批次编号
     */
    @ApiModelProperty(value = "称量中的批次编号", example = "1")
    private String pendingStorageMaterialBatchNo;

    /**
     * 物料名称(物料合并编码 - 物料名称)
     */
    @ApiModelProperty(value = "物料名称(物料合并编码 - 物料名称)", example = "YH101001 - 盐酸组氨")
    private String pendingStorageMaterialFullName;

    /**
     * 称量中的批次单位
     */
    @ApiModelProperty(value = "称量中的批次单位", example = "kg")
    private String pendingStorageMaterialBatchUnit;

    /**
     * 称量中的批次单位id
     */
    @ApiModelProperty(value = "称量中的批次单位id", example = "1")
    private Long pendingStorageMaterialBatchUnitId;

    /**
     * 配料计划名称
     */
    @ApiModelProperty(value = "配料计划名称", example = "人血白蛋白-2402016-01")
    private String ingredientPlanName;

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id", example = "1")
    private List<Long> station = new ArrayList<>();
}
