package resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.bmos.common.constant.RequestConstant;
import listener.BmosRefreshScopeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import property.I18nJson;
import property.I18nProperties;
import property.I18nPropertyMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static cn.hutool.core.text.StrPool.C_DOT;

public abstract class FrontBaseMessageResource implements I18nPropertyMethod, I18nMessageResource {

    private static final Logger log = LoggerFactory.getLogger(BmosMessageResource.class);

    final ConcurrentMap<Locale, ConcurrentHashMap<String, I18nJson>> cachedProperties = new ConcurrentHashMap<>();

    final Map<I18nProperties.NacosI18nLocations, Locale> nacosPropertyLocaleMap = new ConcurrentHashMap<>();

    final Map<I18nProperties.NacosI18nLocations, BmosRefreshScopeListener.BmosI18nListener> bmosI18nListenerMap = new ConcurrentHashMap<>();

    NacosConfigManager nacosConfigManager;

    volatile String i18nHash;

    @Override
    public void initMessageSource() throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend config init...");
        if (StrUtil.isNotEmpty(i18nHash)){
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend config already initialized.");
            return ;
        }
        // 加载语言配置
        List<I18nProperties.NacosI18nLocations> locations = getLocations();
        if (CollUtil.isEmpty(locations)){
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend no configuration, no initialization required.");
            return ;
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        for (I18nProperties.NacosI18nLocations nacosI18nLocation : locations) {
            Locale locale = this.resolveLocale(nacosI18nLocation.getDataId());
            loadProperties(configService, nacosI18nLocation, locale);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend config:《dataId:{} group:{}》 init complete.",
                    nacosI18nLocation.getDataId(), nacosI18nLocation.getGroup());
        }
        this.i18nHash = hashProperties();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend config init complete.");
    }

    @Override
    public Locale resolveLocale(String dataId) {
        dataId = StrUtil.subBefore(dataId, C_DOT, false);
        List<String> split = StrUtil.split(dataId, StrUtil.C_UNDERLINE);
        if (CollUtil.isEmpty(split)){
            split = StrUtil.split(DEFAULT_MESSAGE_SOURCE_NAME, StrUtil.C_UNDERLINE);
            return new Locale(split.get(0), split.get(1));
        }
        return new Locale(split.get(split.size() - 2), split.get(split.size() - 1));
    }

    @Override
    public I18nJson loadAllProperties(HttpServletRequest request) {
        String lang = request.getHeader(RequestConstant.LANGUAGE);
        Locale locale = Locale.getDefault();
        if (StrUtil.isNotBlank(lang)) {
            List<String> chars = StrUtil.split(lang, StrUtil.C_UNDERLINE, 2);
            locale = new Locale(chars.get(0), chars.get(1));
        }
        ConcurrentHashMap<String, I18nJson> locateI18nJsonMap = cachedProperties.get(locale);
        if (CollUtil.isEmpty(locateI18nJsonMap)){
            return new I18nJson();
        }
        I18nJson i18nJson = new I18nJson();
        for (I18nJson curI18nJson : locateI18nJsonMap.values()) {
            i18nJson.putAll(curI18nJson);
        }
        return i18nJson;
    }

    @Override
    public void propertyChange() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend config changing...");
        if (StrUtil.equals(hashProperties(), i18nHash)){
            // 代表配置没有发生变化 不进行配置更新
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend no configuration change, no change required.");
            return ;
        }
        List<I18nProperties.NacosI18nLocations> deletedNacosI18nLocations = findDeleteLocations();
        List<I18nProperties.NacosI18nLocations> addNacosI18nLocations = findAddLocations();
        solveDeleteLocations(deletedNacosI18nLocations);
        solveAddLocations(addNacosI18nLocations);
        this.i18nHash = hashProperties();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>frontend config change, add config[{}] complete. delete config[{}] complete.",
                JSON.toJSONString(addNacosI18nLocations), JSON.toJSONString(deletedNacosI18nLocations));
    }

    @Override
    public I18nJson replaceProperties(I18nProperties.NacosI18nLocations locations, Locale locale){
        ConcurrentHashMap<String, I18nJson> dataIdGroupPropertyMap = cachedProperties.getOrDefault(locale, new ConcurrentHashMap<>());
        cachedProperties.put(locale, dataIdGroupPropertyMap);
        String dataIdGroupKey = StrUtil.format(KEY_TEMPLATE, locations.getGroup(), locations.getDataId());
        // 每次获取的时候都给一个新的Properties进行替换
        I18nJson i18NJson = new I18nJson();
        dataIdGroupPropertyMap.put(dataIdGroupKey, i18NJson);
        return i18NJson;
    }

    @Override
    public void loadPropertiesByConfig(String config, I18nProperties.NacosI18nLocations nacosI18nLocation, Locale locale) throws Exception {
        I18nJson i18NJson = this.replaceProperties(nacosI18nLocation, locale);
        I18nJson i18nJson = JSON.parseObject(config, I18nJson.class);
        i18NJson.putAll(i18nJson);
    }

    private void loadProperties(ConfigService configService, I18nProperties.NacosI18nLocations nacosI18nLocation, Locale locale) throws Exception {
        String config = configService.getConfig(nacosI18nLocation.getDataId(), nacosI18nLocation.getGroup(), 5000);
        // 确保使用正确的字符编码
        this.loadPropertiesByConfig(config, nacosI18nLocation, locale);
        // 添加监听器，监听配置变化
        BmosRefreshScopeListener.BmosI18nListener bmosI18nListener = new BmosRefreshScopeListener.BmosI18nListener(locale, this, nacosI18nLocation.copy());
        configService.addListener(nacosI18nLocation.getDataId(), nacosI18nLocation.getGroup(), bmosI18nListener);
        I18nProperties.NacosI18nLocations nacosI18nLocationsCopy = nacosI18nLocation.copy();
        nacosPropertyLocaleMap.put(nacosI18nLocationsCopy, locale);
        bmosI18nListenerMap.put(nacosI18nLocationsCopy, bmosI18nListener);
    }

    private List<I18nProperties.NacosI18nLocations> findAddLocations() {
        List<I18nProperties.NacosI18nLocations> res = new ArrayList<>();
        if (CollUtil.isEmpty(getLocations())){
            return res;
        }
        for (I18nProperties.NacosI18nLocations location : getLocations()) {
            Boolean haveEqual = false;
            for (I18nProperties.NacosI18nLocations nacosI18nLocation : nacosPropertyLocaleMap.keySet()) {
                if (haveEqual = location.equalData(nacosI18nLocation)){
                    break;
                }
            }
            if (!haveEqual){
                res.add(location.copy());
            }
        }
        return res;
    }

    private List<I18nProperties.NacosI18nLocations> findDeleteLocations() {
        List<I18nProperties.NacosI18nLocations> res = new ArrayList<>();
        if (CollUtil.isEmpty(getLocations())){
            res.addAll(nacosPropertyLocaleMap.keySet());
            return res;
        }
        for (I18nProperties.NacosI18nLocations prevNacosI18nLocations : nacosPropertyLocaleMap.keySet()) {
            Boolean haveEqual = false;
            for (I18nProperties.NacosI18nLocations location : getLocations()) {
                if (haveEqual = prevNacosI18nLocations.equalData(location)){
                    break;
                }
            }
            if (!haveEqual){
                res.add(prevNacosI18nLocations);
            }
        }
        return res;
    }

    private void solveAddLocations(List<I18nProperties.NacosI18nLocations> addNacosI18nLocations) {
        if (CollUtil.isEmpty(addNacosI18nLocations)) {
            return ;
        }
        for (I18nProperties.NacosI18nLocations location : addNacosI18nLocations) {
            Locale locale = this.resolveLocale(location.getDataId());
            try {
                loadProperties(nacosConfigManager.getConfigService(), location, locale);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void solveDeleteLocations(List<I18nProperties.NacosI18nLocations> locations) {
        if (CollUtil.isEmpty(locations)){
            return ;
        }
        for (I18nProperties.NacosI18nLocations location : locations) {
            this.removeLocation(location, nacosPropertyLocaleMap.get(location));
            nacosPropertyLocaleMap.remove(location);

        }
        // 剔除对应的Nacos监听器
        removeListeners(locations);
    }

    private void removeListeners(List<I18nProperties.NacosI18nLocations> locations) {
        ConfigService configService = nacosConfigManager.getConfigService();
        for (I18nProperties.NacosI18nLocations location : locations) {
            BmosRefreshScopeListener.BmosI18nListener remove = bmosI18nListenerMap.remove(location);
            configService.removeListener(location.getDataId(), location.getGroup(), remove);
        }
    }

    private void removeLocation(I18nProperties.NacosI18nLocations locations, Locale locale){
        ConcurrentHashMap<String, I18nJson> dataIdGroupPropertyMap = cachedProperties.get(locale);
        if (dataIdGroupPropertyMap != null){
            String dataIdGroupKey = StrUtil.format(KEY_TEMPLATE, locations.getGroup(), locations.getDataId());
            dataIdGroupPropertyMap.remove(dataIdGroupKey);

        }
        if (CollUtil.isEmpty(dataIdGroupPropertyMap)){
            cachedProperties.remove(locale);
        }
    }

}
