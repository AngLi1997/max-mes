package com.bmos.mes.service.record.business.model;

import lombok.Data;

@Data
public class ProcessDetailInfo {

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 工艺版本号
     */
    private String processVersion;

    /**
     * 配方版本id
     */
    private Long formulaVersionId;

}
