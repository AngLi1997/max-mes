package com.bmos.mes.service.ingredient.input.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("投料组件实例VO")
@Data
public class InputComponentInstanceVO {

    @ApiModelProperty("配料计划id")
    private Long ingredientPlanId;

    @ApiModelProperty("组件实例id")
    private Long componentInstanceId;

    @ApiModelProperty("配料计划名称")
    private String ingredientPlanName;

}
