package com.bmos.mes.service.audit.service;

import com.bmos.mes.service.audit.dto.SaveFlowAuditUserDTO;
import com.bmos.mes.service.audit.model.FlowAuditUser;
import com.bmos.mes.service.audit.vo.FlowAuditUserVO;

import java.util.Collection;
import java.util.List;

public interface FlowAuditUserService {


    Boolean saveFlowAuditUserList(List<SaveFlowAuditUserDTO> auditUserList, String deploymentId);

    List<FlowAuditUser> findListByDeploymentId(String deploymentId);

    Boolean deleteByIds(List<FlowAuditUser> userList);

    List<FlowAuditUserVO> queryListByDeploymentId(String deploymentId);

    List<FlowAuditUser> findListByKeyAndDeploymentId(String key,String deploymentId);

    List<String> selectUserIdListByNodeIdAndDeploymentId(String deploymentId, String nodeId);
}
