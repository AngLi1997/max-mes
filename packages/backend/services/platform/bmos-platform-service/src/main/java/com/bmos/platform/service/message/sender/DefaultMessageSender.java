package com.bmos.platform.service.message.sender;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.MessageContextDTO;
import com.bmos.platform.service.message.dto.MessageCountDTO;
import com.bmos.platform.service.message.dto.MessageDTO;
import com.bmos.platform.service.message.entity.MessageTemplate;
import com.bmos.platform.service.message.mapper.MessageTemplateMapper;
import com.bmos.platform.service.message.persistence.IMessagePersistence;
import com.bmos.platform.service.message.ws.WebSocketServer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class DefaultMessageSender implements IMessageSender {


    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private MessageTemplateMapper templateMapper;

    @Autowired
    private IMessagePersistence messagePersistence;

    @Override
    public void send(MessageContextDTO messageContextDTO) {
        Collection<String> receiveUserIds = getReceiveUserIds(messageContextDTO);
        if (CollectionUtil.isEmpty(receiveUserIds)) {
            log.info("消息订阅人为空");
            return;
        }
        MessageDTO message = this.generateMessage(messageContextDTO);
        if (message == null) {
            log.error("消息为null，不进行发送");
            return;
        }
        // 持久化
        messagePersistence.saveMessage(SysUserHolder.getUser().getUserId() != null ? SysUserHolder.getUser().getUserId() : "system", getMessageType(), receiveUserIds, message);
        webSocketServer.sendMessage(receiveUserIds, message);
        // 将数量通过长链接发送
        Map<String, List<MessageCountDTO>> messageCount = this.getMessageCount(receiveUserIds);
        if (CollectionUtil.isNotEmpty(messageCount)) {
            messageCount.forEach((k, v) ->
                    webSocketServer.sendMessage(Lists.newArrayList(k), v));
        }
    }

    protected Map<String, List<MessageCountDTO>> getMessageCount(Collection<String> receiveUserIds) {
        return messagePersistence.selectNotReadCount(receiveUserIds);
    }

    protected MessageDTO generateMessage(MessageContextDTO messageContextDTO) {
        MessageTemplate messageTemplate = this.getMessageTemplate();
        if (messageTemplate == null) {
            return null;
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTitle(getTitle(messageTemplate, messageContextDTO));
        messageDTO.setContent(getContent(messageTemplate, messageContextDTO));
        messageDTO.setTime(messageContextDTO.getTime());
        return messageDTO;
    }

    protected abstract String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO);

    protected abstract String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO);

    protected abstract MessageTypeEnum getMessageType();

    protected abstract Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO);

    protected MessageTemplate getMessageTemplate() {
        LambdaQueryWrapper<MessageTemplate> ql = new QueryWrapper<MessageTemplate>().lambda();
        ql.eq(MessageTemplate::getMessageType, this.getMessageType());
        MessageTemplate messageTemplate = templateMapper.selectOne(ql);
        messageTemplate.setTitleTemplate(StrUtil.isEmpty(messageTemplate.getTitleTemplate()) ? null :
                reEscape(I18nUtils.getCodeMessage(getMessageType().getI18nCode() + ".TITLE", escape(messageTemplate.getTitleTemplate()), null)));
        messageTemplate.setContentTemplate(StrUtil.isEmpty(messageTemplate.getContentTemplate()) ? null :
                reEscape(I18nUtils.getCodeMessage(getMessageType().getI18nCode() + ".CONTENT", escape(messageTemplate.getContentTemplate()), null)));
        return messageTemplate;
    }

    protected String escape(String str) {
        return str.replaceAll("\\{", "'{").replaceAll("}", "}'");
    }

    protected String reEscape(String str) {
        return str.replaceAll("'\\{", "{").replaceAll("}'", "}");
    }
}
