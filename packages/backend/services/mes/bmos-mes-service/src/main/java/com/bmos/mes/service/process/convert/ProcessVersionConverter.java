package com.bmos.mes.service.process.convert;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.mes.common.enums.StateEnum;
import com.bmos.mes.service.workflow.enums.WorkflowType;
import com.bmos.mes.service.process.dto.save.ProcessSaveDTO;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProcessVersionConverter {
    ProcessVersionConverter INSTANCE = Mappers.getMapper(ProcessVersionConverter.class);


    default ProcessVersion build(Long processId, ProcessSaveDTO dto) {
        return ProcessVersion.builder()
                .processId(processId)
                .productFormulaVersionId(dto.getProductFormulaVersionId())
                .version(dto.getVersion())
                .state(StateEnum.OFF.getValue())
                .description(dto.getDescription())
                .actionState(ActionStateEnum.EDIT.getValue())
                .productionStageCode(dto.getProductionStageCode())
                .build();
    }

    default CreateDeploymentCmd convertCmd(String name, String processModel, String version) {
        CreateDeploymentCmd cmd = new CreateDeploymentCmd();
        cmd.setName(name + version);
        cmd.setCategory(WorkflowType.PROCEDURE.name());
        cmd.setMetaInfo(processModel);
        cmd.setCreateBy("1");
        return cmd;
    }
}
