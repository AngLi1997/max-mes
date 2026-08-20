package com.bmos.lims2.server.eln.entry.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldConfigVO {

    private Long recordId;

    private Long recordVersionId;

    private Long recordItemId;

    private Long fieldId;

    private Long inspectParameterId;

    private Long inspectParameterConfigId;

    private String componentType;
}
