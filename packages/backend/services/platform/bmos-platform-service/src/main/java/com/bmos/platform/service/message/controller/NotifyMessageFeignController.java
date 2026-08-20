package com.bmos.platform.service.message.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.common.constant.ThreadPoolConstants;
import com.bmos.platform.facade.notify.MessageNotifyFeign;
import com.bmos.platform.facade.notify.dto.*;
import com.bmos.platform.service.message.dto.AuditMessageContext;
import com.bmos.platform.service.message.dto.DataOutLimitMessageContext;
import com.bmos.platform.service.message.dto.MaterialForeWarningMessageContext;
import com.bmos.platform.service.message.dto.ProductModifyAbnormalMessageContext;
import com.bmos.platform.service.message.sender.AuditMessageSender;
import com.bmos.platform.service.message.sender.DataOverLimitSender;
import com.bmos.platform.service.message.sender.MaterialExpireForeWarningMessageSender;
import com.bmos.platform.service.message.sender.ProductModifyAbnormalMessageSender;
import com.bmos.platform.service.message.dto.*;
import com.bmos.platform.service.message.sender.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;

/**
 * 通知消息feign接口
 *
 * @className: NotifyMessageFeignController
 * @author: yigaohui
 * @date: 2025/1/9 11:40
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/notifyMessage/feign")
public class NotifyMessageFeignController implements MessageNotifyFeign {

    @Autowired
    private ProductModifyAbnormalMessageSender productModifyAbnormalMessageSender;

    @Autowired
    private MaterialExpireForeWarningMessageSender materialExpireForeWarningMessageSender;

    @Autowired
    private AuditMessageSender auditMessageSender;

    @Resource
    private DataOverLimitSender dataOverLimitSender;

    @Resource
    private LismsMaterialExpireWarningMessageSender lismsMaterialExpireWarningMessageSender;

    @Resource
    private LismsMaterialInventoryWarningMessageSender lismsMaterialInventoryWarningMessageSender;

    @Resource
    private LismsSupplierExpireWarningMessageSender lismsSupplierExpireWarningMessageSender;

    @Autowired
    private BsmsSampleInventoryWarningMessageSender bsmsSampleInventoryWarningMessageSender;

    @Autowired
    private BsmsPlasmaInventoryWarningMessageSender bsmsPlasmaInventoryWarningMessageSender;

    @PostMapping("/productModifyAbnormalMessage")
    @Override
    public ResponseInfo<Void> productModifyAbnormalMessage(ProductModifyAbnormalMessage productModifyAbnormalMessage) {
        productModifyAbnormalMessageSender.send(BeanUtil.copyProperties(productModifyAbnormalMessage, ProductModifyAbnormalMessageContext.class));
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/batchSendProductModifyAbnormalMessage")
    public ResponseInfo<Void> batchSendProductModifyAbnormalMessage(@RequestBody ProductModifyBatchMessage productModifyBatchMessage) {
        SysUser user = SysUserHolder.getUser();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        for (ProductModifyAbnormalMessage message : productModifyBatchMessage.getMessageList()) {
            ThreadPoolConstants.MESSAGE_THREAD_POOL.execute(() -> {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                SysUserHolder.setUser(user);
                message.setTime(productModifyBatchMessage.getTime());
                productModifyAbnormalMessageSender.send(BeanUtil.copyProperties(message, ProductModifyAbnormalMessageContext.class));
            });
        }
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/materialExpireForeWarning")
    public ResponseInfo<Void> materialExpireForeWarning(MaterialForeWarningMessage materialForeWarningMessage) {
        materialExpireForeWarningMessageSender.send(BeanUtil.copyProperties(materialForeWarningMessage, MaterialForeWarningMessageContext.class));
        return ResponseInfo.success();
    }

    @PostMapping("/auditMessage")
    @Override
    public ResponseInfo<Void> auditMessage(AuditMessage auditMessage) {
        auditMessageSender.send(BeanUtil.copyProperties(auditMessage, AuditMessageContext.class));
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/batchSendDataOverLimit")
    public ResponseInfo<Void> batchSendDataOverLimitMessage(@RequestBody DataOverLimitBatchMessage dataOverLimitBatchMessage) {
        SysUser user = SysUserHolder.getUser();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        for (DataOverLimitMessage message : dataOverLimitBatchMessage.getMessageList()) {
            ThreadPoolConstants.MESSAGE_THREAD_POOL.execute(() -> {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                SysUserHolder.setUser(user);
                message.setTime(message.getTime());
                dataOverLimitSender.send(BeanUtil.copyProperties(message, DataOutLimitMessageContext.class));
            });
        }
        return ResponseInfo.success();
    }

    @PostMapping("/lisms/materialExpireWarning")
    @Override
    public ResponseInfo<Void> lismsMaterialExpireWarning(LismsMaterialExpireWarningMessage lismsMaterialExpireWarningMessage) {
        lismsMaterialExpireWarningMessageSender.send(BeanUtil.copyProperties(lismsMaterialExpireWarningMessage, LismsMaterialExpireWarningMessageContext.class));
        return ResponseInfo.success();
    }

    @PostMapping("/lisms/materialInventoryWarning")
    @Override
    public ResponseInfo<Void> lismsMaterialInventoryWarning(LismsMaterialInventoryWarningMessage lismsMaterialInventoryWarningMessage) {
        lismsMaterialInventoryWarningMessageSender.send(BeanUtil.copyProperties(lismsMaterialInventoryWarningMessage, LismsMaterialInventoryWarningMessageContext.class));
        return ResponseInfo.success();
    }

    @PostMapping("/lisms/supplierExpireWarning")
    @Override
    public ResponseInfo<Void> lismsSupplierExpireWarning(LismsSupplierExpireWarningMessage lismsSupplierExpireWarningMessage) {
        lismsSupplierExpireWarningMessageSender.send(BeanUtil.copyProperties(lismsSupplierExpireWarningMessage, LismsSupplierExpireWarningMessageContext.class));
        return ResponseInfo.success();
    }

    @PostMapping("/sampleInventoryWarning")
    @Override
    public ResponseInfo<Void> bsmsSampleInventoryWarning(BsmsSampleInventoryWarningMessage bsmsSampleInventoryWarningMessage) {
        bsmsSampleInventoryWarningMessageSender.send(BeanUtil.copyProperties(bsmsSampleInventoryWarningMessage, BsmsSampleInventoryWarningMessageContext.class));
        return ResponseInfo.success();
    }

    @PostMapping("/plasmaInventoryWarning")
    @Override
    public ResponseInfo<Void> bsmsPlasmaInventoryWarning(BsmsPlasmaInventoryWarningMessage bsmsPlasmaInventoryWarningMessage) {
        bsmsPlasmaInventoryWarningMessageSender.send(BeanUtil.copyProperties(bsmsPlasmaInventoryWarningMessage, BsmsPlasmaInventoryWarningMessageContext.class));
        return ResponseInfo.success();
    }
}
