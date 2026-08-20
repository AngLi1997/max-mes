package com.bmos.mes.service.platform.print.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.print.dto.PrintBatchDTO;
import com.bmos.mes.service.platform.print.dto.PrintCommonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 平台 打印标签接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/14 18:49
 */
@FeignClient(name = "bmos-platform-service", contextId = "bmos-platform-tag")
public interface PlatformTagClient {

    @PostMapping("/api/app/platform/tag/instance/print")
    ResponseInfo<Void> printTag(@RequestBody PrintCommonDTO printerDTO);

    @PostMapping("/api/app/platform/tag/instance/printBatchSameDevice")
    ResponseInfo<Void> printBatchTags(@RequestBody PrintBatchDTO printBatchDTO);
}
