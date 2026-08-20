package com.bmos.platform.service.util;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
}
