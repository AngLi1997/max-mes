package com.bmos.platform.service.tag.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标签管理数据源注解
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/4 14:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface TagDataSource {

    /**
     * 字段名称
     */
    String name();

    /**
     * 示例值
     */
    String exampleValue();
}
