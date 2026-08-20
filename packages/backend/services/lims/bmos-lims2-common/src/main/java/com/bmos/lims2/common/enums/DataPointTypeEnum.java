package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Period;
import java.util.Arrays;

/**
 * 数据点类型枚举
 *
 * @author makejava
 * @since 2025-01-28 10:00:00
 */
@AllArgsConstructor
@Getter
public enum DataPointTypeEnum implements KeyValueEnum<String> {

    /**
     * 数值类型
     */
    NUMBER("NUMBER", "数值"),

    /**
     * 文本类型
     */
    TEXT("TEXT", "文本"),

    /**
     * 选项类型
     */
    OPTION("OPTION", "选项"),


    DATE("DATE", "日期"),

    /**
     * 时间类型
     */
    TIME("TIME", "时间");

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