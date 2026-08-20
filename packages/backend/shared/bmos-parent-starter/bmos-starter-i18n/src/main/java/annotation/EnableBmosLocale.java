package annotation;

import property.BmosI18nProperties;
import config.BmosLocaleAutoConfiguration;
import property.FrontAppI18nProperties;
import property.FrontWebI18nProperties;
import listener.BmosRefreshScopeListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BmosLocaleAutoConfiguration.class, BmosRefreshScopeListener.class})
@EnableConfigurationProperties({BmosI18nProperties.class, FrontWebI18nProperties.class, FrontAppI18nProperties.class})
public @interface EnableBmosLocale {
}