package com.bmos.mes.service.requisition.mapper;

import com.bmos.mes.service.requisition.model.RequisitionMaterialReserved;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequisitionMaterialBatchMapper extends BaseMapperX<RequisitionMaterialReserved> {

    default List<RequisitionMaterialReserved> getByRequisitionAndMaterial(Long requisitionPlanId, Long formulaMaterialId){
        return selectList(new LambdaQueryWrapperX<RequisitionMaterialReserved>()
                .eq(RequisitionMaterialReserved::getRequisitionPlanId, requisitionPlanId)
                .eq(RequisitionMaterialReserved::getFormulaMaterialId, formulaMaterialId));
    }

    default void cancelReservedMaterial(Long requisitionPlanId, Long formulaMaterialId){
        delete(new LambdaQueryWrapperX<RequisitionMaterialReserved>()
                .eq(RequisitionMaterialReserved::getRequisitionPlanId, requisitionPlanId)
                .eq(RequisitionMaterialReserved::getFormulaMaterialId, formulaMaterialId));
    }

    default List<RequisitionMaterialReserved> getByRequisitionId(Long id){
        return selectList(new LambdaQueryWrapperX<RequisitionMaterialReserved>()
                .eq(RequisitionMaterialReserved::getRequisitionPlanId, id));
    }
}
