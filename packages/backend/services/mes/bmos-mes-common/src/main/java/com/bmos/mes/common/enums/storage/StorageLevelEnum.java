package com.bmos.mes.common.enums.storage;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 暂存间等级枚举
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 15:38
 */
@AllArgsConstructor
@Getter
public enum StorageLevelEnum implements CommonEnum<Integer> {

    /**
     * 车间
     */
    WORKSHOP(1, "车间"),

    /**
     * 区域
     */
    AREA(2, "区域"),

    /**
     * 暂存间
     */
    STORAGE(3, "暂存间"),

    /**
     * 货位（其实不算是暂存间等级,但是又要和暂存间等级一起显示在树上）
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
