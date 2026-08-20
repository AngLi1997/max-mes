package com.bmos.platform.service.message.sender;

import cn.hutool.core.util.BooleanUtil;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.AuditMessageContext;
import com.bmos.platform.service.message.dto.MessageContextDTO;
import com.bmos.platform.service.message.entity.MessageTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 审批通知
 *
 * @className: AuditMessageTopic
 * @author: yigaohui
 * @date: 2025/1/7 11:19
 * @Version: 1.0
 * @description:
 */

@Service
public class AuditMessageSender extends DefaultMessageSender {


    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        AuditMessageContext auditMessageContext = (AuditMessageContext) messageContextDTO;
        if(BooleanUtil.isTrue(auditMessageContext.getIsStart())){
            return "";
        }
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(AuditMessageVariables.AUDIT_CONTENT, auditMessageContext.getAuditContent());
        ctx.setVariable(AuditMessageVariables.AUDIT_REMARK, auditMessageContext.getRemark());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getContentTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        AuditMessageContext auditMessageContext = (AuditMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(AuditMessageVariables.APPLY_USER_NAME, auditMessageContext.getAuditUser());
        ctx.setVariable(AuditMessageVariables.BUSINESS_TEXT, auditMessageContext.getBusinessText());
        ctx.setVariable(AuditMessageVariables.auditNodeName, auditMessageContext.getNodeName());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.AUDIT;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        return messageContextDTO.getNotifyUserIds();
    }


    static class AuditMessageVariables {

        private static final String APPLY_USER_NAME = "applyUserName";

        private static final String BUSINESS_TEXT = "businessText";

        private static final String AUDIT_CONTENT = "auditContent";

        private static final String AUDIT_REMARK = "auditRemark";

        private static final String auditNodeName = "auditNodeName";
    }
}
