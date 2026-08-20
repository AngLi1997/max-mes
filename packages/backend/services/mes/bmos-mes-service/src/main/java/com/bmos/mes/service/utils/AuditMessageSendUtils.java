package com.bmos.mes.service.utils;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.audit.builder.AuditDataConditionBuilder;
import com.bmos.mes.service.audit.dto.SendMessageDTO;
import com.bmos.mes.service.audit.vo.AuditMessageVO;
import com.bmos.platform.facade.notify.MessageNotifyFeign;
import com.bmos.platform.facade.notify.dto.AuditMessage;
import org.springframework.core.task.SyncTaskExecutor;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * @author renjinguang
 */
public class AuditMessageSendUtils {

    private static MessageNotifyFeign messageNotifyFeign;


    public static void init(MessageNotifyFeign messageNotifyFeign) {
        AuditMessageSendUtils.messageNotifyFeign = messageNotifyFeign;
    }

    public static void sendMessage(SendMessageDTO dto) {
        CompletableFuture.runAsync(() -> {
            AuditMessageVO messageVO = AuditDataConditionBuilder.build(dto.getAuditCategoryCode())
                    .getReceiveMessageUserId(dto.getDeploymentId(), dto.getNodeId(), dto.getBusinessId());
            AuditMessage message = new AuditMessage();
            message.setAuditUser(UserUtils.getUsername(SysUserHolder.getUser().getUserId()));
            message.setAuditContent(dto.getComment());
            message.setBusinessText(messageVO.getBusinessText());
            message.setNodeName(dto.getNodeName());
            message.setRemark(dto.getRemark());
            message.setNotifyUserIds(messageVO.getUserIdList());
            message.setTime(LocalDateTime.now());
            message.setIsStart(dto.getIsStart());
            messageNotifyFeign.auditMessage(message);
        }, new SyncTaskExecutor());

    }
}
