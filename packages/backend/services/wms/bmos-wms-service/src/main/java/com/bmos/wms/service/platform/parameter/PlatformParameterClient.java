package com.bmos.wms.service.platform.parameter;

import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.service.platform.parameter.vo.BusinessParameterDetailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 平台业务参数 feign 客户端（与 mes-service 同款）
 */
@FeignClient(name = "bmos-platform-service", contextId = "wms-bmos-parameter")
public interface PlatformParameterClient {

    @GetMapping("/api/app/platform/business/parameter/detailByCode/{code}")
    @ApiOperation("业务参数详情")
    ResponseInfo<BusinessParameterDetailVO> detail(@PathVariable("code") String code);
}
