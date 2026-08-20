package com.bmos.mes.common.enums.inpspect;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InspectResultCloseEnum implements CommonEnum<String> {
    PENDING("检验中", "PENDING"),
    FINISHED("结束", "FINISHED")
    ;

    private String name;

    private String value;

}
