package com.bmos.lims2.server.util;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.bmos.mybatis.page.BasePage;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
public class PageUtils {
    public static String getOrderByOrDefaultByUpdateTimeDesc(BasePage basePage) {
        if (StringUtils.isBlank(basePage.getOrderBy())) {
            return "update_time ,id desc";
        } else {
            return basePage.getOrderSql();
        }
    }

    public static String getOrderByOrDefaultByCreateTimeDesc(BasePage basePage) {
        if (StringUtils.isBlank(basePage.getOrderBy())) {
            return "create_time desc ,id desc";
        } else {
            return basePage.getOrderSql();
        }
    }
}
