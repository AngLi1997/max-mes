package com.bmos.mes.service.audit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.audit.FlowAuditCodeEnum;
import com.bmos.mes.service.audit.convert.FlowAuditMessageConvert;
import com.bmos.mes.service.audit.dto.SaveFlowAuditMegDTO;
import com.bmos.mes.service.audit.mapper.FlowAuditMessageMapper;
import com.bmos.mes.service.audit.model.FlowAuditMessage;
import com.bmos.mes.service.audit.service.FlowAuditMessageService;
import com.bmos.mes.service.audit.vo.FlowAuditMegVO;
import com.bmos.mes.service.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class FlowAuditMessageServiceImpl implements FlowAuditMessageService {

    @Autowired
    private FlowAuditMessageMapper messageMapper;

    @Override
    public Boolean saveMegUserList(List<SaveFlowAuditMegDTO> auditMegDTOList, String deploymentId) {
        if (CollUtil.isNotEmpty(auditMegDTOList)) {
            List<String> nodeIdList = CollectionUtils.convertList(auditMegDTOList, SaveFlowAuditMegDTO::getNodeId);
            messageMapper.deleteByNodeIdListAndDeploymentId(nodeIdList, deploymentId);
            auditMegDTOList.forEach(item -> item.setDeploymentId(deploymentId));
            List<FlowAuditMessage> list = FlowAuditMessageConvert.INSTANCE.convertToUserList(auditMegDTOList);
            return messageMapper.saveMegUserList(list);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<FlowAuditMessage> findMegListByDeploymentId(String deploymentId) {
        return messageMapper.findMegListByDeploymentId(deploymentId);
    }

    @Override
    public Boolean deleteByIds(List<FlowAuditMessage> megList) {
        if (CollUtil.isEmpty(megList)){
            return Boolean.TRUE;
        }
        List<Long> ids = CollectionUtils.convertList(megList, FlowAuditMessage::getId);
        return messageMapper.deleteByIds(ids);
    }

    @Override
    public List<FlowAuditMegVO> queryListByDeploymentId(String deploymentId) {
        List<FlowAuditMessage> list = findMegListByDeploymentId(deploymentId);
        if (CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return FlowAuditMessageConvert.INSTANCE.convertToUserListVo(list);
    }

    @Override
    public List<FlowAuditMessage> queryListByNodId(String elementKey) {
        return messageMapper.queryListByNodId(elementKey);
    }

    @Override
    public List<String> listMakeUser(String nodeId, String deploymentId) {
        List<FlowAuditMessage> messageUserList = messageMapper.listMakeUser(nodeId, deploymentId, FlowAuditCodeEnum.MAKE.getValue());
        if (CollUtil.isEmpty(messageUserList)){
            return Collections.emptyList();
        }
        return messageUserList.stream()
                .map(item -> UserUtils.getUsername(item.getUserId()))
                .collect(Collectors.toList());
    }
}
