package com.bmos.mes.common.enums.inpspect;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InspectStorageMaterialCodeEnum implements CommonEnum<String> {
    HYDRATION("水分" ,"hydration"),
    NO_HYDRATION_CONTENT("无水含量(%)", "noHydrationContent");

    private String name;

    private String value;

}
