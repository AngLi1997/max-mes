package com.bmos.mes.common.enums.ingredient;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料投入状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 20:55
 */
@Getter
@AllArgsConstructor
public enum WeighInputStatus implements CommonEnum<Integer> {

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
    SCRAPED(4, "已失效"),

    /**
     * 未签名
     */
    UN_SIGNED(6, "未签名");

    @EnumValue
    private final Integer value;

    private final String name;

}
