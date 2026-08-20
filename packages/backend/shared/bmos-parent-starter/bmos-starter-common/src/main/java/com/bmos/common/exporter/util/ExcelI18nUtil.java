package com.bmos.common.exporter.util;

import com.bmos.common.util.i18n.I18nUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ExcelI18nUtil {

    private static final String PREFIX = "EXCEL_";

    /**
     * 获取导入导出excel的head和sheetName的国际化值
     * 默认添加"EXCEL_"前缀
     * 当国际化未获取到时返回原值
     * @param value
     * @return
     */
    public static String getI18n(String value) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return value;
        }
        HttpServletRequest request = attributes.getRequest();
        return I18nUtils.getMenuMessage(PREFIX + value, value, null, request);
    }

}
