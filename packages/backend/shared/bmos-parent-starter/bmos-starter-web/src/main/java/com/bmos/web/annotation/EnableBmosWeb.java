package com.bmos.web.annotation;


import com.bmos.web.config.BmosWebAutoConfiguration;
import com.bmos.web.swagger.config.Swagger3Configuration;
import com.bmos.web.version.config.BmosApiVersionConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BmosWebAutoConfiguration.class, BmosApiVersionConfiguration.class, Swagger3Configuration.class})
public @interface EnableBmosWeb {
}
