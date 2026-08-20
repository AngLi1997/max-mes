package com.bmos.platform.service.message.sender;

import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.MaterialForeWarningMessageContext;
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
 * 物料预警通知
 *
 * @className: ForeWarningTopic
 * @author: yigaohui
 * @date: 2025/1/7 11:35
 * @Version: 1.0
 * @description:
 */

@Service
public class MaterialExpireForeWarningMessageSender extends DefaultMessageSender {

    @Autowired
    private RoleService roleService;

    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        return "";
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        MaterialForeWarningMessageContext auditMessageContext = (MaterialForeWarningMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(MaterialExpireForeWarningMessageVariables.MATERIAL_NAME, auditMessageContext.getMaterialName());
        ctx.setVariable(MaterialExpireForeWarningMessageVariables.MATERIAL_CODE, auditMessageContext.getMaterialCode());
        ctx.setVariable(MaterialExpireForeWarningMessageVariables.BATCH_NO, auditMessageContext.getBatchNo());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.MATERIAL_EXPIRE_FORE_WARNING;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        List<FeignUserVO> feignUserVOS = roleService.authUserList(MessageTypeEnum.MATERIAL_EXPIRE_FORE_WARNING.getAuthorityCode());
        return feignUserVOS.stream().map(FeignUserVO::getUserId).collect(Collectors.toSet());
    }

    static class MaterialExpireForeWarningMessageVariables {
        public static final String MATERIAL_NAME = "materialName";
        public static final String MATERIAL_CODE = "materialCode";
        public static final String BATCH_NO = "batchNo";
    }
}
