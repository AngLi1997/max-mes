package com.bmos.platform.facade.auth.annotation;


import com.bmos.platform.facade.auth.config.BmosAuthAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BmosAuthAutoConfiguration.class})
public @interface EnableBmosAuth {
}
