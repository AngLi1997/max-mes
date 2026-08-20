package com.bmos.mes.service.ingredient.weigh.vo;

import com.bmos.mes.common.enums.ingredient.IngredientWeighStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/29 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRecordCalc {

    private BigDecimal weighedQuantity = BigDecimal.ZERO;

    private IngredientWeighStatus ingredientWeighStatus;
}
