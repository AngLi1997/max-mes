package com.bmos.lims2.server.eln.record.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class BigDecimalUtil {


    private static final String percentageFlag = "%";

    private static final Pattern PERCENTAGE_PATTERN = Pattern.compile("^(-?\\d+(\\.\\d+)?|(-?\\.\\d+))%$");

    private static final String upperE = "E";

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public static boolean isValidPercentage(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return PERCENTAGE_PATTERN.matcher(input.trim()).matches();
    }

    public static String toPercentageStr(BigDecimal number, Integer scale, RoundingMode roundingMode) {
        BigDecimal multiply = number.multiply(HUNDRED);
        if (scale != null) {
            return multiply.setScale(scale, roundingMode).toPlainString() + percentageFlag;
        }
        return multiply.toPlainString() + percentageFlag;
    }

    public static String toPercentageStr(BigDecimal number) {
        BigDecimal multiply = number.multiply(HUNDRED);
        return multiply.toPlainString() + percentageFlag;
    }

    /**
     * 将字符串转换为BigDecimal
     * 能够处理百分数 例如98% 转换为 0.98
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(String value) {
        // 判断是否是百分数
        if (isValidPercentage(value)) {
            String replace = value.replace(percentageFlag, StrUtil.EMPTY);
            return new BigDecimal(replace).divide(HUNDRED);
        }
        if (NumberUtil.isNumber(value)){
            return new BigDecimal(value);
        }
        throw new NumberFormatException();

    }

    /**
     * 将字符串转换为BigDecimal字符串
     * 能够处理百分数 例如98% 转换为 0.98
     * @param value
     * @return
     */
    public static String toBigDecimalStr(String value) {
        return toBigDecimal(value).toPlainString();
    }


    /**
     * 将 BigDecimal 转换为科学计数法表示
     * 不进行修约，保留所有有效数字
     *
     * @param value 输入的 BigDecimal 数值
     * @return 转换后的科学计数法表示字符串
     */
    public static String toScientific(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0"; // 特殊处理 0
        }

        // 计算指数
        int exponent = value.precision() - value.scale() - 1;
        BigDecimal scaledValue = value.movePointLeft(exponent); // 转为 [1, 10) 范围

        // 拼接科学计数法形式
        return scaledValue.stripTrailingZeros().toPlainString() + upperE + (exponent >= 0 ? "+" : "") + exponent;
    }

    /**
     * 将 BigDecimal 转换为指定有效数字和修约规则的科学计数法表示
     *
     * @param value             输入的 BigDecimal 数值
     * @param significantDigits 有效数字位数
     * @param roundingMode      修约规则
     * @return 转换后的科学计数法表示字符串
     */
    public static String toScientific(BigDecimal value, int significantDigits, RoundingMode roundingMode) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0"; // 特殊处理 0
        }

        // 计算指数
        int exponent = value.precision() - value.scale() - 1;
        BigDecimal scaledValue = value.movePointLeft(exponent); // 转为 [1, 10) 范围

        // 修约到指定有效数字
        BigDecimal roundedValue = scaledValue.setScale(significantDigits - 1, roundingMode);

        // 拼接科学计数法形式
        return roundedValue.toPlainString() + upperE + (exponent >= 0 ? "+" : "") + exponent;
    }

    public static boolean isValidNumber(String input) {
        if (isValidPercentage(input)) {
            return true;
        }
        return NumberUtil.isNumber(input);
    }

    public static BigDecimal getMax(BigDecimal... args) {
        return Arrays.stream(args).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(null);
    }

    public static BigDecimal getMin(BigDecimal... args) {
        return Arrays.stream(args).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(null);
    }

}
