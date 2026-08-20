package com.bmos.wms.common.enums.inventory;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货品日志类型
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 13:47
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum CargoInventoryOperateLogType implements KeyValueEnum<Integer> {

    /**
     * 入库
     */
    INBOUND(1, new CargoInventoryOperateType[]{CargoInventoryOperateType.INBOUND, CargoInventoryOperateType.INBOUND}, new String[]{"货品入库-接收", "货品入库-递交"}),

    /**
     * 出库
     */
    OUTBOUND(2, new CargoInventoryOperateType[]{CargoInventoryOperateType.OUTBOUND, CargoInventoryOperateType.OUTBOUND}, new String[]{"货品出库-发放", "货品出库-领用"}),

    /**
     * 盘点
     */
    CHECK(3, new CargoInventoryOperateType[]{CargoInventoryOperateType.CHECK, CargoInventoryOperateType.CHECK}, new String[]{"货品盘点-盘点", "货品盘点-复核"}),

    /**
     * 移库
     */
    MOVE(4, new CargoInventoryOperateType[]{CargoInventoryOperateType.OUTBOUND, CargoInventoryOperateType.INBOUND}, new String[]{"货品移库-移出", "货品移库-移入"}),

    /**
     * 发料
     */
    SEND_OUT(5, new CargoInventoryOperateType[]{CargoInventoryOperateType.OUTBOUND, CargoInventoryOperateType.OUTBOUND}, new String[]{"货品出库-发料", "货品出库-复核"}),

    /**
     * 新增货品
     */
    ADD(6, new CargoInventoryOperateType[]{CargoInventoryOperateType.ADD}, new String[]{"新增货品"});

    @EnumValue
    private final Integer value;

    private final CargoInventoryOperateType[] operateType;

    private final String[] operateInfo;

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
