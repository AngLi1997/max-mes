package resource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.bmos.common.constant.RequestConstant;
import com.bmos.common.exception.BmosException;
import enums.RequestTypeEnum;
import error.I18nResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import property.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MessageResourceRegister implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(MessageResourceRegister.class);

    private final Map<RequestTypeEnum, I18nProperties> propertiesMap = new HashMap<>();

    private final Map<RequestTypeEnum, I18nMessageResource> messageResourceMap = new HashMap<>();

    private final Map<RequestTypeEnum, I18nPropertyMethod> propertyMethodMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        registerMessageResource(applicationContext);
        initMessageResource(applicationContext);
    }

    public void propertyChange(){
        for (I18nMessageResource messageResource : messageResourceMap.values()) {
            messageResource.propertyChange();
        }
    }

    private void initMessageResource(ApplicationContext applicationContext) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>MessageResourceRegister init...");
        NacosConfigManager nacosConfigManager = applicationContext.getBean(NacosConfigManager.class);
        for (RequestTypeEnum requestTypeEnum : propertiesMap.keySet()) {
            try {
                propertyMethodMap.get(requestTypeEnum).register(propertiesMap.get(requestTypeEnum), nacosConfigManager);
                messageResourceMap.get(requestTypeEnum).initMessageSource();
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>MessageResourceRegister init complete.");
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>>>>>>>>MessageResourceRegister init error.", e);
                throw new BmosException(I18nResponseItem.RESOURCE_INIT_LOAD_ERROR);
            }
        }
    }

    private void registerMessageResource(ApplicationContext applicationContext) {
        BmosMessageResource bmosMessageResource = applicationContext.getBean(BmosMessageResource.class);
        FrontWebI18nMessageResource frontWebI18NMessageResource = applicationContext.getBean(FrontWebI18nMessageResource.class);
        BmosI18nProperties bmosI18nProperties = applicationContext.getBean(BmosI18nProperties.class);
        FrontWebI18nProperties frontWebI18NProperties = applicationContext.getBean(FrontWebI18nProperties.class);
        FrontAppI18nMessageResource frontAppI18NMessageResource = applicationContext.getBean(FrontAppI18nMessageResource.class);
        FrontAppI18nProperties frontAppI18NProperties = applicationContext.getBean(FrontAppI18nProperties.class);

        propertiesMap.put(RequestTypeEnum.BACKEND, bmosI18nProperties);
        messageResourceMap.put(RequestTypeEnum.BACKEND, bmosMessageResource);
        propertyMethodMap.put(RequestTypeEnum.BACKEND, bmosMessageResource);

        propertiesMap.put(RequestTypeEnum.FRONTEND_WEB, frontWebI18NProperties);
        messageResourceMap.put(RequestTypeEnum.FRONTEND_WEB, frontWebI18NMessageResource);
        propertyMethodMap.put(RequestTypeEnum.FRONTEND_WEB, frontWebI18NMessageResource);

        propertiesMap.put(RequestTypeEnum.FRONTEND_APP, frontAppI18NProperties);
        messageResourceMap.put(RequestTypeEnum.FRONTEND_APP, frontAppI18NMessageResource);
        propertyMethodMap.put(RequestTypeEnum.FRONTEND_APP, frontAppI18NMessageResource);
    }

    /**
     * 加载对应资源
     * @param request
     * @return
     */
    public I18nJson getAllProperties(HttpServletRequest request) {
        String requestResource = RequestConstant.RESOURCE;
        RequestTypeEnum requestTypeEnum = RequestTypeEnum.getRequestTypeEnumByCode(request.getHeader(requestResource));
        I18nPropertyMethod i18nPropertyMethod = propertyMethodMap.get(requestTypeEnum);
        if (i18nPropertyMethod == null){
            throw new BmosException(I18nResponseItem.RESOURCE_NO_REGISTER);
        }
        I18nPropertyMethod propertyMethod = propertyMethodMap.get(requestTypeEnum);
        if (propertyMethod == null){
            throw new BmosException(I18nResponseItem.RESOURCE_NO_REGISTER);
        }
        return propertyMethodMap.get(requestTypeEnum).loadAllProperties(request);
    }


}
