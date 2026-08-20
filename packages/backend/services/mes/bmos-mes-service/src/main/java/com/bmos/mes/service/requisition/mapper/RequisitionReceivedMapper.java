package com.bmos.mes.service.requisition.mapper;

import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.requisition.model.RequisitionReceived;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequisitionReceivedMapper extends BaseMapperX<RequisitionReceived> {

    default boolean existsBoundRequisition(Long id){
        return exists(new LambdaQueryWrapperX<RequisitionReceived>().eq(RequisitionReceived::getRequisitionId, id));
    }

    default RequisitionReceived queryUniqueComponent(UniqueComponentQueryDTO build){
        return selectOne(new LambdaQueryWrapperX<RequisitionReceived>()
                .eq(RequisitionReceived::getComponentId, build.getComponentId())
                .eq(RequisitionReceived::getProductPlanId, build.getProductPlanId())
                .eq(RequisitionReceived::getCopyVersion, build.getCopyVersion())
                .eq(!build.getReuse(), RequisitionReceived::getProcedureStepModelId, build.getProcedureStepModelId())
                .eq(RequisitionReceived::getReuse, build.getReuse())
                .eq(RequisitionReceived::getRecordVersionId, build.getRecordVersionId())
                .eq(RequisitionReceived::getRecordItemId, build.getRecordItemId()));
    }

    default RequisitionReceived selectByRequisitionId(Long requisitionId){
        return selectOne(new LambdaQueryWrapperX<RequisitionReceived>().eq(RequisitionReceived::getRequisitionId, requisitionId));
    }
}
