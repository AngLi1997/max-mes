package com.bmos.web.swagger.config;

import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.RequestHandlerCombiner;

import java.util.List;

public class BmosSwaggerRequestHandlerCombiner implements RequestHandlerCombiner {
    @Override
    public List<RequestHandler> combine(List<RequestHandler> sources) {
        return sources;
    }
}