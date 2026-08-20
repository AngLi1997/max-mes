package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 任务录入方式
 * @Author: yigaohui
 * @Date: 2025/11/25 00:00
 */
@Getter
@AllArgsConstructor
public enum TaskInputMethodEnum {
    MANUAL("MANUAL", "手工录入"),
    INSTRUMENT("INSTRUMENT", "仪器导入"),
    CALCULATED("CALCULATED", "系统计算"),
    IMPORT("IMPORT", "文件导入"),
    API("API", "接口采集");

    @EnumValue
    private final String value;
    private final String label;

    public static String getLabel(TaskInputMethodEnum method) {
        return method == null ? null : method.getLabel();
    }
}

