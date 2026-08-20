package com.bmos.mes.common.enums.ingredient;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/17 17:51
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IngredientWeighStatus implements CommonEnum<Integer> {

    /**
     * 未称量
     */
    PENDING(0, "未称量"),

    /**
     * 称量中
     */
    PROCESSING(1, "称量中"),

    /**
     * 已完成
     */
    FINISHED(2, "已完成");

    @EnumValue
    private final Integer value;

    private final String name;
}
