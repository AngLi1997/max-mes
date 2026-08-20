package com.bmos.mes.service.preparation.plan.repository;

import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;

import java.util.List;

/**
 * 配液计划对内接口
 */
public interface PreparationPlanRepository {

    /**
     * 根据配液计划id查询配液计划
     * @param id
     * @return
     */
    LiquidPreparationPlan selectById(Long id);

    /**
     * 查询当前生产计划下未投入的配液计划
     * @param productPlanId
     * @return
     */
    List<LiquidPreparationPlan> selectInputPlanList(Long productPlanId);

    /**
     * 根据配液计划id查询配液量取记录(不含余液量取)
     * @param preparationPlanId
     * @return
     */
    List<LiquidPreparationMeasureRecord> selectLiquidMeasureRecordByPreparationId(Long preparationPlanId);
}
