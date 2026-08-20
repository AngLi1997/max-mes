package com.bmos.platform.service.message.sender;

import cn.hutool.core.util.StrUtil;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.DataOutLimitMessageContext;
import com.bmos.platform.service.message.dto.MessageContextDTO;
import com.bmos.platform.service.message.entity.MessageTemplate;
import com.bmos.platform.service.system.role.service.RoleService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据超限异常通知
 */
@Service
public class DataOverLimitSender extends DefaultMessageSender{

    @Resource
    private RoleService roleService;

    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        DataOutLimitMessageContext dataOverLimitMessageContext = (DataOutLimitMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(DataOverLimitMessageVariables.BATCH_NO, dataOverLimitMessageContext.getBatchNo());
        String join = Stream.of(dataOverLimitMessageContext.getProcessName(), dataOverLimitMessageContext.getProcedureName(),
                dataOverLimitMessageContext.getProcedureStepName()).filter(StrUtil::isNotEmpty).collect(Collectors.joining("-"));
        ctx.setVariable(DataOverLimitSender.DataOverLimitMessageVariables.ABNORMAL_NODE, join);
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getContentTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        DataOutLimitMessageContext dataOutLimitMessageContext = (DataOutLimitMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(DataOverLimitSender.DataOverLimitMessageVariables.ABNORMAL_DESCRIPTION, dataOutLimitMessageContext.getAbnormalDescription());
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

    static class DataOverLimitMessageVariables {
        public static final String BATCH_NO = "batchNo";
        public static final String ABNORMAL_NODE = "abnormalNode";
        public static final String ABNORMAL_DESCRIPTION = "abnormalDescription";
    }
}
