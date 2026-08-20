package com.bmos.mes.service.trace.material.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料追溯类型
 * 消耗：生产投料、配料投入、配液投入、物料投入
 * 产出：中间品产出、配液产出
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 14:51
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MaterialTraceOperateType implements CommonEnum<Integer> {

    PRODUCT_INPUT(1, "生产投料", MaterialTraceType.CONSUME),
    INGREDIENT_INPUT(2, "配料投入", MaterialTraceType.CONSUME),
    PREPARATION_INPUT(3, "配液投入", MaterialTraceType.CONSUME),
    MATERIAL_INPUT(4, "物料投入", MaterialTraceType.CONSUME),
    MIDDLE_OUTPUT(5, "中间品产出", MaterialTraceType.OUTPUT),
    PREPARATION_OUTPUT(6, "配液产出", MaterialTraceType.OUTPUT),
    PRODUCT_OUTPUT(7, "成品产出", MaterialTraceType.OUTPUT);

    @EnumValue
    private final Integer value;

    private final String name;

    private final MaterialTraceType traceType;
}
