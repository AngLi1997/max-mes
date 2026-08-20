package com.bmos.platform.common.enums.equipment;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.Getter;

/**
 * 设备操作日志填报状态枚举
 */
@Getter
public enum OperateLogFillingStatusEnum implements CommonEnum<Integer> {

    DEFAULT_STATUS(0, "默认状态"),

    INCOMPLETE_FILLING(1, "未完成填报"),
    COMPLETED(2, "完成填报"),
    ;
    @EnumValue
    private Integer code;

    private String name;

    OperateLogFillingStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
