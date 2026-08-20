package com.bmos.mes.service.audit.service;

import com.bmos.mes.service.audit.dto.SaveFlowAuditMegDTO;
import com.bmos.mes.service.audit.model.FlowAuditMessage;
import com.bmos.mes.service.audit.vo.FlowAuditMegVO;

import java.util.List;

public interface FlowAuditMessageService {


    Boolean saveMegUserList(List<SaveFlowAuditMegDTO> auditMegDTOList, String deploymentId);

    List<FlowAuditMessage> findMegListByDeploymentId(String deploymentId);

    Boolean deleteByIds(List<FlowAuditMessage> megList);

    List<FlowAuditMegVO> queryListByDeploymentId(String deploymentId);

    List<FlowAuditMessage> queryListByNodId(String elementKey);

    List<String> listMakeUser(String nodeId, String deploymentId);
}
