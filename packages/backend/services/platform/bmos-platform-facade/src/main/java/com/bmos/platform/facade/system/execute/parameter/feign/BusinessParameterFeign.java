package com.bmos.platform.facade.system.execute.parameter.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bmos-platform-service", contextId = "platform-system-business-parameter")
public interface BusinessParameterFeign {

    /**
     * 根据编码查询参数
     * @param code
     * @return
     */
    @GetMapping("/api/app/platform/business/parameter/feign/detailByCode")
    ResponseInfo<BusinessParameterDetailFeignVO> detailByCode(@RequestParam("code") String code);

}
