package com.bmos.lims2.server.audit;


import com.bmos.lims2.server.audit.dto.SaveFlowAuditUserDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditUser;
import com.bmos.lims2.server.audit.vo.FlowAuditUserVO;

import java.util.List;

public interface FlowAuditUserService {


    Boolean saveFlowAuditUserList(List<SaveFlowAuditUserDTO> auditUserList, String deploymentId);

    List<FlowAuditUser> findListByDeploymentId(String deploymentId);

    Boolean deleteByIds(List<FlowAuditUser> userList);

    List<FlowAuditUserVO> queryListByDeploymentId(String deploymentId);

    List<FlowAuditUser> findListByKeyAndDeploymentId(String key,String deploymentId);

    List<String> selectUserIdListByNodeIdAndDeploymentId(String deploymentId, String nodeId);
}
