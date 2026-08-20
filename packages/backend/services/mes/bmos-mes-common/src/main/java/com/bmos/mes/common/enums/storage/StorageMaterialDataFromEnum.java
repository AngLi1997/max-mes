package com.bmos.mes.common.enums.storage;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料件数据来源
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 16:00
 */
@AllArgsConstructor
@Getter
public enum StorageMaterialDataFromEnum implements CommonEnum<Integer> {

    INBOUND(1, "物料入库"),

    SEND_BACK(2, "物料入库");

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
}
