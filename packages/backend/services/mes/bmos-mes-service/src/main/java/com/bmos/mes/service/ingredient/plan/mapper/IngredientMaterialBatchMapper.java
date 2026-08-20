package com.bmos.mes.service.ingredient.plan.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatch;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IngredientMaterialBatchMapper extends BaseMapperX<IngredientMaterialBatch> {


    List<IngredientMaterialBatchDetailInfo> getByIngredientIdAndFormulaMaterialId(@Param("ingredientPlanId") Long ingredientPlanId,
                                                                                  @Param("formulaMaterialId") Long formulaMaterialId);

    default void deleteByIngredientPlanIdAndFormulaMaterialId(Long ingredientPlanId, Long formulaMaterialId) {
        delete(new LambdaQueryWrapperX<IngredientMaterialBatch>()
                .eq(IngredientMaterialBatch::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientMaterialBatch::getFormulaMaterialId, formulaMaterialId));
    }

    List<IngredientMaterialBatchDetailInfo> getByIngredientId(@Param("ingredientPlanId") Long ingredientId);

    /**
     * 根据配料计划id和物料批次id查询
     *
     * @param ingredientPlanId 配料计划id
     * @param materialBatchId  物料批次id
     * @return 配料计划物料批次
     */
    default IngredientMaterialBatch getByIngredientPlanIdAndMaterialBatchId(Long ingredientPlanId, Long materialBatchId) {
        if (ingredientPlanId == null || materialBatchId == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(IngredientMaterialBatch.class)
                .eq(IngredientMaterialBatch::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientMaterialBatch::getMaterialBatchId, materialBatchId)
        );
    }

    /**
     * 区别于上方getByIngredientIdAndFormulaMaterialId 该查询只有IngredientMaterialBatch信息
     * @param ingredientPlanId
     * @param formulaMaterialId
     * @return
     */
    default List<IngredientMaterialBatch> selectByIngredientIdAndFormulaMaterialId(Long ingredientPlanId, Long formulaMaterialId){
        return selectList(new LambdaQueryWrapperX<IngredientMaterialBatch>()
                .eq(IngredientMaterialBatch::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientMaterialBatch::getFormulaMaterialId, formulaMaterialId));
    }
}
