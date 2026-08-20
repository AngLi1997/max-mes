package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 检验单状态枚举（请验阶段）
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@AllArgsConstructor
public enum InspectionOrderStatusEnum implements KeyValueEnum<String> {

    PENDING_CONFIRM("PENDING_CONFIRM", "待确认"),
    CONFIRMED("CONFIRMED", "已确认"),
    TERMINATED("TERMINATED", "已终止"),
    // 样品审核相关状态
    SAMPLE_AUDIT_PENDING("SAMPLE_AUDIT_PENDING", "样品待审核"),
    SAMPLE_AUDIT_REJECTED("SAMPLE_AUDIT_REJECTED", "样品审核未通过"),
    // 检验完成（样品审核通过即视为检验完成）
    COMPLETED("COMPLETED", "已完成");
    @EnumValue
    private final String value;
    private final String name;


}