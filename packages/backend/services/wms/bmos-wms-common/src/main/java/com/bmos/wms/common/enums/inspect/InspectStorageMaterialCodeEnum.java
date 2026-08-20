package com.bmos.wms.common.enums.inspect;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LIMS 检验项 alreadyConvertProgramNo 中用于回写水分 / 无水含量的固定 code（与 MES 同）。
 */
@Getter
@AllArgsConstructor
public enum InspectStorageMaterialCodeEnum implements CommonEnum<String> {

    HYDRATION("水分", "hydration"),
    NO_HYDRATION_CONTENT("无水含量(%)", "noHydrationContent"),
    ;

    private final String name;
    private final String value;
}
