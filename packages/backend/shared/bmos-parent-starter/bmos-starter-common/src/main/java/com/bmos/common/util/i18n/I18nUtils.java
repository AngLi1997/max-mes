package com.bmos.common.util.i18n;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.common.constant.RequestConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.bmos.common.constant.I18nConstant.RESPONSE_MESSAGE_PREFIX;

public class I18nUtils {

    public static String ZH_CN = "zh_CN";
    private static final Map<String, Locale> localeCache = new ConcurrentHashMap<>();
    private final static Logger log = LoggerFactory.getLogger(I18nUtils.class);

    private static final String DEFAULT_LANGUAGE_KEY = "i18n.default-language";

    /**
     * @param code    响应码
     * @param args    todo 拼接
     * @param request 请求
     * @return 国际化消息
     */
    public static String getResponseMessage(int code, String defaultMessage, Object[] args,
                                            HttpServletRequest request) {
        String result;
        try {
            Object[] i18nArgs = null;
            if (Objects.nonNull(args)){
                i18nArgs = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof String){
                        if (NumberUtil.isNumber((String)arg)){
                            i18nArgs[i] = getResponseMessage(Integer.parseInt((String)arg), (String)arg, null, request);
                        } else {
                            i18nArgs[i] = getMenuMessage((String) arg, (String) arg, null, request);
                        }
                    } else {
                        i18nArgs[i] = arg;
                    }
                }
            }
            result = SpringUtil.getBean(MessageSource.class).getMessage(RESPONSE_MESSAGE_PREFIX + code, i18nArgs,
                    getLocale(request));
        } catch (NoSuchMessageException e) {
            result = MessageFormat.format(defaultMessage, args);
        }
        return result;
    }

    public static String getMenuMessage(String code, String defaultMessage, Object[] args,
                                        HttpServletRequest request){
        String result;
        try {
            result = SpringUtil.getBean(MessageSource.class).getMessage(code, args,
                    getLocale(request));
        } catch (NoSuchMessageException e) {
            result = MessageFormat.format(defaultMessage, args);
        }
        return result;
    }

    public static String getCodeMessage(String code, String defaultMessage, Object[] args){
        String result;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = null;
            if (Objects.nonNull(attributes)) {
                request = attributes.getRequest();
            }
            result = SpringUtil.getBean(MessageSource.class).getMessage(code, args,
                    getLocale(request));
        } catch (NoSuchMessageException e) {
            result = MessageFormat.format(defaultMessage, args);
        }
        return result;
    }


    /**
     * @param code 响应码
     * @param args
     * @return 国际化消息
     */
    public static String getResponseMessage(int code, String defaultMessage, Object ... args) {
        String result;
        try {
            result = SpringUtil.getBean(MessageSource.class).getMessage(RESPONSE_MESSAGE_PREFIX + code, args,
                    LocaleUtil.getCurrentLocale());
        } catch (NoSuchMessageException e) {
            result = MessageFormat.format(defaultMessage, args);
        }
        return result;
    }

    public static Locale getLocale(HttpServletRequest request) {
        // 如果为空 使用spring util获取application.yaml中的默认配置
        String lang;
        if (request == null) {
            lang = SpringUtil.getProperty(DEFAULT_LANGUAGE_KEY);
        } else {
            lang = request.getHeader(RequestConstant.LANGUAGE);
        }
        if (StrUtil.isEmpty(lang)) {
            lang = ZH_CN;
        }
        Locale locale = localeCache.get(lang);
        if (null == locale) {
            locale = getLocale(lang);
            localeCache.put(lang, locale);
        }
        return locale;
    }

    public static Locale getLocale(String lang) {
        List<String> chars = StrUtil.split(lang, StrUtil.C_UNDERLINE, 2);
        return new Locale(chars.get(0), chars.get(1));
    }

    public static String getEnumMessage(KeyValueEnum commonEnum){
        try{
            if (Objects.isNull(commonEnum)){
                return null;
            }
            // 获取HttpServletRequest
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.isNull(attributes)) {
                // 代表非前端请求 不需要进行国际话
                return commonEnum.getName();
            }
            String simpleName = commonEnum.getClass().getSimpleName();
            String fieldName = (String) commonEnum.getClass().getMethod("name").invoke(commonEnum);
            String i18nKey = simpleName + "." + fieldName;
            HttpServletRequest request = attributes.getRequest();
            return I18nUtils.getMenuMessage(i18nKey, commonEnum.getName(), null, request);
        } catch (Exception e){
            log.error("获取国际化信息失败:{}", e.getMessage());
        }
        return commonEnum.getName();
    }


}
