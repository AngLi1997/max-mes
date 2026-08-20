package com.bmos.mes.inspect.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.inspect.dto.InspectRejectDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 请验feign接口
 */
@FeignClient(name = "bmos-mes-service", contextId = "mes-inspect")
public interface InspectFeign {

    /**
     * 检验结果回传
     * @param dtoList
     * @return
     */
    @PostMapping("/api/app/mes/feign/inspect/callback")
    ResponseInfo<Void> inspectCallBack(@RequestBody InspectResultCallBackDTO inspectResultCallBackDTO);

    /**
     * 检验单退回
     * @param dtoList
     * @return
     */
    @PostMapping("/api/app/mes/feign/inspect/reject")
    ResponseInfo<Void> rejectInspect(@RequestBody List<InspectRejectDTO> dtoList);
}
