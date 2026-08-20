package com.bmos.mes.service.preparation.input.mapper;


import com.bmos.mes.service.preparation.input.model.PreparationInputComponentInstance;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * (BmPreparationInputComponentInstance)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-01 12:55:24
 */
@Mapper
public interface PreparationInputComponentInstanceMapper extends BaseMapperX<PreparationInputComponentInstance> {


    /**
     * 根据组件id、复制版本、工序步骤模型id、生产指令单id查询组件实例
     * @param planId 生产计划id
     * @param componentId 组件id
     * @param copyVersion 复制版本
     * @param procedureStepModelId 工艺步骤模型id
     * @param reuse 是否复用
     * @return
     */
    default PreparationInputComponentInstance selectByComponentInfo(Long planId, Long componentId, Long copyVersion, Long procedureStepModelId, Boolean reuse){
        return selectOne(new LambdaQueryWrapperX<PreparationInputComponentInstance>()
                .eq(PreparationInputComponentInstance::getProductPlanId, planId)
                .eq(PreparationInputComponentInstance::getComponentId, componentId)
                .eq(PreparationInputComponentInstance::getCopyVersion, copyVersion)
                .eq(PreparationInputComponentInstance::getProcedureStepModelId, procedureStepModelId)
                .eq(PreparationInputComponentInstance::getReuse, reuse));
    }
}

