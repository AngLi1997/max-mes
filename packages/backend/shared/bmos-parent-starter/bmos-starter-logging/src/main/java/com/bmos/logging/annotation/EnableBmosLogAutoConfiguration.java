package com.bmos.logging.annotation;


import com.bmos.logging.config.LogPropertiesConfig;
import com.bmos.logging.util.LogTranslateUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties(LogPropertiesConfig.class)
@Import({LogTranslateUtil.class})
public @interface EnableBmosLogAutoConfiguration {

}
