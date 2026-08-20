package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 记录作业查询
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 15:00
 */
@Data
@ApiModel("记录作业查询")
public class InputWeighProcessQuery {

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1", required = true)
    @NotNull
    private Long productPlanId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty(value = "工序步骤模型id", example = "1", required = true)
    @NotNull
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    @ApiModelProperty(value = "拷贝版本(默认0)", example = "0", required = true)
    @NotNull
    private Long copyVersion = 0L;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1")
    @NotNull
    private Long componentId;
}
