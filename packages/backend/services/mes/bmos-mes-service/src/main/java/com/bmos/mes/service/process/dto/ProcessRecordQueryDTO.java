package com.bmos.mes.service.process.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessRecordQueryDTO {

    private Long processId;

    private String processVersion;

    private Long recordId;

    private Boolean reuse;

}
