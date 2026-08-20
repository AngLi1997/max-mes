package resource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import property.FrontWebI18nProperties;
import property.I18nProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 前端i18n资源
 */
public class FrontWebI18nMessageResource extends FrontBaseMessageResource{

    private static final Logger log = LoggerFactory.getLogger(FrontWebI18nMessageResource.class);

    FrontWebI18nProperties frontWebI18NProperties;


    @Override
    public void register(I18nProperties i18nProperties, NacosConfigManager nacosConfigManager) {
        this.frontWebI18NProperties = (FrontWebI18nProperties) i18nProperties;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public List<I18nProperties.NacosI18nLocations> getLocations() {
        return frontWebI18NProperties.getLocations();
    }

    @Override
    public String hashProperties() {
        return frontWebI18NProperties.hash();
    }
}
