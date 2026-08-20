package com.bmos.mes.service.requisition.convert;

import com.bmos.mes.service.requisition.dto.ReceiveRepositoryByBatchDTO;
import com.bmos.mes.service.requisition.dto.ReceiveRepositoryByMaterialDTO;
import com.bmos.mes.service.requisition.dto.SendOutFeignDTO;
import com.bmos.mes.service.requisition.model.RequisitionMaterialReserved;
import com.bmos.mes.service.requisition.model.RequisitionReceived;
import com.bmos.mes.service.requisition.model.RequisitionReceivedBatch;
import com.bmos.mes.service.requisition.model.RequisitionReceivedMaterial;
import com.bmos.mes.service.requisition.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RequisitionPlanConverter {

    RequisitionPlanConverter INSTANCE = Mappers.getMapper(RequisitionPlanConverter.class);
    default List<RepositoryMaterialBatchListVO> convertToRepositoryMaterialBatchList(List<InventoryBatchListVO> list){
        return list.stream().map(this::convertToRepositoryMaterialBatch).collect(Collectors.toList());
    }

    @Mapping(target = "originBatchNo",source = "factoryBatchNo")
    RepositoryMaterialBatchListVO convertToRepositoryMaterialBatch(InventoryBatchListVO e);

    List<RepositoryMaterialReservedBatchListVO> convertToRepositoryMaterialReservedBatch(List<RequisitionMaterialReserved> list);

    RequisitionReceivedBatch convertToRequisitionReceivedBatch(SendOutFeignDTO.SendOutBatch sendOutBatch);

    RequisitionReceivedMaterial convertToRequisitionReceivedMaterial(SendOutFeignDTO.SendOutInventory inventory);

    List<RequisitionMaterialBatchVO> convertToRequisitionMaterialBatchVO(List<RequisitionReceivedBatch> list);

    default List<RepositoryBatchMaterialListVO> convertToRepositoryBatchMaterialList(List<RequisitionReceivedMaterial> list){
        return list.stream().map(this::convertToRepositoryBatchMaterial).collect(Collectors.toList());
    }

    @Mapping(source = "inventoryNo", target = "materialNo")
    RepositoryBatchMaterialListVO convertToRepositoryBatchMaterial(RequisitionReceivedMaterial e);

    RequisitionReceived convertToRequisitionReceived(ReceiveRepositoryByBatchDTO dto);

    RequisitionReceived convertToRequisitionReceived(ReceiveRepositoryByMaterialDTO dto);
}
