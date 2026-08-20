package com.bmos.mes.service.dataset.util;

import java.util.regex.Pattern;

/**
 * 占位符常量
 * @author liang
 * @version 1.0.0
 * @date 2024/9/2 16:32
 */
public final class PlaceholderConstants {

    // 占位符正则匹配表达式
    public static final Pattern PATTERN = Pattern.compile("\\$\\{\\((.\\d*\\.\\d*[^)]+)\\)(\\[\\d*]){4,5}}");
}
