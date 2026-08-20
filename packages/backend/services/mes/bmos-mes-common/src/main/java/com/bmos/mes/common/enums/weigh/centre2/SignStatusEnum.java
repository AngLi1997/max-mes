package com.bmos.mes.common.enums.weigh.centre2;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignStatusEnum implements CommonEnum<Integer> {
    SIGNED(1, "已签名"),
    UNSIGNED(2, "未签名");

    @EnumValue
    private final Integer value;
    private final String name;
} 