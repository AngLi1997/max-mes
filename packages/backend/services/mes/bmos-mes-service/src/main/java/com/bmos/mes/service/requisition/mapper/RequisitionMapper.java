package com.bmos.mes.service.requisition.mapper;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.common.enums.requisition.SendStatusEnum;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.requisition.model.Requisition;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequisitionMapper extends BaseMapperX<Requisition> {

    default boolean existsRequisitionPlan(ProcedureStepModel procedureStepModel, Long productPlanId) {
        return exists(new LambdaQueryWrapperX<Requisition>()
                .eq(Requisition::getProductPlanId, productPlanId)
                .eq(Requisition::getRecordItemId, procedureStepModel.getRecordItemId())
                .eq(Requisition::getProcedureStepModelId, BooleanUtil.isTrue(procedureStepModel.getReusable()) ? 0 : procedureStepModel.getId()));
    }


    default Requisition selectByModelAndPlanAndComponent(ProcedureStepModel procedureStepModel, Long productPlanId,
                                                         Long componentId,  Long copyVersion) {
        return selectOne(new LambdaQueryWrapperX<Requisition>()
                .eq(Requisition::getProductPlanId, productPlanId)
                .eq(Requisition::getRecordItemId, procedureStepModel.getRecordItemId())
                .eq(Requisition::getRecordVersionId, procedureStepModel.getRecordVersionId())
                .eq(Requisition::getComponentId, componentId)
                .eq(Requisition::getCopyVersion, copyVersion)
                .eq(Requisition::getProcedureStepModelId, BooleanUtil.isTrue(procedureStepModel.getReusable()) ? 0 : procedureStepModel.getId()));
    }

    default Integer selectNextSerialNo(Long productPlanId) {
        Integer max = selectMaxSerialNo(productPlanId);
        return ObjectUtil.isNull(max) ? 1 : max + 1;
    }

    Integer selectMaxSerialNo(Long productPlanId);

    default List<Requisition> selectByPlanId(Long batchId, SendStatusEnum repositorySend) {
        return selectList(new LambdaQueryWrapperX<Requisition>()
                .eq(Requisition::getProductPlanId, batchId)
                .eqIfPresent(Requisition::getSendStatus, repositorySend.getValue())
                .isNull(Requisition::getReceivedId));
    }

    default Requisition selectComponnetBoundRequisition(UniqueComponentQueryDTO dto, Long receiveComponentId){
        return selectOne(new LambdaQueryWrapperX<Requisition>()
                .eq(Requisition::getProductPlanId, dto.getProductPlanId())
                .eq(Requisition::getCopyVersion, dto.getCopyVersion())
                .eq(Requisition::getReceivedId, receiveComponentId));
    }
}
