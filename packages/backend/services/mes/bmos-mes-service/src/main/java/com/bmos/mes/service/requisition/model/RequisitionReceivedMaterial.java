package com.bmos.mes.service.requisition.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 领料计划 待领物料件
 */
@Getter
@Setter
@TableName("bm_requisition_received_material")
public class RequisitionReceivedMaterial extends BaseDO {

    /**
     * 领料单id
     */
    private Long requisitionPlanId;

    /**
     * 批次id
     */
    private Long inventoryBatchId;

    /**
     * 物料平台id
     */
    private Long platformMaterialId;

    /**
     * 物料件号
     */
    private String inventoryNo;

    /**
     * 发放物料量
     */
    private BigDecimal quantity;

    /**
     * 单位id(对应物料单位,根源为配方物料单位)
     */
    private Long unitId;

    /**
     * 暂存货位id
     */
    private Long cargoPositionId;

    /**
     * received_batch_id
     */
    private Long receivedBatchId;

}
