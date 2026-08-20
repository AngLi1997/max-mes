package com.bmos.platform.service.message.sender;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.LismsSupplierExpireWarningMessageContext;
import com.bmos.platform.service.message.dto.MessageContextDTO;
import com.bmos.platform.service.message.dto.MessageDTO;
import com.bmos.platform.service.message.entity.MessageTemplate;
import com.bmos.platform.service.message.mapper.MessageTemplateMapper;
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

@Service
public class LismsSupplierExpireWarningMessageSender extends DefaultMessageSender {

    private static final String CONTENT_TEMPLATE_ALL = "ALL";

    private static final String CONTENT_TEMPLATE_SECTION = "SECTION";

    private static final String CONTENT_SUPPLIER_INFO = "supplierCnShortNameList";

    private static final String CONTENT_SUPPLIER_COUNT = "count";

    @Autowired
    private MessageTemplateMapper templateMapper;

    @Autowired
    private RoleService roleService;


    @Override
    protected String getContent(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        return "";
    }

    @Override
    protected String getTitle(MessageTemplate messageTemplate, MessageContextDTO messageContextDTO) {
        LismsSupplierExpireWarningMessageContext context = (LismsSupplierExpireWarningMessageContext) messageContextDTO;
        List<String> supplierCnShortNamelList = context.getSupplierCnShortNamelList();

        EvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable(CONTENT_SUPPLIER_INFO, supplierCnShortNamelList.stream()
                .limit(5)
                .collect(Collectors.joining("、")));
        ctx.setVariable(CONTENT_SUPPLIER_COUNT, supplierCnShortNamelList.size());

        ExpressionParser ep = new SpelExpressionParser();
        String titleTemplate = messageTemplate.getTitleTemplate();
        return ep.parseExpression(titleTemplate, new TemplateParserContext()).getValue(ctx, String.class);
    }

    @Override
    protected MessageTypeEnum getMessageType() {
        return MessageTypeEnum.LISMS_SUPPLIER_EXPIRE_WARNING;
    }

    @Override
    protected Collection<String> getReceiveUserIds(MessageContextDTO messageContextDTO) {
        return roleService.authUserList(MessageTypeEnum.LISMS_SUPPLIER_EXPIRE_WARNING.getAuthorityCode())
                .stream().map(FeignUserVO::getUserId).collect(Collectors.toSet());
    }


    @Override
    protected MessageDTO generateMessage(MessageContextDTO messageContextDTO) {
        LismsSupplierExpireWarningMessageContext context = (LismsSupplierExpireWarningMessageContext) messageContextDTO;
        List<String> supplierCnShortNamelList = context.getSupplierCnShortNamelList();

        // 根据物料批次数量选择合适的模板
        String contentTemplateType = supplierCnShortNamelList.size() <= 5 ? CONTENT_TEMPLATE_ALL : CONTENT_TEMPLATE_SECTION;

        MessageTemplate messageTemplate = getMessageTemplateByContentType(contentTemplateType);
        if (messageTemplate == null) {
            return null;
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTitle(getTitle(messageTemplate, messageContextDTO));
        messageDTO.setContent(getContent(messageTemplate, messageContextDTO));
        messageDTO.setTime(messageContextDTO.getTime());
        return messageDTO;
    }

    private MessageTemplate getMessageTemplateByContentType(String contentType) {
        LambdaQueryWrapper<MessageTemplate> ql = new QueryWrapper<MessageTemplate>().lambda();
        ql.eq(MessageTemplate::getContentTemplate, contentType)
                .eq(MessageTemplate::getMessageType, this.getMessageType());
        return templateMapper.selectOne(ql);
    }
}
