package com.bmos.wms.inspect.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.inspect.dto.InspectRejectDTO;
import com.bmos.wms.inspect.dto.InspectResultCallBackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * WMS 请验结果回传 feign 接口
 *
 * <p>由 LIMS 调用，将检验结果 / 退回信息回送给 WMS。
 * 与 mes-feign 的 {@code com.bmos.mes.inspect.feign.InspectFeign} 同结构，仅服务名 / contextId / 路径前缀不同。
 */
@FeignClient(name = "bmos-wms-service", contextId = "wms-inspect")
public interface InspectFeign {

    /**
     * 检验结果回传
     */
    @PostMapping("/api/app/wms/feign/inspect/callback")
    ResponseInfo<Void> inspectCallBack(@RequestBody InspectResultCallBackDTO inspectResultCallBackDTO);

    /**
     * 检验单退回
     */
    @PostMapping("/api/app/wms/feign/inspect/reject")
    ResponseInfo<Void> rejectInspect(@RequestBody List<InspectRejectDTO> dtoList);
}
