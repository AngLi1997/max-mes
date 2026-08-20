package com.bmos.mes.service.ingredient.weigh.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.common.enums.ingredient.IngredientWeighStatus;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighBatchProcess;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 配料称量批次流程 mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 17:47
 */
@Mapper
public interface IIngredientWeighBatchProcessMapper extends BaseMapperX<IngredientWeighBatchProcess> {

    /**
     * 根据称量计划id和物料批次id查询批次称量状态
     *
     * @param ingredientPlanId       称量计划id
     * @param storageMaterialBatchId 暂存批次id
     * @return
     */
    default IngredientWeighBatchProcess queryByIngredientPlanIdAndStorageMaterialBatchId(Long ingredientPlanId, Long storageMaterialBatchId) {
        if (ingredientPlanId == null || storageMaterialBatchId == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(IngredientWeighBatchProcess.class)
                .eq(IngredientWeighBatchProcess::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientWeighBatchProcess::getStorageMaterialBatchId, storageMaterialBatchId)
        );
    }

    default List<IngredientWeighBatchProcess> queryByIngredientPlanId(Long ingredientPlanId) {
        if (ingredientPlanId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighBatchProcess.class)
                .eq(IngredientWeighBatchProcess::getIngredientPlanId, ingredientPlanId)
        );
    }

    default List<IngredientWeighBatchProcess> queryProcessingBatchByPlanId(Long ingredientPlanId){
        if (ingredientPlanId == null){
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighBatchProcess.class)
                .eq(IngredientWeighBatchProcess::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientWeighBatchProcess::getWeighStatus, IngredientWeighStatus.PROCESSING.getValue()));
    }

    default IngredientWeighBatchProcess queryBatchProcessByComponent(Long ingredientPlanId, Long componentId, Long copyVersion, Long procedureStepModelId, Boolean reuse, IngredientWeighStatus weighStatus){
        return selectOne(Wrappers.lambdaQuery(IngredientWeighBatchProcess.class)
                .eq(IngredientWeighBatchProcess::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientWeighBatchProcess::getComponentId, componentId)
                .eq(IngredientWeighBatchProcess::getCopyVersion, copyVersion)
                .eq(reuse, IngredientWeighBatchProcess::getReuse, reuse)
                .eq(!reuse, IngredientWeighBatchProcess::getProcedureStepModelId, procedureStepModelId)
                .eq(IngredientWeighBatchProcess::getWeighStatus, weighStatus.getValue())
        );
    }

    default List<IngredientWeighBatchProcess> queryBatchProcessByComponent(Long ingredientPlanId, Long componentId, Long copyVersion, Long procedureStepModelId, Boolean reuse) {
        return selectList(Wrappers.lambdaQuery(IngredientWeighBatchProcess.class)
                .eq(IngredientWeighBatchProcess::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientWeighBatchProcess::getComponentId, componentId)
                .eq(IngredientWeighBatchProcess::getCopyVersion, copyVersion)
                .eq(reuse, IngredientWeighBatchProcess::getReuse, reuse)
                .eq(!reuse, IngredientWeighBatchProcess::getProcedureStepModelId, procedureStepModelId)
        );
    }

    default List<IngredientWeighBatchProcess> queryList(List<Long> ingredientWeighProcessIdList, List<Long> procedureStepModelIds){
        if (CollectionUtil.isEmpty(ingredientWeighProcessIdList) || CollectionUtil.isEmpty(procedureStepModelIds)){
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighBatchProcess.class)
                .in(IngredientWeighBatchProcess::getIngredientWeighProcessId, ingredientWeighProcessIdList)
                .in(IngredientWeighBatchProcess::getProcedureStepModelId, procedureStepModelIds)
        );
    }
}
