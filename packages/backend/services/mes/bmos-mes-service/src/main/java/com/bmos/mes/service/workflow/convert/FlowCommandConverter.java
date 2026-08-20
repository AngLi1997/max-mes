package com.bmos.mes.service.workflow.convert;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.workflow.dto.StartWorkflowDTO;
import com.bmos.orchestrator.engine.core.command.StartProcessInstanceCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlowCommandConverter {
    FlowCommandConverter INSTANCE = Mappers.getMapper(FlowCommandConverter.class);


    default StartProcessInstanceCmd convertStartCmd(StartWorkflowDTO dto, ProcessVersion processVersion){
        StartProcessInstanceCmd cmd = new StartProcessInstanceCmd();
        cmd.setDeploymentId(processVersion.getProcessModelId());
        cmd.setBusinessKey(String.valueOf(dto.getProductPlanId()));
        cmd.setStartBy("1");
        return cmd;
    }
}
