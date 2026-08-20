package com.bmos.mes.service.requisition.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 领料计划
 */
@Getter
@Setter
@TableName("bm_requisition_plan")
public class Requisition extends BaseDO {

    /**
     * 领料单名称
     */
    private String name;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 复制版本
     */
    private Long copyVersion;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 流水号
     */
    private Integer serialNo;

    /**
     * 领料类型->按批次量领料 按物料量领料
     */
    private String requisitionType;

    /**
     * 仓库是否发料
     */
    private Integer sendStatus;

    /**
     * 是否完成向仓库发送领料计划
     */
    private Boolean completedPlan;

    /**
     * bm_requisition_received主键id
     */
    private Long receivedId;

    /**
     * 是否完成领料接收
     */
    private Boolean completedReceive;


}
