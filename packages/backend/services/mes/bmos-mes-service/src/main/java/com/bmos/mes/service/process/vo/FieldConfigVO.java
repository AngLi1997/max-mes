package com.bmos.mes.service.process.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldConfigVO {

    private Long recordItemId;

    private Long recordVersionId;

    private Long fieldId;

    private Long procedureStepId;

    private Boolean reuse;

    private String componentType;
}
