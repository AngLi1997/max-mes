package com.bmos.mes.service.ingredient.plan.service;

import com.bmos.mes.service.ingredient.plan.dto.*;
import com.bmos.mes.service.ingredient.plan.vo.*;

import java.util.List;

public interface IngredientService {
    IngredientPlanVO getMaterialIngredientPlanVO(IngredientQueryDTO dto);

    List<AvailableAndBoundMaterialBatchVO> getAvailableAndAddedMaterialBatch(IngredientAvailableAndBoundBatchQueryDTO dto);

    void ingredientBindMaterialBatch(IngredientBindMaterialBatchDTO dto);

    List<IngredientBoundMaterialBatchVO> getBoundMaterialBatch(IngredientBoundMaterialBatchQueryDTO dto);

    void completeIngredientPlan(IngredientPlanCompleteDTO dto);

    IngredientQuantityCalculateVO calculateTheoreticalQuantity(TheoreticalQuantityCalculateDTO dto);

    IngredientQuantityListCalculateVO calculateIngredientQuantity(IngredientQuantityCalculateDTO dto);
}
