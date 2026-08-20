package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 检验方案版本状态枚举
 *
 * @author makejava
 * @since 2025-01-28 10:00:00
 */
@AllArgsConstructor
@Getter
public enum InspectionSchemeVersionStatusEnum implements KeyValueEnum<String> {

    /**
     * 编辑中
     */
    EDITING("EDITING", "编辑中"),

    /**
     * 审批中
     */
    APPROVING("APPROVING", "审批中"),

    /**
     * 生效
     */
    ACTIVE("ACTIVE", "生效"),

    /**
     * 已完成
     */
    COMPLETED("COMPLETED", "已完成"),

    /**
     * 失效
     */
    INACTIVE("INACTIVE", "失效"),

    /**
     * 作废
     */
    VOIDED("VOIDED", "作废");

    /**
     * 状态编码（数据库存储值）
     */
    @EnumValue
    private final String value;

    /**
     * 状态描述
     */
    private final String name;
} 