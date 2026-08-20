package com.bmos.mes.service.requisition.service;

import com.bmos.mes.service.requisition.dto.*;
import com.bmos.mes.service.requisition.model.Requisition;
import com.bmos.mes.service.requisition.vo.*;
import com.bmos.mes.service.storage.manage.dto.BatchReservedMaterialQueryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface RequisitionService {

    void handleNameAndSave(Requisition requisition);

    RequisitionPlanVO getMaterialRequisitionPlanVO(RequisitionQueryDTO dto);

    void receiveRepositoryByBatch(ReceiveRepositoryByBatchDTO dto);

    List<RequisitionListVO> getRequisitionList(Long batchId);

    void receiveRepositoryByMaterial(ReceiveRepositoryByMaterialDTO dto);

    List<RepositoryBatchMaterialListVO> getRepositoryBatchMaterialList(RepositoryBatchMaterialQueryDTO dto);

    List<RepositoryMaterialBatchListVO> getRepositoryMaterialBatch(RepositoryBatchQueryDTO dto);

    RepositoryReservedBatchVO getRepositoryReservedBatch(RepositoryReservedBatchQueryDto dto);

    void completeRequisitionPlan(RequisitionCompleteDTO dto);

    List<InventoryAvailableQuantityListVO> getRepositoryMaterialQuantityList(RepositoryQuantityQueryDTO dto);

    void cancelReservedSingle(StorageMaterialCancelReservedSingleDto dto);

    void reserveRepositoryMaterial(ReserveRepositoryMaterialDTO dto);

    void cancelRepositoryMaterial(CancelRepositoryMaterialDTO dto);

    void cancelSendOut(Long requisitionPlanId);

    void sendOut(SendOutFeignDTO dto);

    List<RequisitionMaterialBatchVO> getRequisitionMaterialBatchList(Long requisitionId);

    ComponentBoundRequisitionVO getComponentBoundRequisition(ComponentBoundRequisitionQueryDTO dto);

    void completeReceive(ReceiveRequisitionCompleteDTO dto);

    BigDecimal calculateQuantity(QuantityCalculateDTO dto);

    BatchReservedMaterialInfoVO getBatchReservedMaterialInfo(BatchReservedMaterialQueryDTO dto);

    ReserveComponentInstanceVO getReserveComponentInstanceInfo(ReserveComponentInstanceQueryDTO dto);

    void reserveStorageMaterial(ReserveComponentReserveDTO dto);

    void reserveComponentCancelReserve(ReserveComponentCancelReserveDTO dto);
}
