package com.bmos.mes.common.enums.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货位日志操作类型枚举
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 10:14
 */
@AllArgsConstructor
@Getter
public enum StorageOperateTypeShowEnum {

    INBOUND("入库"),

    OUTBOUND("出库"),

    PLUS("盘增"),

    MINUS("盘减"),

    SEND_BACK("退库"),

    DESTROY("销毁"),

    USE("使用");

    private final String operate;

    public static StorageOperateTypeShowEnum getByName(String name) {
        for (StorageOperateTypeShowEnum value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
