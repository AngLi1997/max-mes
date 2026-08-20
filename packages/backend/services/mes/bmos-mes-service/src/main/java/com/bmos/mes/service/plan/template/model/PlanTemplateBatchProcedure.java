package com.bmos.mes.service.plan.template.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 生产计划模板批次工步配置
 */
@Getter
@Setter
public class PlanTemplateBatchProcedure {

    /**
     * procedure_id
     * 历史工序id
     * 记录历史工序id使在工艺升版时此处也能继承配置
     */
    private Long procedureId;

    /**
     * 工序名称
     */
    private String name;

    /**
     * 间隔时长
     */
    private Integer intervalDuration;

    /**
     * 执行时长
     */
    private Integer executionDuration;

    /**
     * 排序
     */
    private int sort;

}
