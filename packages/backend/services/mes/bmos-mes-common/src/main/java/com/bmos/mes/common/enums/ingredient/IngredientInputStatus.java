package com.bmos.mes.common.enums.ingredient;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配料投料状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 20:55
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IngredientInputStatus implements CommonEnum<Integer> {

    /**
     * 待投料
     */
    PENDING(1, "待投料"),

    /**
     * 投料中
     */
    PROCESSING(2, "投料中"),

    /**
     * 已投料
     */
    FINISHED(3, "已投料"),

    /**
     * 已失效
     */
    SCRAPED(4, "已失效");

    @EnumValue
    private final Integer value;

    private final String name;

}
