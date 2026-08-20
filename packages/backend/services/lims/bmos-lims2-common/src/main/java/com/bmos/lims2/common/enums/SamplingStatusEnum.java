package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 取样状态枚举
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@AllArgsConstructor
public enum SamplingStatusEnum implements KeyValueEnum<String> {

    PENDING_SAMPLING("PENDING_SAMPLING", "待取样"),
    PARTIAL_SAMPLING("PARTIAL_SAMPLING", "部分取样"),
    SAMPLING_COMPLETED("SAMPLING_COMPLETED", "取样完成");

    @EnumValue
    private final String value;
    private final String name;
}