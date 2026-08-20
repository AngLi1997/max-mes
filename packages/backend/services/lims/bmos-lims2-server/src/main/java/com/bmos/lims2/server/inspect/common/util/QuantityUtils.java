package com.bmos.lims2.server.inspect.common.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @Description: 数量解析与格式化工具
 * @Author: yigaohui
 * @Date: 2025/10/13 10:00
 */
public final class QuantityUtils {

    private static final Pattern QUANTITY_PATTERN = Pattern.compile("^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$");

    private QuantityUtils() {}

    public static boolean isValidQuantityString(String value) {
        if (value == null) return false;
        return QUANTITY_PATTERN.matcher(value.trim()).matches();
    }

    public static BigDecimal toBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return new BigDecimal(value.trim());
    }

    public static String toCanonicalString(BigDecimal value) {
        if (value == null) return null;
        // 去除不必要的尾随0，避免前端看到多余0
        return value.stripTrailingZeros().toPlainString();
    }
}


