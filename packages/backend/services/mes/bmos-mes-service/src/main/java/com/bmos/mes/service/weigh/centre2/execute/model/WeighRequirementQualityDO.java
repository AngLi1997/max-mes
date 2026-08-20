package com.bmos.mes.service.weigh.centre2.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 工单需求称量的量DO
 * 对应表：bm_weigh_requirement_quality
 */
@Data
@ToString
@TableName("bm_weigh_requirement_quality")
public class WeighRequirementQualityDO extends BaseDO {
    /**
     * 称量工单需求ID
     * */
    private Long weighTicketRequirementId;
    /**
     * 已称量的量
     */
    private BigDecimal weighQuality;
    /**
     * 当前称量需求内所有物料件的可用量之和
     */
    private BigDecimal quality;
    /**
     * 当前称量需求内所有物料件的数量
     */
    private Long storageMaterialCount;
} 