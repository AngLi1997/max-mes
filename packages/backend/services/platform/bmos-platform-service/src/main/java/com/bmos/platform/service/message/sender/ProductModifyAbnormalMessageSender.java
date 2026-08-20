package com.bmos.platform.service.message.sender;

import cn.hutool.core.util.StrUtil;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.MessageContextDTO;
import com.bmos.platform.service.message.dto.ProductModifyAbnormalMessageContext;
import com.bmos.platform.service.message.entity.MessageTemplate;
import com.bmos.platform.service.system.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 生产修订异常通知
 *
 * @className: ProductModifyAbnormalNotify
 * @author: yigaohui
 * @date: 2025/1/8 11:12
 * @Version: 1.0
 * @description:
 */

@Service
public class ProductModifyAbnormalMessageSender extends DefaultMessageSender {
    @Autowired
    private RoleService roleService;

    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        ProductModifyAbnormalMessageContext auditMessageContext = (ProductModifyAbnormalMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(ProductModifyAbnormalMessageVariables.BATCH_NO, auditMessageContext.getBatchNo());
        String join = Stream.of(auditMessageContext.getProcessName(), auditMessageContext.getProcedureName(),
                auditMessageContext.getProcedureStepName()).filter(StrUtil::isNotEmpty).collect(Collectors.joining("-"));
        ctx.setVariable(ProductModifyAbnormalMessageVariables.ABNORMAL_NODE, join);
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getContentTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        ProductModifyAbnormalMessageContext auditMessageContext = (ProductModifyAbnormalMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(ProductModifyAbnormalMessageVariables.ABNORMAL_DESCRIPTION, auditMessageContext.getAbnormalDescription());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.PRODUCT_MODIFY_ABNORMAL_WARNING;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        List<FeignUserVO> feignUserVOS = roleService.authUserList(MessageTypeEnum.PRODUCT_MODIFY_ABNORMAL_WARNING.getAuthorityCode());
        return feignUserVOS.stream().map(FeignUserVO::getUserId).collect(Collectors.toSet());
    }


    static class ProductModifyAbnormalMessageVariables {
        public static final String BATCH_NO = "batchNo";
        public static final String ABNORMAL_NODE = "abnormalNode";
        public static final String ABNORMAL_DESCRIPTION = "abnormalDescription";
    }
}
