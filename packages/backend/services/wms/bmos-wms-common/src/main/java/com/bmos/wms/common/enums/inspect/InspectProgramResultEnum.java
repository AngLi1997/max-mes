package com.bmos.wms.common.enums.inspect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 检验项结论（与 MES InspectProgramResultEnum 同语义）。
 */
@Getter
@AllArgsConstructor
public enum InspectProgramResultEnum implements CommonEnum<String> {

    QUALIFIED("合格", "QUALIFIED"),
    UNQUALIFIED("不合格", "UNQUALIFIED"),

    ;

    private final String name;
    @EnumValue
    private final String value;
}
