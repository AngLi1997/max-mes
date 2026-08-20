package com.bmos.lims2.server.eln.record.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ComponentDetailDTO {

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 工艺版本记录id
     */
    private Long recordItemId;

    /**
     * 字段id
     */
    private Long fieldId;

}
