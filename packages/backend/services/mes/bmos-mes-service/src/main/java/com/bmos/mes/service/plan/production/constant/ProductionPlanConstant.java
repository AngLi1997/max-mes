package com.bmos.mes.service.plan.production.constant;

import io.swagger.annotations.ApiModelProperty;

/**
 * 生产计划常量
 */
public interface ProductionPlanConstant {

    /**
     * 产品编码
     */
    String PRODUCT_MERGE_CODE = "productMergeCode";

    /**
     * 产品名称
     */
    String PRODUCT_NAME = "productName";

    /**
     * 计划类型
     */
    String PRODUCT_PLAN_TYPE= "productPlanType";

    /**
     * 产线编码
     */
    String PRODUCTION_LINE_CODE = "productionLineCode";

    /**
     * 内包规格
     */
    String INNER_PACKING_SPECIFICATION = "innerPackingSpecification";

    /**
     * 包装规格
     */
    String PACKING_SPECIFICATION = "packingSpecification";

    /**
     * 生产阶段代码
     */
    String PRODUCTION_STAGE_CODE = "productionStageCode";

    /**
     * 产品标识
     */
    String PRODUCT_MARK = "productMark";

    /**
     * 申请编码日期(此处传入计划开始时间 对应平台codeApplyTime)
     */
    String APPLY_TIME = "codeApplyTime";
}
