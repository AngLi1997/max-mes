package com.bmos.web.version.config;

import com.bmos.web.version.ApiVersionRequestMappingHandlerMapping;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class BmosApiVersionConfiguration implements WebMvcRegistrations {

    @Override
    @NonNull
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping();
    }
}
