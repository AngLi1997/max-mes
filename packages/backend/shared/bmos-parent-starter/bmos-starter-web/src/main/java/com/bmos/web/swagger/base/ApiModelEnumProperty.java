package com.bmos.web.swagger.base;

import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.common.validate.EnumValidate;

import java.lang.annotation.*;

/**
 * swagger 枚举属性
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiModelEnumProperty {

    /**
     * 字段名
     */
    String value();

    /**
     * 枚举类
     */
    Class<? extends Enum<? extends KeyValueEnum>> enumClass() default EnumValidate.DefaultKeyValueEnum.class;

    /**
     * 可选枚举值，即swagger只会显示该指定值对应的枚举描述，支持String, Integer
     */
    String[] values() default {};

    boolean required() default false;
}

