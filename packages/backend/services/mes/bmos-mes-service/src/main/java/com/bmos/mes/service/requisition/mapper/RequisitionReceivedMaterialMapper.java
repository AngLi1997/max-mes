package com.bmos.mes.service.requisition.mapper;

import com.bmos.mes.service.requisition.dto.RepositoryBatchMaterialQueryDTO;
import com.bmos.mes.service.requisition.model.RequisitionReceivedMaterial;
import com.bmos.mes.service.requisition.vo.RepositoryBatchMaterialListVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequisitionReceivedMaterialMapper extends BaseMapperX<RequisitionReceivedMaterial> {

//    default List<RequisitionReceivedMaterial> selectRepositoryBatchMaterialList(RepositoryBatchMaterialQueryDTO dto){
//        return selectList(new LambdaQueryWrapperX<RequisitionReceivedMaterial>()
//                .eq(RequisitionReceivedMaterial::getRequisitionPlanId, dto.getRequisitionId())
//                .eq(RequisitionReceivedMaterial::getReceivedBatchId, dto.getReceivedBatchId()));
//    }

    List<RepositoryBatchMaterialListVO> selectRepositoryBatchMaterialList(RepositoryBatchMaterialQueryDTO dto);

    default List<RequisitionReceivedMaterial> selectByInventoryBatchIdList(List<Long> inventoryBatchIdList, boolean received){
        return selectList(new LambdaQueryWrapperX<RequisitionReceivedMaterial>()
                .in(RequisitionReceivedMaterial::getInventoryBatchId, inventoryBatchIdList)
                .isNull(!received, RequisitionReceivedMaterial::getCargoPositionId));
    }

    default boolean existsNotReceivedMaterial(Long requisitionPlanId){
        return exists(new LambdaQueryWrapperX<RequisitionReceivedMaterial>()
                .eq(RequisitionReceivedMaterial::getRequisitionPlanId, requisitionPlanId)
                .isNull(RequisitionReceivedMaterial::getCargoPositionId));
    }

    default List<RequisitionReceivedMaterial> selectByReceivedBatchIds(List<Long> idList, boolean received){
        return selectList(new LambdaQueryWrapperX<RequisitionReceivedMaterial>()
                .in(RequisitionReceivedMaterial::getReceivedBatchId, idList)
                .isNull(!received, RequisitionReceivedMaterial::getCargoPositionId));
    }
}
