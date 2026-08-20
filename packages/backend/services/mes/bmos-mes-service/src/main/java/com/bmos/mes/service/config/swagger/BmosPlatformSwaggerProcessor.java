package com.bmos.mes.service.config.swagger;

import com.bmos.web.swagger.config.BmosSwaggerProcessor;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

/**
 * 自定义接口文档名称
 */
@Component
public class BmosPlatformSwaggerProcessor extends BmosSwaggerProcessor {
    @Override
    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("BMOS 平台 接口文档")
                .build();
    }

    @Override
    protected String apiVersionName(String version) {
        return super.apiVersionName(version);
    }
}
