package resource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import property.FrontAppI18nProperties;
import property.I18nProperties;

import java.util.List;

/**
 * 前端i18n资源
 */
public class FrontAppI18nMessageResource extends FrontBaseMessageResource {

    private FrontAppI18nProperties frontAppI18NProperties;

    @Override
    public void register(I18nProperties i18nProperties, NacosConfigManager nacosConfigManager) {
        this.frontAppI18NProperties = (FrontAppI18nProperties) i18nProperties;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public List<I18nProperties.NacosI18nLocations> getLocations(){
        return frontAppI18NProperties.getLocations();
    }

    @Override
    public String hashProperties(){
        return frontAppI18NProperties.hash();
    }

}
