package com.bmos.mes.service.weigh.centre2.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 物料件与称量需求绑定关系DO
 */
@Getter
@Setter
@ToString
@TableName("bm_weigh_storage_material_requirement_record")
public class WeighStorageMaterialRequirementDO extends BaseDO {
    /** 称量工单需求id bm_weigh_ticket_requirement_id主键id */
    private Long weighTicketRequirementId;
    /** 暂存货位id */
    private Long storageMaterialId;
    /** 当前物料件消耗的量 */
    private java.math.BigDecimal consumeQuantity;
} 