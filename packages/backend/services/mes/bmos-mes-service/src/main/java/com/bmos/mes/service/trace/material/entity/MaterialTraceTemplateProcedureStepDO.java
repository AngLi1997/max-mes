package com.bmos.mes.service.trace.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物料追溯物料关联工序步骤信息
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 10:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_material_trace_template_procedure_step")
public class MaterialTraceTemplateProcedureStepDO extends BaseDO {

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
     * 工艺版本
     */
    private String processVersion;

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
