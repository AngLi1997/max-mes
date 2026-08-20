package com.bmos.web.swagger.config;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.constant.RequestConstant;
import com.bmos.web.version.ApiVersion;
import io.swagger.annotations.Api;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BmosSwaggerProcessor implements BeanDefinitionRegistryPostProcessor {

    private final static String SWAGGER_BEAN_NAME = "apiDescriptionReader";

    private final static String DEFAULT = "default";

    private final static String DEFAULT_DOCKET = "defaultDocket";

    private final static String DOCKET = "docket";

    private final static String VERSION_NAME = "版本-";
    private final static String DOCUMENT_NAME = "BMOS 接口文档";


    public BmosSwaggerProcessor() {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(Docket.class);
        ConstructorArgumentValues constructorArgumentValues = getConstructorArgumentValues(definition);
        registry.registerBeanDefinition(DEFAULT_DOCKET, definition);

        ApiVersion.Version.SUPPORT_VERSIONS.forEach(version -> {
            GenericBeanDefinition beanDefinition = buildGenericBeanDefinition(constructorArgumentValues);
            registry.registerBeanDefinition(DOCKET + version, beanDefinition);
        });


        //替换 swagger 原有逻辑
        if (registry.containsBeanDefinition(SWAGGER_BEAN_NAME)) {
            registry.removeBeanDefinition(SWAGGER_BEAN_NAME);
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(BmosSwaggerApiDescriptionReader.class);
            registry.registerBeanDefinition(SWAGGER_BEAN_NAME, beanDefinition);
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanName : beanFactory.getBeanNamesForType(Docket.class)) {

            String version = beanName.equals(DEFAULT_DOCKET) ? DEFAULT : StrUtil.replace(beanName, DOCKET, "");

            Docket docket = (Docket) beanFactory.getBean(beanName);
            docket
                    .apiInfo(apiInfo())
                    .directModelSubstitute(LocalDate.class, String.class)
                    .directModelSubstitute(LocalDateTime.class, String.class)
                    .groupName(apiVersionName(version))
                    .select()
                    .apis(input -> {
                        if (!RequestHandlerSelectors.withClassAnnotation(Api.class).test(input)) {
                            return false;
                        }
                        Optional<ApiVersion> annotation = input.findAnnotation(ApiVersion.class);
                        if (DEFAULT.equals(version) && !annotation.isPresent()) {
                            return true;
                        }
                        return annotation.map(apiVersion -> apiVersion.value().equals(version)).orElse(false);
                    })
                    .paths(PathSelectors.any())
                    .build()
                    .securitySchemes(securitySchemes())
                    .securityContexts(securityContexts());
        }
    }

    protected ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(DOCUMENT_NAME)
                .build();
    }

    protected String apiVersionName(String version) {
        return VERSION_NAME + version;
    }

    private ConstructorArgumentValues getConstructorArgumentValues(GenericBeanDefinition definition) {
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(DocumentationType.OAS_30);
        definition.setConstructorArgumentValues(constructorArgumentValues);
        return constructorArgumentValues;
    }

    private GenericBeanDefinition buildGenericBeanDefinition(ConstructorArgumentValues constructorArgumentValues) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(Docket.class);
        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
        return beanDefinition;
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> securitySchemes = new ArrayList<>();
        securitySchemes.add(new ApiKey(RequestConstant.BMOS_TOKEN, RequestConstant.BMOS_TOKEN, "header"));
        return securitySchemes;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(null)
                        .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(RequestConstant.BMOS_TOKEN, authorizationScopes));
        return securityReferences;
    }


}
