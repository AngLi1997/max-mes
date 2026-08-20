package com.bmos.mes.service.preparation.plan.convert;

import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatchDetailInfo;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationAvailableBoundMaterialBatchVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationBoundMaterialBatchVO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.storage.manage.vo.ReservedBatchInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface LiquidPreparationPlanConverter {

    LiquidPreparationPlanConverter INSTANCE = Mappers.getMapper(LiquidPreparationPlanConverter.class);


    default List<LiquidPreparationBoundMaterialBatchVO> convertToBatchVO(List<LiquidPreparationMaterialBatchDetailInfo> list) {
        return list.stream().map(this::convertToBatchVO).collect(Collectors.toList());
    }

    @Mapping(target = "originalBatchNo",source = "factoryBatchNo")
    @Mapping(target = "originalCode",source = "originalBatchNo")
    LiquidPreparationBoundMaterialBatchVO convertToBatchVO(LiquidPreparationMaterialBatchDetailInfo info);

    List<LiquidPreparationAvailableBoundMaterialBatchVO> convertToAvailableBatchVO(List<ReservedBatchInfo> reservedBatchInfos);

    default BusinessDataHandleBaseDTO convertToBusinessDataBaseDTO(LiquidPreparationPlan liquidPreparationPlan, ProcedureStepModel procedureStepModel){
        BusinessDataHandleBaseDTO result = convertToBusinessDataBaseDTO(liquidPreparationPlan);
        result.setProcedureStepId(procedureStepModel.getProcedureStepId());
        result.setProcessId(procedureStepModel.getProcessId());
        result.setProcessVersion(procedureStepModel.getProcessVersion());
        return result;
    }

    BusinessDataHandleBaseDTO convertToBusinessDataBaseDTO(LiquidPreparationPlan liquidPreparationPlan);

    BusinessComponentBatchSaveDTO convertToBusinessComponentBatchSaveDTO(BusinessDataHandleBaseDTO dto);
}
