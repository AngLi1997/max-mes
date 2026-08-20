package com.bmos.mes.service.execute.enums;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ExecuteFormDataType implements CommonEnum<String> {
    SAVE("录入", "save"),
    MODIFY("修订", "modify"),
    UPDATE("更新", "update");

    private final String name;

    private final String value;

}
