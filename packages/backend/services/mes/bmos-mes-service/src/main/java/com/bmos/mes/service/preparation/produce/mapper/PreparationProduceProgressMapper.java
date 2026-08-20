package com.bmos.mes.service.preparation.produce.mapper;

import com.bmos.mes.service.preparation.produce.model.PreparationProduceProgress;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 配液产出称量流程表(BmPreparationProduceProgress)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-01 12:57:52
 */
@Mapper
public interface PreparationProduceProgressMapper extends BaseMapperX<PreparationProduceProgress> {


    /**
     * 查询当前配液产出组件的产出流程
     * @param componentId
     * @param procedureStepModelId
     * @param copyVersion
     * @param reuse
     * @return
     */
    default PreparationProduceProgress selectByComponentInfo(Long planId, Long componentId, Long procedureStepModelId, Long copyVersion, Boolean reuse){
        return selectOne(new LambdaQueryWrapperX<PreparationProduceProgress>()
                .eq(PreparationProduceProgress::getProductPlanId, planId)
                .eq(PreparationProduceProgress::getComponentId, componentId)
                .eq(PreparationProduceProgress::getProcedureStepModelId, procedureStepModelId)
                .eq(PreparationProduceProgress::getCopyVersion, copyVersion)
                .eq(PreparationProduceProgress::getReuse, reuse));
    }
}

