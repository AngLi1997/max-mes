package com.bmos.platform.service.system.message.annotation;

import java.lang.annotation.*;

/**
 * @className: ServiceNameAnnotaion
 * @author: yigaohui
 * @date: 2024/11/5 16:51
 * @Version: 1.0
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ServiceMessageAnnotation {
    String value();
}
