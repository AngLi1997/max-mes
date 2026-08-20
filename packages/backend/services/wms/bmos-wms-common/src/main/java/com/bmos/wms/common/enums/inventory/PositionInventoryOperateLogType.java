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
public enum PositionInventoryOperateLogType implements KeyValueEnum<Integer> {

    /**
     * 入库
     */
    INBOUND(1, new PositionInventoryOperateType[]{PositionInventoryOperateType.INBOUND, PositionInventoryOperateType.INBOUND}, new String[]{"货品入库-接收", "货品入库-递交"}),

    /**
     * 出库
     */
    OUTBOUND(2, new PositionInventoryOperateType[]{PositionInventoryOperateType.OUTBOUND, PositionInventoryOperateType.OUTBOUND}, new String[]{"货品出库-发放", "货品出库-领用"}),


    /**
     * 移库
     */
    MOVE(3, new PositionInventoryOperateType[]{PositionInventoryOperateType.OUTBOUND, PositionInventoryOperateType.INBOUND}, new String[]{"货品移库-移出", "货品移库-移入"}),

    /**
     * 发料
     */
    SEND_OUT(4, new PositionInventoryOperateType[]{PositionInventoryOperateType.OUTBOUND, PositionInventoryOperateType.OUTBOUND}, new String[]{"货品出库-发料", "货品出库-复核"}),

    /**
     * 盘点
     */
    CHECK_PLUS(6, new PositionInventoryOperateType[]{PositionInventoryOperateType.CHECK_PLUS, PositionInventoryOperateType.CHECK_PLUS}, new String[]{"货品盘点-盘增", "货品盘点-复核"}),

    /**
     * 盘减
     */
    CHECK_MINUS(7, new PositionInventoryOperateType[]{PositionInventoryOperateType.CHECK_MINUS, PositionInventoryOperateType.CHECK_MINUS}, new String[]{"货品盘点-盘减", "货品盘点-复核"});

    @EnumValue
    private final Integer value;

    private final PositionInventoryOperateType[] operateType;

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
