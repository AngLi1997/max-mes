package com.bmos.mes.service.preparation.measure.mapper;

import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.measure.vo.MeasureResultRecordVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LiquidPreparationMeasureRecordMapper extends BaseMapperX<LiquidPreparationMeasureRecord> {


    default List<LiquidPreparationMeasureRecord> selectByMeasureBatchId(Long id, MeasureTypeEnum measureType) {
        return selectList(new LambdaQueryWrapperX<LiquidPreparationMeasureRecord>()
                .eq(LiquidPreparationMeasureRecord::getMeasureBatchId, id)
                .eqIfPresent(LiquidPreparationMeasureRecord::getMeasureType, measureType));
    }

    default List<LiquidPreparationMeasureRecord> selectByMeasureInstanceId(Long id) {
        return selectList(new LambdaQueryWrapperX<LiquidPreparationMeasureRecord>()
                .eq(LiquidPreparationMeasureRecord::getMeasureInstanceId, id));
    }

    List<MeasureResultRecordVO> selectResultVOByMeasureInstanceId(@Param("measureInstanceId") Long id);

    default List<LiquidPreparationMeasureRecord> selectLiquidMeasureRecordByPreparationId(Long preparationPlanId){
        return selectList(new LambdaQueryWrapperX<LiquidPreparationMeasureRecord>()
                .eq(LiquidPreparationMeasureRecord::getLiquidPreparationPlanId, preparationPlanId)
                .eq(LiquidPreparationMeasureRecord::getMeasureType, MeasureTypeEnum.LIQUID_MEASURE));
    }

    default LiquidPreparationMeasureRecord selectByStorageMaterialId(Long id){
        return selectOne(new LambdaQueryWrapperX<LiquidPreparationMeasureRecord>()
                .eq(LiquidPreparationMeasureRecord::getStorageMaterialId, id));
    }
}
