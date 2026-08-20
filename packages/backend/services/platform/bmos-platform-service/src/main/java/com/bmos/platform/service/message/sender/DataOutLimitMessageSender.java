package com.bmos.platform.service.message.sender;

import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.DataOutLimitMessageContext;
import com.bmos.platform.service.message.dto.MessageContextDTO;
import com.bmos.platform.service.message.entity.MessageTemplate;
import com.bmos.platform.service.system.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据超限异常通知
 *
 * @className: WarningMessageTopic
 * @author: yigaohui
 * @date: 2025/1/7 11:25
 * @Version: 1.0
 * @description:
 */

@Service
public class DataOutLimitMessageSender extends DefaultMessageSender {

    @Autowired
    private RoleService roleService;


    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        DataOutLimitMessageContext auditMessageContext = (DataOutLimitMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(DataOutLimitMessageVariables.BATCH_NO, auditMessageContext.getBatchNo());
        ctx.setVariable(DataOutLimitMessageVariables.PROCESS_NAME, auditMessageContext.getProcessName());
        ctx.setVariable(DataOutLimitMessageVariables.PROCEDURE_NAME, auditMessageContext.getProcedureStepName());
        ctx.setVariable(DataOutLimitMessageVariables.PROCEDURE_STEP_NAME, auditMessageContext.getProcedureStepName());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        DataOutLimitMessageContext auditMessageContext = (DataOutLimitMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(DataOutLimitMessageVariables.ABNORMAL_DESCRIPTION, auditMessageContext.getAbnormalDescription());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.DATA_OUT_LIMIT_WARNING;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        List<FeignUserVO> feignUserVOS = roleService.authUserList(MessageTypeEnum.DATA_OUT_LIMIT_WARNING.getAuthorityCode());
        return feignUserVOS.stream().map(FeignUserVO::getUserId).collect(Collectors.toSet());
    }

    static class DataOutLimitMessageVariables {
        public static final String BATCH_NO = "batchNo";
        public static final String PROCESS_NAME = "processName";
        public static final String PROCEDURE_NAME = "procedureName";
        public static final String PROCEDURE_STEP_NAME = "procedureStepName";
        public static final String ABNORMAL_DESCRIPTION = "abnormalDescription";
    }
}
