package com.bmos.mes.service.preparation.plan.repository.impl;

import com.bmos.mes.service.preparation.measure.mapper.LiquidPreparationMeasureRecordMapper;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.plan.mapper.LiquidPreparationPlanMapper;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.preparation.plan.repository.PreparationPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PreparationPlanRepositoryImpl implements PreparationPlanRepository {

    @Autowired
    LiquidPreparationPlanMapper liquidPreparationPlanMapper;

    @Autowired
    LiquidPreparationMeasureRecordMapper liquidPreparationMeasureRecordMapper;

    @Override
    public LiquidPreparationPlan selectById(Long id) {
        return liquidPreparationPlanMapper.selectById(id);
    }

    @Override
    public List<LiquidPreparationPlan> selectInputPlanList(Long productPlanId) {
        // 查询生产计划下的配液单
        return liquidPreparationPlanMapper.selectByPlanId(productPlanId);
    }

    @Override
    public List<LiquidPreparationMeasureRecord> selectLiquidMeasureRecordByPreparationId(Long preparationPlanId) {
        return liquidPreparationMeasureRecordMapper.selectLiquidMeasureRecordByPreparationId(preparationPlanId);
    }
}
