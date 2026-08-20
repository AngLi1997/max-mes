package com.bmos.lims2.server.audit;


import com.bmos.lims2.server.audit.dto.SaveFlowAuditMegDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditMessage;
import com.bmos.lims2.server.audit.vo.FlowAuditMegVO;

import java.util.List;

public interface FlowAuditMessageService {


    Boolean saveMegUserList(List<SaveFlowAuditMegDTO> auditMegDTOList, String deploymentId);

    List<FlowAuditMessage> findMegListByDeploymentId(String deploymentId);

    Boolean deleteByIds(List<FlowAuditMessage> megList);

    List<FlowAuditMegVO> queryListByDeploymentId(String deploymentId);

    List<FlowAuditMessage> queryListByNodId(String elementKey);

    List<String> listMakeUser(String nodeId, String deploymentId);
}
