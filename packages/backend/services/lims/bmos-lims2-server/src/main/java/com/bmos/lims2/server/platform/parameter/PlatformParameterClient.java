package com.bmos.lims2.server.platform.parameter;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.platform.parameter.vo.BusinessParameterDetailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-parameter")
public interface PlatformParameterClient {
    @GetMapping("/api/app/platform/business/parameter/detailByCode/{code}")
    @ApiOperation("详情--后续废弃使用同级detailByCode接口")
    ResponseInfo<BusinessParameterDetailVO> detail(@PathVariable("code") String code);
}
