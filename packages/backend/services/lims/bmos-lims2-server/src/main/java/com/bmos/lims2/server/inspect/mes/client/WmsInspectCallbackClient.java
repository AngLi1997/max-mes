package com.bmos.lims2.server.inspect.mes.client;

import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.inspect.dto.InspectRejectDTO;
import com.bmos.wms.inspect.dto.InspectResultCallBackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * LIMS → WMS 检验结果回传 client（结构 1:1 复制 {@link MesInspectCallbackClient}，仅服务 / 路径 / DTO 包名不同）。
 *
 * <p>调用条件：{@code lm_inspection_order.source_system = 'WMS'} 时由 audit 流程选择此 client。
 */
@FeignClient(name = "bmos-wms-service", contextId = "lims-to-wms-inspect")
public interface WmsInspectCallbackClient {

    /** 检验结果回传 */
    @PostMapping("/api/app/wms/feign/inspect/callback")
    ResponseInfo<Void> inspectCallBack(@RequestBody InspectResultCallBackDTO dto);

    /** 检验单退回 */
    @PostMapping("/api/app/wms/feign/inspect/reject")
    ResponseInfo<Void> rejectInspect(@RequestBody List<InspectRejectDTO> dtoList);
}
