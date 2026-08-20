package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报告验证任务状态
 */
@Getter
@AllArgsConstructor
public enum ReportGenerateStatusEnum implements KeyValueEnum<String> {

    PENDING("PENDING", "待生成"),
    RUNNING("RUNNING", "生成中"),
    SUCCESS("SUCCESS", "生成成功"),
    FAILED("FAILED", "生成失败");

    @EnumValue
    private final String value;
    private final String name;
}


