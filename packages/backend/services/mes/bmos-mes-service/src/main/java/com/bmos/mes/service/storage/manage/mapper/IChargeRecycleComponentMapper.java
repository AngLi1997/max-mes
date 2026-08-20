package com.bmos.mes.service.storage.manage.mapper;

import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.storage.manage.model.ChargeRecycleComponent;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IChargeRecycleComponentMapper extends BaseMapperX<ChargeRecycleComponent> {

    default ChargeRecycleComponent selectUnique(UniqueComponentQueryDTO dto){
        return selectOne(new LambdaQueryWrapperX<ChargeRecycleComponent>()
                .eq(ChargeRecycleComponent::getProductPlanId, dto.getProductPlanId())
                .eq(ChargeRecycleComponent::getComponentId,dto.getComponentId())
                .eq(ChargeRecycleComponent::getCopyVersion, dto.getCopyVersion())
                .eq(ChargeRecycleComponent::getRecordItemId, dto.getRecordItemId())
                .eq(ChargeRecycleComponent::getRecordVersionId, dto.getRecordVersionId())
                .eq(ChargeRecycleComponent::getReuse, dto.getReuse())
                .eq(!dto.getReuse(), ChargeRecycleComponent::getProcedureStepModelId, dto.getProcedureStepModelId()));
    }

    default List<ChargeRecycleComponent> selectListByProductPlanId(Long productPlanId){
        return selectList(new LambdaQueryWrapperX<ChargeRecycleComponent>()
                .eq(ChargeRecycleComponent::getProductPlanId, productPlanId));
    }
}
