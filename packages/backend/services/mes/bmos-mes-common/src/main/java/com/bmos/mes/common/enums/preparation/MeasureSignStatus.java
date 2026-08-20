package com.bmos.mes.common.enums.preparation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量签名状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 20:55
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MeasureSignStatus implements CommonEnum<Integer> {

    /**
     * 未签名
     */
    UN_SIGNED(0, "未签名"),

    /**
     * 已签名
     */
    SIGNED(1, "已签名"),

    /**
     * 已作废
     */
    SCRAPED(2, "已作废");

    @EnumValue
    private final Integer value;

    private final String name;

}
