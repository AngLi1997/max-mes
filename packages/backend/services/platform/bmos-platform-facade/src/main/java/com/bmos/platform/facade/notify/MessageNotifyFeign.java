package com.bmos.platform.facade.notify;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.notify.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @className: MessageNotifyFeign
 * @author: yigaohui
 * @date: 2025/1/9 11:34
 * @Version: 1.0
 * @description:
 */

@FeignClient(name = "bmos-platform-service", contextId = "platform-messageNotify")
public interface MessageNotifyFeign {

    @PostMapping("/api/app/platform/notifyMessage/feign/productModifyAbnormalMessage")
    ResponseInfo<Void> productModifyAbnormalMessage(@RequestBody ProductModifyAbnormalMessage productModifyAbnormalMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/batchSendProductModifyAbnormalMessage")
    ResponseInfo<Void> batchSendProductModifyAbnormalMessage(@RequestBody ProductModifyBatchMessage productModifyBatchMessage);


    @PostMapping("/api/app/platform/notifyMessage/feign/materialExpireForeWarning")
    ResponseInfo<Void> materialExpireForeWarning(@RequestBody MaterialForeWarningMessage materialForeWarningMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/auditMessage")
    ResponseInfo<Void> auditMessage(@RequestBody AuditMessage auditMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/batchSendDataOverLimit")
    ResponseInfo<Void> batchSendDataOverLimitMessage(@RequestBody DataOverLimitBatchMessage dataOverLimitMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/lisms/materialExpireWarning")
    ResponseInfo<Void> lismsMaterialExpireWarning(@RequestBody LismsMaterialExpireWarningMessage lismsMaterialExpireWarningMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/lisms/materialInventoryWarning")
    ResponseInfo<Void> lismsMaterialInventoryWarning(@RequestBody LismsMaterialInventoryWarningMessage lismsMaterialInventoryWarningMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/lisms/supplierExpireWarning")
    ResponseInfo<Void> lismsSupplierExpireWarning(@RequestBody LismsSupplierExpireWarningMessage lismsSupplierExpireWarningMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/sampleInventoryWarning")
    ResponseInfo<Void> bsmsSampleInventoryWarning(@RequestBody BsmsSampleInventoryWarningMessage bsmsSampleInventoryWarningMessage);

    @PostMapping("/api/app/platform/notifyMessage/feign/plasmaInventoryWarning")
    ResponseInfo<Void> bsmsPlasmaInventoryWarning(@RequestBody BsmsPlasmaInventoryWarningMessage bsmsPlasmaInventoryWarningMessage);
}
