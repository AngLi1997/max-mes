package com.bmos.mes.service.weigh.centre2.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 称量工单与操作人绑定关系DO
 */
@Getter
@Setter
@ToString
@TableName("bm_weigh_ticket_user")
public class WeighTicketUserDO extends BaseDO {
    /** 称量工单id bm_weigh_ticket主键id */
    private Long weighTicketId;
    /** 工单操作人id */
    private String operator;
    /** 签名人id（工单操作人变更时需要） */
    private String signUser;
    /** 备注 */
    private String remark;
}