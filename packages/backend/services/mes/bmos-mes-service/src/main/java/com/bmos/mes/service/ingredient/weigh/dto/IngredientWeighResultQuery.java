package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/12 12:00
 */
@Data
@ApiModel("配料称量结果查询dto")
public class IngredientWeighResultQuery {

    /**
     * 配料计划id
     */
    @ApiModelProperty(value = "配料计划id", example = "1", required = true)
    @NotNull
    private Long planId;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1", required = true)
    @NotNull
    private Long componentId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty(value = "工序步骤模型id", example = "1", required = true)
    @NotNull
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    @ApiModelProperty(value = "拷贝版本(默认0)", example = "1", required = true)
    @NotNull
    private Long copyVersion;
}
