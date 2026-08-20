package com.bmos.mes.service.ingredient.input.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.IngredientWeighStatus;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 配料计划投料信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 22:32
 */
@Data
@ApiModel("配料计划投料信息")
public class IngredientInputPlanVO {

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1")
    private Long ingredientPlanId;

    /**
     * 配料单名称
     */
    @ApiModelProperty(value = "配料单名称", example = "配料单名称")
    private String ingredientPlanName;

    /**
     * 称量状态
     */
    @ApiModelEnumProperty(value = "称量状态", enumClass = IngredientWeighStatus.class)
    @EnumValidate(IngredientWeighStatus.class)
    private IngredientWeighStatus weighStatus;

    /**
     * 配料单待投料列表
     */
    @ApiModelProperty("配料单待投料列表")
    private List<IngredientInputRecordVO> inputList = new ArrayList<>();
}
