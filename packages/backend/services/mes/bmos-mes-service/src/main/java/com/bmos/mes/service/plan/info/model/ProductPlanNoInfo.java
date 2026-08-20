package com.bmos.mes.service.plan.info.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 生产计划编号信息
 */
@Data
@TableName("bm_product_plan_no_info")
public class ProductPlanNoInfo {

    /**
     * 生产指令单id
     */
    private Long productPlanId;

    /**
     * 计划编号规则CODE
     */
    private String planNoCode;

    /**
     * 批号编号规则CODE
     */
    private String batchNoCode;

    /**
     * 计划编号
     */
    private String planNo;

    /**
     * 批次编号
     */
    private String batchNo;

    /**
     * 计划编号id
     */
    private Long planNoId;

    /**
     * 批次编号id
     */
    private Long batchNoId;

    /**
     * 编码规则相关参数
     */
    private String fields;

}
