package com.bmos.lims2.server.inspect.mes.client;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.inspect.dto.InspectRejectDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * LIMS → MES 检验结果回传 client（复用 MES feign 契约 DTO）。
 */
@FeignClient(name = "bmos-mes-service", contextId = "lims-to-mes-inspect")
public interface MesInspectCallbackClient {

    /**
     * 检验结果回传（样品审核通过=检验完成时调用）。
     */
    @PostMapping("/api/app/mes/feign/inspect/callback")
    ResponseInfo<Void> inspectCallBack(@RequestBody InspectResultCallBackDTO dto);

    /**
     * 检验单退回。
     */
    @PostMapping("/api/app/mes/feign/inspect/reject")
    ResponseInfo<Void> rejectInspect(@RequestBody List<InspectRejectDTO> dtoList);
}
