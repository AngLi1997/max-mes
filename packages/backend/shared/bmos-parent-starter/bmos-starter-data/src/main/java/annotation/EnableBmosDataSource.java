package annotation;

import config.DataSourceConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yincc
 * @version 1.0
 * @date 2022/6/22 12:45
 * @description 启用缓存配置：redis以及redisson
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DataSourceConfiguration.class)
public @interface EnableBmosDataSource {
}
