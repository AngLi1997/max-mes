package com.bmos.platform.service.message.service;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.MessageCountDTO;
import com.bmos.platform.service.message.persistence.IMessagePersistence;
import com.bmos.platform.service.message.ws.WebSocketServer;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @className: MessageServiceImpl
 * @author: yigaohui
 * @date: 2025/2/6 10:09
 * @Version: 1.0
 * @description:
 */

@Service
public class NotifyNotifyMessageServiceImpl implements INotifyMessageService {


    @Autowired
    private IMessagePersistence messagePersistence;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void read(List<Long> ids, boolean all, List<MessageTypeEnum> messageType) {
        messagePersistence.updateReadStatus(ids, all,messageType);
        // 发送当前用户的未读数量
        Map<String, List<MessageCountDTO>> notReadCount = messagePersistence.selectNotReadCount(Lists.newArrayList(SysUserHolder.getUser().getUserId()));
        webSocketServer.sendMessage(Lists.newArrayList(SysUserHolder.getUser().getUserId()), notReadCount);
    }
}
