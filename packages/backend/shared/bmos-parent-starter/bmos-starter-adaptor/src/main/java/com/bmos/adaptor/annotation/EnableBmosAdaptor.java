package com.bmos.adaptor.annotation;

import com.bmos.adaptor.config.BmosApiAdaptorAutoConfiguration;
import com.bmos.adaptor.config.FeignConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BmosApiAdaptorAutoConfiguration.class, FeignConfiguration.class})
public @interface EnableBmosAdaptor {
}
