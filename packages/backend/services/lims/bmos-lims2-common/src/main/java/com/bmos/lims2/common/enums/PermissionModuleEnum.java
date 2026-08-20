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
public enum PermissionModuleEnum implements KeyValueEnum<String> {

    TEAM("TEAM", "班组"),
    REPORT_TEMPLATE("REPORT_TEMPLATE", "报告模板"),
    METHOD("METHOD", "检测方法"),
    OPERATE_RULE("OPERATE_RULE", "操作规程"),
    STABILITY_SCHEME("STABILITY_SCHEME", "稳定性方案"),
    STABILITY_INSPECT_PLAN("STABILITY_INSPECT_PLAN", "稳定性考察计划"),
    ;



    @EnumValue
    private final String value;

    private final String name;


}
