package com.bmos.unit.annotation.auto;

import com.bmos.unit.config.UnitAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liang
 * @version 1.0
 * @date 2024/3/27 00:08
 * @description 启用缓存单位
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UnitAutoConfiguration.class)
public @interface EnableBmosUnit {
}
