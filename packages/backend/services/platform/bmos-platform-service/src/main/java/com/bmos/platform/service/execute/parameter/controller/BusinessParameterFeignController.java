package com.bmos.platform.service.execute.parameter.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/parameter/feign")
@Validated
public class BusinessParameterFeignController implements BusinessParameterFeign {

    @Autowired
    private BusinessParameterService businessParameterService;

    /**
     * 根据编码查询参数
     * @param code
     * @return
     */
    @Override
    @GetMapping("/detailByCode")
    public ResponseInfo<BusinessParameterDetailFeignVO> detailByCode(@RequestParam("code") String code){
        return ResponseInfo.success(businessParameterService.detailFeignByCode(code));
    }

}
