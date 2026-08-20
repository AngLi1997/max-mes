package com.bmos.common.exporter.annotation;

import cn.hutool.core.annotation.Alias;

import java.lang.annotation.*;

/**
 * 用于指定excel实体属性在excel当中的自定义格式
 * @author : yigaohui
 * @version : 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiExcelDataFormat {
    /**
     * 数据格式化字符串
     * @return
     */
    String value() default "";
    @Alias("value")
    String dataFormat() default "";

}
