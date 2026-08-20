package com.bmos.mes.service.process.vo;

import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import lombok.Data;

/**
 * 工序步骤vo（包含工序/工艺信息）
 * @author liang
 * @version 1.0.0
 * @date 2024/11/20 09:59
 */
@Data
public class ProcedureStepTraceVO {

    /**
     * 工序步骤id
     */
    private Long id;

    /**
     * 工序步骤名称
     */
    private String name;

    /**
     * 步骤或者任务的区分
     */
    private StepTaskTypeEnum type;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 工序模型id
     */
    private Long processId;

    /**
     * 工序模型名称
     */
    private String processName;
}
