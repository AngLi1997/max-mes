package com.bmos.mes.service.ingredient.weigh.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/17 17:07
 */
@Data
@ApiModel("配料单vo")
public class IngredientPlanItemVO {

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1")
    private Long id;

    /**
     * 配料单名称
     */
    @ApiModelProperty(value = "配料单名称", example = "人血白蛋白-2402016-01")
    private String name;

    /**
     * 计划批次数目
     */
    @JsonIgnore
    private Integer planBatchCount;

    /**
     * 已称量批次数目
     */
    @JsonIgnore
    private Integer weighBatchCount;
}
