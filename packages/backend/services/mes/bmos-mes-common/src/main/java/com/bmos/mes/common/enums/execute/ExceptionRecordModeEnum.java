package com.bmos.mes.common.enums.execute;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 异常记录方式
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionRecordModeEnum implements CommonEnum<String> {

    MANUAL_RECORD("MANUAL_RECORD", "手动录入"),
    AUTO_RECORD("AUTO_RECORD","自动录入"),
    ;

    @EnumValue
    private final String value;

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ExceptionRecordModeEnum getEnumByValue(String value) {
        return Arrays.stream(ExceptionRecordModeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
