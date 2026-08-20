package com.bmos.mes.service.preparation.plan.model;

import lombok.Data;

@Data
public class LiquidPreparationPlanMaterial {

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 浓度参数 前端存的id
     */
    private String consistenceParamCode;

    /**
     * 浓度参数
     */
    private String field;

    /**
     * 浓度参数名称
     */
    private String fieldName;

    /**
     * 目标浓度
     */
    private String targetConcentration;

}
