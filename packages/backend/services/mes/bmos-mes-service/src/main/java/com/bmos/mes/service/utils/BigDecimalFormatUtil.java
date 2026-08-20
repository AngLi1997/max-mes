package com.bmos.mes.service.utils;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/27 18:51
 */
public class BigDecimalFormatUtil {

    public static String formatBigDecimal(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return "0";
        }
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    private static void printEnumValues(Reflections reflections) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获取所有实现CommonEnum接口的类
        Set<Class<? extends CommonEnum>> enumClasses = reflections.getSubTypesOf(CommonEnum.class);

        // 过滤出枚举类
        for (Class<? extends CommonEnum> enumClass : enumClasses) {
            if (enumClass.isEnum()) {
                String simpleName = enumClass.getSimpleName();
                CommonEnum[] enumConstants = enumClass.getEnumConstants();
                for (int i = 0; i < enumConstants.length; i++) {
                    CommonEnum commonEnum = enumConstants[i];
                    System.out.println(simpleName+"."+enumClass.getMethod("name").invoke(commonEnum) + "=" + commonEnum.getName());
                }
            }
        }
    }

    public static BigDecimal convertBigDecimal(String str) {
        if (str == null || StrUtil.isEmpty(str) || !isNumber(str)) {
            return null;
        }
        return new BigDecimal(str);
    }

    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
