package resource;

import property.I18nJson;
import property.I18nProperties;

import java.util.Locale;

public interface I18nMessageResource {

    String KEY_TEMPLATE = "{}-{}";

    String DEFAULT_MESSAGE_SOURCE_NAME = "zh_CN";

    /**
     * 解析Locale
     * @param dataId
     * @return
     */
    Locale resolveLocale(String dataId);

    /**
     * 初始化当前配置
     */
    void initMessageSource()  throws Exception;

    /**
     * 配置改变之后
     */
    void propertyChange();

    /**
     * 替换配置
     * @param locations
     * @param locale
     * @return
     */
    I18nJson replaceProperties(I18nProperties.NacosI18nLocations locations, Locale locale);

    /**
     * 根据配置文件内容加载配置
     * @param config
     * @param nacosI18nLocation
     * @param locale
     * @throws Exception
     */
    void loadPropertiesByConfig(String config, I18nProperties.NacosI18nLocations nacosI18nLocation, Locale locale) throws Exception;

}
