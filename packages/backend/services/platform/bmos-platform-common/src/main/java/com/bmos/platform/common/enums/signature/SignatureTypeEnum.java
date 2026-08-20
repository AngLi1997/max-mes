package com.bmos.platform.common.enums.signature;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignatureTypeEnum implements CommonEnum<Integer> {
    PASSWORD_AUTH(0,"密码认证");

    @EnumValue
    private final Integer value;

    private final String name;

    public static SignatureTypeEnum getByValue(Integer value) {
        for (SignatureTypeEnum item : values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

}
