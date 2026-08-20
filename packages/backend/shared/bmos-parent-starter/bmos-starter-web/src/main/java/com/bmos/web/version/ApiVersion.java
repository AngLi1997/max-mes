package com.bmos.web.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {

    String value() default Version.V1;

    interface Version {
        String V1 = "1";
        String V2 = "2";
        String V3 = "3";

        List<String> SUPPORT_VERSIONS =
                Arrays.asList(ApiVersion.Version.V1, ApiVersion.Version.V2, ApiVersion.Version.V3);
    }
}
