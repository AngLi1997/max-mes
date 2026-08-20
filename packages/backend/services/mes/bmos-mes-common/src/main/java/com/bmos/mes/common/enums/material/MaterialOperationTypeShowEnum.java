package com.bmos.mes.common.enums.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料日志操作类型枚举
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 10:14
 */
@Getter
@AllArgsConstructor
public enum MaterialOperationTypeShowEnum {

    INBOUND("入库"),
    OUTBOUND("出库"),
    CHECK("盘点"),
    RESERVE("预定"),
    CANCEL_RESERVE("取消预定"),
    WEIGH("称量"),
    ADD("新增"),
    CHARGE("投料"),
    RECYCLE("回收"),
    MEASURE("量取"),
    SEND_BACK("退库"),
    DESTROY("销毁"),
    USE("使用"),
    OUTPUT("产出")
    ;

    private final String operate;

    public static MaterialOperationTypeShowEnum getByName(String name) {
        for (MaterialOperationTypeShowEnum value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
