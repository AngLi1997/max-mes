package com.bmos.mes.service.trace.material.vo;

import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import lombok.Data;

/**
 * 物料追溯物料关联工序步骤信息
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 10:01
 */
@Data
public class MaterialTraceTemplateProcedureStepVO {

    /**
     * 物料追溯物料关联id
     */
    private Long relationId;

    /**
     * 物料追溯模板id
     */
    private Long templateId;

    /**
     * 物料追溯物料id
     */
    private Long materialId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 工序步骤id
     */
    private Long procedureStepId;

    /**
     * 工序步骤名称
     */
    private String procedureStepName;

    /**
     * 物料追溯类型
     */
    private MaterialTraceType traceType;
}
