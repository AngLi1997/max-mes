package resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import property.BmosI18nProperties;
import property.I18nJson;
import property.I18nProperties;
import property.I18nPropertyMethod;
import listener.BmosRefreshScopeListener;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static cn.hutool.core.text.StrPool.C_DOT;

public class BmosMessageResource extends ResourceBundleMessageSource implements I18nMessageResource, I18nPropertyMethod {

    private static final Logger log = LoggerFactory.getLogger(BmosMessageResource.class);

    private BmosI18nProperties bmosI18nProperties;

    private NacosConfigManager nacosConfigManager;

    private volatile String backendI18nHash;

    private final ConcurrentMap<Locale, ConcurrentHashMap<String, I18nJson>> cachedProperties = new ConcurrentHashMap<>();

    private final Map<I18nProperties.NacosI18nLocations, Locale> nacosPropertyLocaleMap = new ConcurrentHashMap<>();

    private final Map<I18nProperties.NacosI18nLocations, BmosRefreshScopeListener.BmosI18nListener> bmosI18nListenerMap = new ConcurrentHashMap<>();

    public BmosMessageResource(){
        this.setAlwaysUseMessageFormat(true);
    }

    @Override
    @Nullable
    protected MessageFormat resolveCode(String code, Locale locale) {
        ConcurrentHashMap<String, I18nJson> propertiesMap = cachedProperties.get(locale);
        if (CollUtil.isEmpty(propertiesMap)){
            return null;
        }
        for (String key : propertiesMap.keySet()) {
            I18nJson properties = propertiesMap.get(key);
            if (properties != null) {
                String message = properties.get(code);
                if (message != null) {
                    return new MessageFormat(message, locale);
                }
            }
        }
        return null;
    }

    @Override
    public void register(I18nProperties i18nProperties, NacosConfigManager nacosConfigManager) {
        this.bmosI18nProperties = (BmosI18nProperties) i18nProperties;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public List<I18nProperties.NacosI18nLocations> getLocations() {
        return bmosI18nProperties.getLocations();
    }

    @Override
    public String hashProperties() {
        return bmosI18nProperties.hash();
    }

    @Override
    public I18nJson loadAllProperties(HttpServletRequest request) {
        // 后端不能进行获取
        return new I18nJson();
    }

    @Override
    public Locale resolveLocale(String dataId) {
        // 剔除dataId中最后一个.后面的所有字符
        dataId = StrUtil.subBefore(dataId, C_DOT, false);
        List<String> split = StrUtil.split(dataId, StrUtil.C_UNDERLINE);
        if (CollUtil.isEmpty(split)){
            split = StrUtil.split(DEFAULT_MESSAGE_SOURCE_NAME, StrUtil.C_UNDERLINE);
            return new Locale(split.get(0), split.get(1));
        }
        return new Locale(split.get(split.size() - 2), split.get(split.size() - 1));
    }

    @Override
    public void initMessageSource() throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend config init...");
        if (StrUtil.isNotEmpty(backendI18nHash)){
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend config already initialized.");
            return ;
        }
        // 加载语言配置
        List<I18nProperties.NacosI18nLocations> locations = bmosI18nProperties.getLocations();
        if (CollUtil.isEmpty(locations)){
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend no configuration, no initialization required.");
            return ;
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        for (I18nProperties.NacosI18nLocations nacosI18nLocation : locations) {
            Locale locale = this.resolveLocale(nacosI18nLocation.getDataId());
            loadProperties(configService, nacosI18nLocation, locale);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend config:《dataId:{} group:{}》 init complete.",
                    nacosI18nLocation.getDataId(), nacosI18nLocation.getGroup());
        }
        this.backendI18nHash = bmosI18nProperties.hash();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend config init complete.");
    }

