package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AnalyzeResultTypeEnum implements KeyValueEnum<String> {

    NUMBER("NUMBER", "数值"),
    TEXT("TEXT", "文本"),
    OPTION("OPTION", "选项"),
    DATE("DATE", "日期"),
    TIME("TIME", "时间");

    @EnumValue
    private final String value;

    private final String name;
}
