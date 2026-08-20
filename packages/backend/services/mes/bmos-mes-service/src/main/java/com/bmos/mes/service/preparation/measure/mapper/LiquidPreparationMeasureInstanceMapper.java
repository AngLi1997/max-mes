package com.bmos.mes.service.preparation.measure.mapper;

import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureInstance;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LiquidPreparationMeasureInstanceMapper extends BaseMapperX<LiquidPreparationMeasureInstance> {


    default LiquidPreparationMeasureInstance selectUnique(UniqueComponentQueryDTO build) {
        return selectOne(new LambdaQueryWrapperX<LiquidPreparationMeasureInstance>()
                .eq(LiquidPreparationMeasureInstance::getProductPlanId, build.getProductPlanId())
                .eq(LiquidPreparationMeasureInstance::getComponentId, build.getComponentId())
                .eq(LiquidPreparationMeasureInstance::getReuse, build.getReuse())
                .eq(LiquidPreparationMeasureInstance::getCopyVersion, build.getCopyVersion())
                .eq(!build.getReuse(), LiquidPreparationMeasureInstance::getProcedureStepModelId, build.getProcedureStepModelId()));
    }
}
