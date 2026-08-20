package com.bmos.mes.service.ingredient.weigh.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighProcess;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 配料称量流程 mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 17:47
 */
@Mapper
public interface IIngredientWeighProcessMapper extends BaseMapperX<IngredientWeighProcess> {

    default IngredientWeighProcess getIngredientWeighProcess(Long productPlanId, Long componentId, Long procedureStepModelId, Long copyVersion, Boolean reuse) {
        if (productPlanId == null || componentId == null || procedureStepModelId == null || copyVersion == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(IngredientWeighProcess.class)
                .eq(IngredientWeighProcess::getProductPlanId, productPlanId)
                .eq(IngredientWeighProcess::getComponentId, componentId)
                .eq(!reuse, IngredientWeighProcess::getProcedureStepModelId, procedureStepModelId)
                .eq(reuse, IngredientWeighProcess::getReuse, reuse)
                .eq(IngredientWeighProcess::getCopyVersion, copyVersion)
        );
    }

    default IngredientWeighProcess getIngredientWeighProcessByIngredientPlanId(Long ingredientPlanId, Long componentId, Long procedureStepModelId, Long copyVersion, Boolean reuse) {
        if (ingredientPlanId == null || componentId == null || procedureStepModelId == null || copyVersion == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(IngredientWeighProcess.class)
                .eq(IngredientWeighProcess::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientWeighProcess::getComponentId, componentId)
                .eq(!reuse, IngredientWeighProcess::getProcedureStepModelId, procedureStepModelId)
                .eq(reuse, IngredientWeighProcess::getReuse, reuse)
                .eq(IngredientWeighProcess::getCopyVersion, copyVersion)
        );
    }

    default List<IngredientWeighProcess> getIngredientWeighProcess(List<Long> productPlanIds) {
        if (CollectionUtil.isEmpty(productPlanIds)){
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighProcess.class)
                .in(IngredientWeighProcess::getProductPlanId, productPlanIds)
        );
    }

    /**
     * 根据称量计划id查询进度
     *
     * @param ingredientPlanId 称量计划id
     * @return
     */
//    default IngredientWeighProcess getIngredientProcessByPlanId(Long ingredientPlanId) {
//        if (ingredientPlanId == null) {
//            return null;
//        }
//        return selectOne(Wrappers.lambdaQuery(IngredientWeighProcess.class)
//                .eq(IngredientWeighProcess::getIngredientPlanId, ingredientPlanId)
//        );
//    }
}
