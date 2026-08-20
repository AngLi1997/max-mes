package com.bmos.mes.common.enums.inpspect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InspectProgramResultEnum implements CommonEnum<String> {

    QUALIFIED("合格", "QUALIFIED"),
    UNQUALIFIED("不合格", "UNQUALIFIED"),

    ;
    private String name;
    @EnumValue
    private String value;

}
