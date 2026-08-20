package com.bmos.web.swagger.config;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.web.version.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import springfox.documentation.RequestHandler;
import springfox.documentation.RequestHandlerKey;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.PathContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.plugins.CombinedRequestHandler;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.OperationReader;
import springfox.documentation.spring.web.scanners.ApiDescriptionLookup;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.wrapper.PatternsRequestCondition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

public class BmosSwaggerApiDescriptionReader extends ApiDescriptionReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmosSwaggerApiDescriptionReader.class);
    private final OperationReader operationReader;
    private final DocumentationPluginsManager pluginsManager;
    private final ApiDescriptionLookup lookup;

    public BmosSwaggerApiDescriptionReader(@Qualifier("cachedOperations") OperationReader operationReader,
                                           DocumentationPluginsManager pluginsManager,
                                           ApiDescriptionLookup lookup) {
        super(operationReader, pluginsManager, lookup);
        this.operationReader = operationReader;
        this.pluginsManager = pluginsManager;
        this.lookup = lookup;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public List<ApiDescription> read(RequestMappingContext outerContext) {
        PatternsRequestCondition patternsCondition = outerContext.getPatternsCondition();
        ApiSelector selector = outerContext.getDocumentationContext().getApiSelector();

        List<ApiDescription> apiDescriptionList = new ArrayList<>();
        for (String path : matchingPaths(selector, patternsCondition)) {
            String methodName = outerContext.getName();
            try {
                RequestMappingContext operationContext = outerContext.copyPatternUsing(path)
                    .withKnownModels(outerContext.getModelMap());

                List<Operation> operations = operationReader.read(operationContext);
                Optional<ApiVersion> apiVersionAnnotation = outerContext.findAnnotation(ApiVersion.class);

                if (operations.size() > 0) {
                    if (apiVersionAnnotation.isPresent()) {
                        operationContext.getDocumentationContext().getRequestHandlers().forEach(requestHandler -> {
                            Optional<ApiVersion> hasApiVersion = requestHandler.findAnnotation(ApiVersion.class);
                            if (hasApiVersion.isPresent() && requestHandler.getName().equals(methodName)) {
                                String replacedPath = replacePathVersion(hasApiVersion.get().value(), path);
                                ApiDescription apiDescription = buildApiDescription(outerContext, replacedPath, methodName, operationContext, operations);
                                lookup.add(copyRequestHandlerKey(outerContext.key(), replacedPath), apiDescription);
                                apiDescriptionList.add(apiDescription);
                            }
                        });
                        return apiDescriptionList;
                    }

                    ApiDescription apiDescription = buildApiDescription(outerContext, path, methodName, operationContext, operations);
                    lookup.add(outerContext.key(), apiDescription);
                    apiDescriptionList.add(apiDescription);
                }
            } catch (Error e) {
                String contentMsg = "Skipping process path[" + path + "], method[" + methodName + "] as it has an error.";
                LOGGER.error(contentMsg, e);
            }
        }
        return apiDescriptionList;
    }

    private String replacePathVersion(String version, String path) {
        return StrUtil.replace(path, "{v}", "v" + version);
    }

    private ApiDescription buildApiDescription(RequestMappingContext outerContext,
                                               String path,
                                               String methodName,
                                               RequestMappingContext operationContext,
                                               List<Operation> operations) {
        operationContext.apiDescriptionBuilder()
            .groupName(outerContext.getGroupName())
            .operations(operations)
            .pathDecorator(pluginsManager.decorator(new PathContext(outerContext, operations.stream().findFirst())))
            .path(path)
            .description(methodName)
            .hidden(false);
        return operationContext.apiDescriptionBuilder().build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<String> matchingPaths(ApiSelector selector, PatternsRequestCondition patternsCondition) {
        return ((Set<String>) patternsCondition.getPatterns()).stream()
            .filter(selector.getPathSelector())
            .sorted(naturalOrder())
            .collect(toList());
    }

    private RequestHandler getRequestHandler(CombinedRequestHandler combinedRequestHandler, Boolean isFirst) {
        return (RequestHandler) ReflectUtil.getFieldValue(combinedRequestHandler, ReflectUtil.getField(CombinedRequestHandler.class, isFirst ? "first" : "second"));
    }

    private RequestHandlerKey copyRequestHandlerKey(RequestHandlerKey key, String path) {
        Set<String> pathMapping = new HashSet<>(1);
        pathMapping.add(path);
        return new RequestHandlerKey(pathMapping, key.getSupportedMethods(), key.getSupportedMediaTypes(), key.getProducibleMediaTypes());
    }


}
