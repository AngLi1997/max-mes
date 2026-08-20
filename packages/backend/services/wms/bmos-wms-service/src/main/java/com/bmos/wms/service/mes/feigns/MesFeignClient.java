package com.bmos.wms.service.mes.feigns;

import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.service.mes.dto.SendOutFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * mes feign
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/16 16:20
 */
@FeignClient(name = "bmos-mes-service", contextId = "bmos-mes-service")
public interface MesFeignClient {

    /**
     * 发料
     *
     * @param dto 发料参数
     * @return
     */
    @PostMapping("/api/app/mes/requisition/receive/sendOut")
    ResponseInfo<Void> sendOut(@RequestBody @Validated SendOutFeignDTO dto);

    /**
     * 取消发料
     *
     * @param requisitionPlanId 领料计划id
     * @return
     */
    @PostMapping("/api/app/mes/requisition/receive/cancelSendOut")
    ResponseInfo<Void> cancelSendOut(@RequestParam("requisitionPlanId") Long requisitionPlanId);
}
