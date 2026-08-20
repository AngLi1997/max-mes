package com.bmos.wms.common.enums.sendout;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发料任务状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 18:11
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum SendOrderStatus implements KeyValueEnum<Integer> {

    PENDING(0, "待发料"),

    FINISHED(1, "已发料"),

    CANCELED(2, "已取消");

    @EnumValue
    private final Integer value;

    private final String name;
}
