package com.bmos.unit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 修约单位id
 * @author liang
 * @version 1.0
 * @date 2024/3/27 00:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrecisionUnitId {
    String group() default "default";
}