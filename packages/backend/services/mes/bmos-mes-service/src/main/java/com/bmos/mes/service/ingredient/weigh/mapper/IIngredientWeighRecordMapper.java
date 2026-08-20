package com.bmos.mes.service.ingredient.weigh.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighRecord;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientWeighStorageMaterialVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * 配料称量记录 mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 17:47
 */
@Mapper
public interface IIngredientWeighRecordMapper extends BaseMapperX<IngredientWeighRecord> {

    default List<IngredientWeighRecord> queryByWeighBatchProcessId(Long batchProcessId, WeighType weighType) {
        if (batchProcessId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighRecord.class)
                .eq(IngredientWeighRecord::getIngredientWeighBatchProcessId, batchProcessId)
                .eq(weighType != null, IngredientWeighRecord::getWeighType, weighType)
        );
    }

    /**
     * 根据称量批次和称量类型查询物料信息列表
     *
     * @param ingredientPlanId 称量计划id
     * @param weighType        称量类型
     * @return
     */
    List<IngredientWeighStorageMaterialVO> queryWeighedStorageMaterialList(@Param("ingredientPlanId") Long ingredientPlanId,
                                                                           @Param("weighType") WeighType weighType,
                                                                           @Param("componentId") Long componentId,
                                                                           @Param("procedureStepModelId") Long procedureStepModelId,
                                                                           @Param("reuse") Boolean reuse,
                                                                           @Param("copyVersion") Long copyVersion);

    /**
     * 根据物料件id查询称量信息
     *
     * @param id
     * @return
     */
    default IngredientWeighRecord queryByStorageMaterialId(@Param("id") Long id) {
        if (id == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(IngredientWeighRecord.class)
                .eq(IngredientWeighRecord::getStorageMaterialId, id)
        );
    }

    /**
     * 根据配料计划id查询称量记录
     *
     * @param ingredientPlanId
     * @return
     */
    default List<IngredientWeighRecord> queryByIngredientPlanId(Long ingredientPlanId) {
        if (ingredientPlanId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighRecord.class)
                .eq(IngredientWeighRecord::getIngredientPlanId, ingredientPlanId)
        );
    }

    /**
     * 根据配料计划id和批次id查询称量记录
     *
     * @param ingredientPlanId       配料计划id
     * @param storageMaterialBatchId 批次id
     * @return
     */
    default List<IngredientWeighRecord> queryByIngredientPlanIdAndBatchId(Long ingredientPlanId, Long storageMaterialBatchId) {
        if (ingredientPlanId == null || storageMaterialBatchId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighRecord.class)
                .eq(IngredientWeighRecord::getIngredientPlanId, ingredientPlanId)
                .eq(IngredientWeighRecord::getStorageMaterialBatchId, storageMaterialBatchId)
        );
    }

    default List<IngredientWeighRecord> queryByBatchProcessIds(List<Long> batchProcessIds){
        if (CollectionUtil.isEmpty(batchProcessIds)){
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(IngredientWeighRecord.class)
                .in(IngredientWeighRecord::getIngredientWeighBatchProcessId, batchProcessIds)
        );
    }
}
