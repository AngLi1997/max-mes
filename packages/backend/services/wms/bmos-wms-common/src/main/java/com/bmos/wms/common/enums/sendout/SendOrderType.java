package com.bmos.wms.common.enums.sendout;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发料任务类型
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 18:11
 */
@Getter
@AllArgsConstructor
public enum SendOrderType implements KeyValueEnum<Integer> {

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

    public static SendOrderType getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (SendOrderType sendOrderType : SendOrderType.values()) {
            if (sendOrderType.getValue().equals(value)) {
                return sendOrderType;
            }
        }
        return null;
    }
}
