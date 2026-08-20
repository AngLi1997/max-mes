package property;

import com.alibaba.cloud.nacos.NacosConfigManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface I18nPropertyMethod {

    /**
     * 注册资源
     * @param i18nProperties
     * @param nacosConfigManager
     */
    void register(I18nProperties i18nProperties, NacosConfigManager nacosConfigManager);

    /**
     * 获取i18n资源
     * @return
     */
    List<I18nProperties.NacosI18nLocations> getLocations();

    /**
     * 资源hash值
     * @return
     */
    String hashProperties();

    /**
     * 加载对应语言以及前端来源下的所有资源
     * @param request
     * @return
     */
    I18nJson loadAllProperties(HttpServletRequest request);

}
