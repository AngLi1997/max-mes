package com.bmos.platform.service.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.platform.service.material.dto.RemoteIssueDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface CommonFeignClient {

    ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueDTO dto);

    ResponseInfo<Void> conditionUpdate(@RequestBody StateEvent event);

}
