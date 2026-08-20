package com.bmos.wms.common.enums.inventory;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货位日志操作
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 13:47
 */
@Getter
@AllArgsConstructor
public enum PositionInventoryOperateType implements KeyValueEnum<Integer> {

    /**
     * 入库
     */
    INBOUND(1, "入库"),

    /**
     * 出库
     */
    OUTBOUND(2, "出库"),

    /**
     * 新增
     */
    ADD(3, "新增"),

    /**
     * 盘增
     */
    CHECK_PLUS(4, "盘增"),

    /**
     * 盘减
     */
    CHECK_MINUS(5, "盘减");

    @EnumValue
    private final Integer value;

    private final String name;
}
