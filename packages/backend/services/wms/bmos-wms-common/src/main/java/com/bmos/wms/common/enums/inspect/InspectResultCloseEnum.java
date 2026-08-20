package com.bmos.wms.common.enums.inspect;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LIMS 回传中表征"是否最后一次"的枚举（与 MES InspectResultCloseEnum 同语义）。
 */
@Getter
@AllArgsConstructor
public enum InspectResultCloseEnum implements CommonEnum<String> {

    PENDING("检验中", "PENDING"),
    FINISHED("结束", "FINISHED"),
    ;

    private final String name;
    private final String value;
}
