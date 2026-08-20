package com.bmos.mes.common.enums.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductPlanInstructStatusEvent  {

    WAIT_CONFIRM("WAIT_CONFIRM", "分解"),

    CONFIRM("CONFIRM", "确认"),
    SEND("SEND", "下发"),

    ;

    private final String name;

    private final String message;
}
