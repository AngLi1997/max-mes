package com.bmos.wms.common.enums.inventory;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 存储区域等级枚举
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 15:38
 */
@AllArgsConstructor
@Getter
public enum StorageLevelEnum implements KeyValueEnum<Integer> {

    /**
     * 车间
     */
    WORKSHOP(1, "车间"),

    /**
     * 区域
     */
    AREA(2, "区域"),

    /**
     * 存储区域
     */
    STORAGE(3, "存储区域"),

    /**
     * 货位（其实不算是存储区域等级,但是又要和存储区域等级一起显示在树上）
     */
    POSITION(4, "货位");

    @EnumValue
    private final Integer value;

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public static StorageLevelEnum increaseLevel(StorageLevelEnum level) {
        if (level == null) {
            return null;
        }
        if (level == POSITION) {
            return POSITION;
        }
        return StorageLevelEnum.values()[level.ordinal() + 1];
    }
}
