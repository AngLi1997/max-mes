package com.bmos.lims2.common.constants;


import cn.hutool.core.util.StrUtil;

/**
 * 基础数据 检品常量
 */
public interface MaterialConstants {

    /**
     * 物料分类树默认排序字段
     */
    String DEFAULT_CATEGORY_QUERY_SORT = "merge_code DESC";

    /**
     * 展示名称默认分割符
     */
    String CATEGORY_SHOW_SPLIT = StrUtil.DASHED;
}
