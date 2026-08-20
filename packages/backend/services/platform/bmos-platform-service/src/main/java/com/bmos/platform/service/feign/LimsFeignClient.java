package com.bmos.platform.service.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.platform.service.material.dto.RemoteIssueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bmos-lims2-service", contextId = "bmos-lims2-service")
public interface LimsFeignClient extends CommonFeignClient{

    @PostMapping("/api/app/lims2/material/issueMaterialAndCategory")
    ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueDTO dto);

    @PostMapping("/api/app/mes/procedure/expressiony/conditon/update")
    ResponseInfo<Void> conditionUpdate(@RequestBody StateEvent event);

}