    @Override
    public void propertyChange() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend config changing...");
        // bmosI18nProperties可能发生了变化
        if (StrUtil.equals(bmosI18nProperties.hash(), backendI18nHash)){
            // 代表配置没有发生变化 不进行配置更新
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend no configuration change, no change required.");
            return ;
        }
        List<I18nProperties.NacosI18nLocations> deletedNacosI18nLocations = findDeleteLocations();
        List<I18nProperties.NacosI18nLocations> addNacosI18nLocations = findAddLocations();
        solveDeleteLocations(deletedNacosI18nLocations);
        solveAddLocations(addNacosI18nLocations);
        this.backendI18nHash = bmosI18nProperties.hash();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>backend config change, add config[{}] complete. delete config[{}] complete.",
                JSON.toJSONString(addNacosI18nLocations), JSON.toJSONString(deletedNacosI18nLocations));
    }

    private List<I18nProperties.NacosI18nLocations> findAddLocations() {
        List<I18nProperties.NacosI18nLocations> res = new ArrayList<>();
        if (CollUtil.isEmpty(bmosI18nProperties.getLocations())){
            return res;
        }
        for (I18nProperties.NacosI18nLocations location : bmosI18nProperties.getLocations()) {
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
        if (CollUtil.isEmpty(bmosI18nProperties.getLocations())){
            res.addAll(nacosPropertyLocaleMap.keySet());
            return res;
        }
        for (I18nProperties.NacosI18nLocations prevNacosI18nLocations : nacosPropertyLocaleMap.keySet()) {
            Boolean haveEqual = false;
            for (I18nProperties.NacosI18nLocations location : bmosI18nProperties.getLocations()) {
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

    public I18nJson replaceProperties(I18nProperties.NacosI18nLocations locations, Locale locale){
        ConcurrentHashMap<String, I18nJson> dataIdGroupPropertyMap = cachedProperties.getOrDefault(locale, new ConcurrentHashMap<>());
        cachedProperties.put(locale, dataIdGroupPropertyMap);
        String dataIdGroupKey = StrUtil.format(KEY_TEMPLATE, locations.getGroup(), locations.getDataId());
        // 每次获取的时候都给一个新的Properties进行替换
        I18nJson properties = new I18nJson();
        dataIdGroupPropertyMap.put(dataIdGroupKey, properties);
        return properties;
    }

    public void removeLocation(I18nProperties.NacosI18nLocations locations, Locale locale){
        ConcurrentHashMap<String, I18nJson> dataIdGroupPropertyMap = cachedProperties.get(locale);
        if (dataIdGroupPropertyMap != null){
            String dataIdGroupKey = StrUtil.format(KEY_TEMPLATE, locations.getGroup(), locations.getDataId());
            dataIdGroupPropertyMap.remove(dataIdGroupKey);

        }
        if (CollUtil.isEmpty(dataIdGroupPropertyMap)){
            cachedProperties.remove(locale);
        }
    }

    private void loadProperties(ConfigService configService, I18nProperties.NacosI18nLocations nacosI18nLocation, Locale locale) throws Exception {
        String config = configService.getConfig(nacosI18nLocation.getDataId(), nacosI18nLocation.getGroup(), 5000);
        // 确保使用正确的字符编码
        loadPropertiesByConfig(config, nacosI18nLocation, locale);
        // 添加监听器，监听配置变化
        BmosRefreshScopeListener.BmosI18nListener bmosI18nListener = new BmosRefreshScopeListener.BmosI18nListener(locale, this, nacosI18nLocation.copy());
        configService.addListener(nacosI18nLocation.getDataId(), nacosI18nLocation.getGroup(), bmosI18nListener);
        I18nProperties.NacosI18nLocations nacosI18nLocationsCopy = nacosI18nLocation.copy();
        nacosPropertyLocaleMap.put(nacosI18nLocationsCopy, locale);
        bmosI18nListenerMap.put(nacosI18nLocationsCopy, bmosI18nListener);
    }

    @Override
    public void loadPropertiesByConfig(String config, I18nProperties.NacosI18nLocations nacosI18nLocation, Locale locale) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));
             InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8))
        {
            Properties properties = new Properties();
            I18nJson I18nJson = this.replaceProperties(nacosI18nLocation, locale);
            properties.load(reader);
            Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
            // 循环遍历键值对
            for (Map.Entry<Object, Object> entry : entrySet) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                I18nJson.put(key, value);
            }
        }
    }

}
