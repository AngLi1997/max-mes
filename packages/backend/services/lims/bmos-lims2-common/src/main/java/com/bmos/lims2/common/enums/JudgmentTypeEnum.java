package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 判定类型枚举
 *
 * @author makejava
 * @since 2025-01-28 10:00:00
 */
@AllArgsConstructor
@Getter
public enum JudgmentTypeEnum implements KeyValueEnum<String> {

    /**
     * 范围判定
     */
    RANGE("RANGE", "范围判定"),

    /**
     * 等于
     */
    EQUAL("EQUAL", "等于"),

    /**
     * 选项包含
     */
    CONTAINS("CONTAINS", "包含"),

    /**
     * 选项不包含
     */
    NOT_CONTAINS("NOT_CONTAINS", "不包含");

    /**
     * 类型编码（数据库存储值）
     */
    @EnumValue
    private final String value;

    /**
     * 类型描述
     */
    private final String name;
} 