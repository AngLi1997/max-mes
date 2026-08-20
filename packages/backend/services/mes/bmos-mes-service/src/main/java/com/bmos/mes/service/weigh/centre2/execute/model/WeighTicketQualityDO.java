package com.bmos.mes.service.weigh.centre2.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 工单称量的量DO
 * 对应表：bm_weigh_ticket_quality
 */
@Data
@ToString
@TableName("bm_weigh_ticket_quality")
public class WeighTicketQualityDO extends BaseDO {
    /** 称量工单ID */
    private Long weighTicketId;
    /** 已称量的量 */
    private BigDecimal weighQuality;

    /** 添加的所有物料件的量 */
    private BigDecimal quality;

} 