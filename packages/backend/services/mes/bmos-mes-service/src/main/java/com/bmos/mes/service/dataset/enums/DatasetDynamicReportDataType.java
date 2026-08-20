package com.bmos.mes.service.dataset.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据集动态填报类型
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:41
 */
@Getter
@AllArgsConstructor
public enum DatasetDynamicReportDataType implements CommonEnum<String> {

    /**
     * 数值
     */
    NUMBER("NUMBER", "数值"),

    /**
     * 文本
     */
    TEXT("TEXT", "文本"),

    /**
     * 日期
     */
    DATE("DATE", "日期");


    @EnumValue
    private final String value;

    private final String name;
}
