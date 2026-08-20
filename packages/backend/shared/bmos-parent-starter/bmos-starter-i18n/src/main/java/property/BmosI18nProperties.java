package property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "i18n")
@RefreshScope
public class BmosI18nProperties extends I18nProperties {

}
