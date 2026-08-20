package com.bmos.mes.service.process.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ProcessRecordRelationDTO {

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 批记录id
     */
    private Long batchRecordItemId;

}
