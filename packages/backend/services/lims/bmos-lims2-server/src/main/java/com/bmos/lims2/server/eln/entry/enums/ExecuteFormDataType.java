package com.bmos.lims2.server.eln.entry.enums;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ExecuteFormDataType implements CommonEnum<String> {
    SAVE("录入", "save"),
    MODIFY("修订", "modify"),
    UPDATE("更新", "update"),
    ANNOTATION("批注", "annotation");

    private final String name;

    private final String value;

}
