package com.bmos.mes.service.preparation.measure.mapper;

import com.bmos.mes.common.enums.preparation.MeasureStatusEnum;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureBatch;
import com.bmos.mes.service.preparation.measure.service.vo.MeasuredBatchDetailVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LiquidPreparationMeasureBatchMapper extends BaseMapperX<LiquidPreparationMeasureBatch> {


    default LiquidPreparationMeasureBatch selectByLiquidPreparationPlanBatchId(Long planBatchId) {
        return selectOne(new LambdaQueryWrapperX<LiquidPreparationMeasureBatch>()
                .eq(LiquidPreparationMeasureBatch::getLiquidPreparationPlanBatchId, planBatchId));
    }

    default List<LiquidPreparationMeasureBatch> selectByMeasureInstanceId(Long measureInstanceId) {
        return selectList(new LambdaQueryWrapperX<LiquidPreparationMeasureBatch>()
                .eq(LiquidPreparationMeasureBatch::getMeasureInstanceId, measureInstanceId));
    }

    default LiquidPreparationMeasureBatch selectMeasuringBatch(Long id) {
        return selectOne(new LambdaQueryWrapperX<LiquidPreparationMeasureBatch>()
                .eq(LiquidPreparationMeasureBatch::getMeasureInstanceId, id)
                .eq(LiquidPreparationMeasureBatch::getMeasureStatus, MeasureStatusEnum.MEASURING));
    }

    default List<LiquidPreparationMeasureBatch> selectByPreparationPlanId(Long preparationPlanId) {
        return selectList(new LambdaQueryWrapperX<LiquidPreparationMeasureBatch>()
                .eq(LiquidPreparationMeasureBatch::getLiquidPreparationPlanId, preparationPlanId));
    }

    List<MeasuredBatchDetailVO> selectMeasureBatchListDetailByInstanceId(@Param("instanceId") Long id);
}
