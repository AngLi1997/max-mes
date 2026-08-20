package com.bmos.mes.service.preparation.plan.service;

import com.bmos.mes.service.preparation.measure.vo.UnmeasuredPreparationPlanVO;
import com.bmos.mes.service.preparation.plan.dto.*;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationAvailableBoundMaterialBatchVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationBoundMaterialBatchVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationPlanInstanceVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationQuantityCalculateVO;

import java.util.List;

public interface LiquidPreparationPlanService {
    /**
     * 获取配液单实例
     * @param dto
     * @return
     */
    LiquidPreparationPlanInstanceVO getPreparationPlanInstance(LiquidPreparationPlanInstanceQueryDTO dto);

    /**
     * 获取已绑定配液批次
     * @param dto
     * @return
     */
    List<LiquidPreparationBoundMaterialBatchVO> getBoundMaterialBatch(LiquidPreparationBoundBatchQueryDTO dto);

    /**
     * 获取已绑定和可绑定配液批次
     * @param dto
     * @return
     */
    List<LiquidPreparationAvailableBoundMaterialBatchVO> getBoundAndAvailableMaterialBatch(LiquidPreparationAvailableBoundBatchQueryDTO dto);

    /**
     * 完成配液计划
     * @param dto
     */
    void completePreparationPlan(LiquidPreparationPlanCompleteDTO dto);

    /**
     * 配液量计算
     * @param dto
     * @return
     */
    LiquidPreparationQuantityCalculateVO calculatePreparationQuantity(LiquidPreparationQuantityCalculateDTO dto);

    /**
     * 绑定配液批次
     * @param dto
     */
    void BindMaterialBatch(LiquidPreparationBindMaterialBatchDTO dto);

    /**
     * 获取未量取的配液单列表
     * @param productPlanId
     * @return
     */
    List<UnmeasuredPreparationPlanVO> getUnmeasuredPreparationPlanList(Long productPlanId);
}
