package com.bmos.mes.service.requisition.mapper;

import com.bmos.mes.service.requisition.dto.RepositoryBatchMaterialQueryDTO;
import com.bmos.mes.service.requisition.model.RequisitionReceivedBatch;
import com.bmos.mes.service.requisition.vo.RequisitionMaterialBatchVO;
import com.bmos.mes.service.requisition.vo.RequisitionReceivedBatchInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RequisitionReceivedBatchMapper extends BaseMapperX<RequisitionReceivedBatch> {

    List<RequisitionMaterialBatchVO> selectByRequisitionId(@Param("requisitionPlanId") Long requisitionPlanId);

    List<RequisitionReceivedBatchInfo> selectReceivedBatchInfo(@Param("requisitionPlanId") Long requisitionPlanId);

    default RequisitionReceivedBatch selectBatch(RepositoryBatchMaterialQueryDTO dto){
        return selectOne(new LambdaQueryWrapperX<RequisitionReceivedBatch>()
                .eq(RequisitionReceivedBatch::getRequisitionPlanId, dto.getRequisitionId())
                .eq(RequisitionReceivedBatch::getInventoryBatchId, dto.getReceivedBatchId()));
    }
}
