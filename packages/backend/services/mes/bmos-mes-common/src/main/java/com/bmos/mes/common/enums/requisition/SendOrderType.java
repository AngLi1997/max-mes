package com.bmos.mes.common.enums.requisition;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发料任务类型
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 18:11
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum SendOrderType implements CommonEnum<Integer> {

    /**
     * 按批次发料
     */
    BATCH(1, "按批次发料"),

    /**
     * 按货品发料
     */
    CARGO(2, "按货品发料");


    @EnumValue
    private final Integer value;

    private final String name;
}
