package com.bmos.mes.service.ingredient.input.mapper;

import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.ingredient.input.model.IngredientInputComponentInstance;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/26 10:20
 */
@Mapper
public interface IIngredientInputComponentInstanceMapper extends BaseMapperX<IngredientInputComponentInstance> {


    default IngredientInputComponentInstance selectUnique(UniqueComponentQueryDTO build) {
        return selectOne(new LambdaQueryWrapperX<IngredientInputComponentInstance>()
                .eq(IngredientInputComponentInstance::getProductPlanId, build.getProductPlanId())
                .eq(IngredientInputComponentInstance::getComponentId, build.getComponentId())
                .eq(IngredientInputComponentInstance::getCopyVersion, build.getCopyVersion())
                .eq(IngredientInputComponentInstance::getRecordItemId, build.getRecordItemId())
                .eq(IngredientInputComponentInstance::getRecordVersionId, build.getRecordVersionId())
                .eq(IngredientInputComponentInstance::getReuse, build.getReuse())
                .eq(!build.getReuse(), IngredientInputComponentInstance::getProcedureStepModelId,
                        build.getProcedureStepModelId()));
    }
}
