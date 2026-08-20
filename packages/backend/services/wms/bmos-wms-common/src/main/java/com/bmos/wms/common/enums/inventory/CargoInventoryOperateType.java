package com.bmos.wms.common.enums.inventory;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货品日志操作
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 13:47
 */
@Getter
@AllArgsConstructor
public enum CargoInventoryOperateType implements KeyValueEnum<Integer> {

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
     * 盘点
     */
    CHECK(4, "盘点");

    @EnumValue
    private final Integer value;

    private final String name;
}
