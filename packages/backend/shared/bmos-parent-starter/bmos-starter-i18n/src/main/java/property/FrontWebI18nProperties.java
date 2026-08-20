package property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties("front.i18n.web")
@RefreshScope
public class FrontWebI18nProperties extends I18nProperties {

}
