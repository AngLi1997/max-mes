package com.bmos.mes.service.config.user;

import com.bmos.mes.service.utils.ExecuteDateCalculateUtil;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExecutionDataCalculateConfiguration implements CommandLineRunner {

    @Resource
    private BusinessParameterFeign businessParameterFeign;


    @Override
    public void run(String... args) {
        ExecuteDateCalculateUtil.init(businessParameterFeign);
    }
}
