package com.bmos.platform.facade.factory.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 房间状态枚举
 */
@Getter
@AllArgsConstructor
public enum RoomStatusEnum implements CommonEnum<Integer> {


    /**
     * 在用
     */
    OCCUPATION(1, "在用"),
    /**
     * 待清场
     */
    BE_CLEANED(2, "待清场"),
    /**
     * 已清场
     */
    CLEANED(3, "已清场"),
    ;

    /**
     * 状态code
     */
    @EnumValue
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;


    @Override
    public String getName() {
        return getDesc();
    }

    @Override
    public Integer getValue() {
        return getCode();
    }
}
