package com.bmos.lims2.server.audit.convert;

import com.bmos.audit.engine.core.command.CreateDeploymentCmd;
import com.bmos.audit.engine.core.command.DeployDeploymentCmd;
import com.bmos.audit.engine.core.command.StartProcessInstanceCmd;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.dto.SaveAuditDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeploymentConvert {

    DeploymentConvert INSTANCE = Mappers.getMapper(DeploymentConvert.class);

    default CreateDeploymentCmd convertToCreateDeployment(SaveAuditDTO dto) {
        CreateDeploymentCmd cmd = new CreateDeploymentCmd();
        cmd.setName(dto.getName());
        cmd.setMetaInfo(dto.getFlowAuditModel());
        cmd.setCreateBy(SysUserHolder.getUser().getUserId());
        cmd.setRemark(dto.getRemark());
        cmd.setCategory(dto.getCategoryCode());
        return cmd;
    }

    default DeployDeploymentCmd convertToDeployCmd(SaveAuditDTO dto) {
        DeployDeploymentCmd cmd = new DeployDeploymentCmd();
        cmd.setDeploymentId(dto.getDeploymentId());
        cmd.setDeployBy(SysUserHolder.getUser().getUserId());
        cmd.setMetaInfo(dto.getFlowAuditModel());
        return cmd;
    }

    default StartProcessInstanceCmd convertToStartCmd(String deploymentId, FlowStartDTO dto) {
        StartProcessInstanceCmd processInstanceCmd = new StartProcessInstanceCmd();
        processInstanceCmd.setDeploymentId(deploymentId);
        processInstanceCmd.setProcessName(dto.getName());
        processInstanceCmd.setBusinessKey(dto.getBusinessKey());
        processInstanceCmd.setStartBy(SysUserHolder.getUser().getUserId());
        processInstanceCmd.setExtField(dto.getExtField());
        return processInstanceCmd;
    }
}
