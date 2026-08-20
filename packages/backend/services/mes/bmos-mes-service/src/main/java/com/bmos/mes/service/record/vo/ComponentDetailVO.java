package com.bmos.mes.service.record.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ComponentDetailVO {

    /**
     * 组件详情
     */
    private String componentDetail;

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 记录项Id
     */
    private Long recordItemId;

    /**
     * fieldId
     */
    private Long fieldId;

}
