package com.bmos.platform.service.message.sender;

import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.BsmsPlasmaInventoryWarningMessageContext;
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
import java.util.stream.Collectors;

@Service
public class BsmsPlasmaInventoryWarningMessageSender extends DefaultMessageSender{

    private static final String INCR_NUM ="incrNum";
    private static final String ALL_NUM ="allNum";

    @Autowired
    private RoleService roleService;

    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        return "";
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        BsmsPlasmaInventoryWarningMessageContext context = (BsmsPlasmaInventoryWarningMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(INCR_NUM, context.getIncrNum());
        ctx.setVariable(ALL_NUM, context.getAllNum());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.BSMS_PLASMA_INVENTORY_WARNING;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        return roleService.authUserList(MessageTypeEnum.BSMS_PLASMA_INVENTORY_WARNING.getAuthorityCode())
                .stream().map(FeignUserVO::getUserId).collect(Collectors.toSet());
    }
}
