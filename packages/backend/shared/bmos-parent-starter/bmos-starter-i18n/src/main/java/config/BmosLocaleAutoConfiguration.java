package config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import resource.BmosMessageResource;
import resource.FrontAppI18nMessageResource;
import resource.FrontWebI18nMessageResource;
import resource.MessageResourceRegister;

/**
 * 此配置文件只能放在项目中，若放在starter中
 * bmosI18nProperties中的值无法做到动态更新
 */
@Configuration
public class BmosLocaleAutoConfiguration {

    @Bean
    public MessageSource messageSource(){
        BmosMessageResource messageSource = new BmosMessageResource();
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public FrontWebI18nMessageResource frontWebI18nMessageResource() {
        return new FrontWebI18nMessageResource();
    }

    @Bean
    public FrontAppI18nMessageResource frontAppI18NMessageResource(){
        return new FrontAppI18nMessageResource();
    }

    @Bean
    public MessageResourceRegister messageResourceRegister(){
        return new MessageResourceRegister();
    }
}
