package com.bmos.mes.service.process.convert;

import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.dto.save.ProcedureConfirmSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessConfirmSaveDTO;
import com.bmos.mes.service.process.model.ProcedureConfirm;
import com.bmos.mes.service.process.model.ProcessConfirm;
import com.bmos.mes.service.process.vo.ProcedureConfirmVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface ProcessConfirmConverter {
    ProcessConfirmConverter INSTANCE = Mappers.getMapper(ProcessConfirmConverter.class);

    ProcessConfirm convertToConfirm(ProcessConfirmSaveDTO dto);

    default ProcessConfirmSaveDTO convertToDto(Plan plan) {
        ProcessConfirmSaveDTO saveDto = new ProcessConfirmSaveDTO();
        saveDto.setInstanceId(plan.getExecuteProcessInstanceId());
        saveDto.setPlanBatchNo(plan.getBatchNo());
        saveDto.setProductId(plan.getProductId());
        saveDto.setProductCode(plan.getProductMergeCode());
        saveDto.setProductSpecification(plan.getProductSpecification());
        saveDto.setProcessId(plan.getProcessId());
        saveDto.setProcessName(plan.getProcessName());
        saveDto.setStartTime(LocalDateTime.now());
        saveDto.setProductName(plan.getProductName());
        return saveDto;
    }

    ProcedureConfirm convertToProcedureConfirm(ProcedureConfirmSaveDTO saveDTO);

    default ProcedureConfirmSaveDTO convertToProcedureDto(ProcessConfirm confirm,String procedureName) {
        ProcedureConfirmSaveDTO dto = new ProcedureConfirmSaveDTO();
        dto.setProcessId(confirm.getProcessId());
        dto.setProcessConfirmId(confirm.getId());
        dto.setProcedureTime(LocalDateTime.now());
        dto.setProcedureName(procedureName);
        return dto;
    }

    List<ProcedureConfirmVO> convertToProcedureVo(List<ProcedureConfirm> list);
}
