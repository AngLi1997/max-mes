package com.bmos.platform.service.message.sender;

import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.EquipmentFaultMessageContext;
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
 * 设备故障通知
 *
 * @className: EquipmentDefaultNotify
 * @author: yigaohui
 * @date: 2025/1/8 11:12
 * @Version: 1.0
 * @description:
 */

@Service
public class EquipmentFaultMessageSender extends DefaultMessageSender {
    @Autowired
    private RoleService roleService;

    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        return "";
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        EquipmentFaultMessageContext auditMessageContext = (EquipmentFaultMessageContext) messageContextDTO;
        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(EquipmentFaultMessageVariables.EQUIPMENT_CODE, auditMessageContext.getEquipmentCode());
        ctx.setVariable(EquipmentFaultMessageVariables.EQUIPMENT_NAME, auditMessageContext.getEquipmentName());
        ExpressionParser ep = new SpelExpressionParser();
        return ep.parseExpression(messageTemplate.getTitleTemplate(), new TemplateParserContext()).getValue(ctx).toString();
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.EQUIPMENT_DEFAULT_WARNING;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        List<FeignUserVO> feignUserVOS = roleService.authUserList(MessageTypeEnum.EQUIPMENT_DEFAULT_WARNING.getAuthorityCode());
        return feignUserVOS.stream().map(FeignUserVO::getUserId).collect(Collectors.toSet());
    }

    static class EquipmentFaultMessageVariables {
        public static final String EQUIPMENT_NAME = "equipmentName";
        public static final String EQUIPMENT_CODE = "equipmentCode";
    }
}
