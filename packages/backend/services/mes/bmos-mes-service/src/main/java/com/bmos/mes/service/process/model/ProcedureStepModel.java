package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工序步骤信息实体
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_step_model")
public class ProcedureStepModel extends BaseDO {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 节点功能
     */
    private String nodeFunction;


    /**
     * 是否可复用
     */
    private Boolean reusable;
    /**
     * 工序id
     */
    private Long procedureId;


    private Long procedureModelId;

    private Long procedureStepId;
    /**
     * 工艺版本号
     */
    private String processVersion;

    /**
     * 名称
     */
    private String name;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 单位
     */
    private String timeUnit;

    /**
     * 记录项Id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 操作规程
     *//*
    private String operationSop;*/

    private Long delIdFlag;

    private Integer sort;


    private String area;

    private String equipment;

    private StepTaskTypeEnum stepType;

}
