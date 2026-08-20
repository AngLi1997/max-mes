package com.bmos.common.base.enums;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

public interface CommonEnum<T> extends KeyValueEnum<T> {

    Logger log = LoggerFactory.getLogger(CommonEnum.class);

    public static <E extends CommonEnum<?>> E getEnumByName(Class<E> clazz, Object name) {
        return Arrays.stream(clazz.getEnumConstants())
            .filter(codeEnum -> Objects.equals(codeEnum.getName(), name))
            .findFirst()
            .orElse(null);
    }

    public static <E extends CommonEnum<?>> E getEnumByValue(Class<E> clazz, Object message) {
        return Arrays.stream(clazz.getEnumConstants())
            .filter(codeEnum -> Objects.equals(codeEnum.getValue(), message))
            .findFirst()
            .orElse(null);
    }

    public static <E extends KeyValueEnum<?>> E getKeyValueEnumByValue(Class<E> clazz, String stringValue) {
        if (StrUtil.isBlank(stringValue)){
            return null;
        }
        return Arrays.stream(clazz.getEnumConstants())
                .filter(codeEnum -> Objects.equals(codeEnum.getValue().toString(), stringValue))
                .findFirst()
                .orElse(null);
    }
}
