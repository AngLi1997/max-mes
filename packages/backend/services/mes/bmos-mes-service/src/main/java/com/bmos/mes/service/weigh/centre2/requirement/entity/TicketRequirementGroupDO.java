package com.bmos.mes.service.weigh.centre2.requirement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.weigh.centre.TicketRequirementReleaseStatus;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 工单需求实体类
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_weigh_ticket_requirement_group")
@Data
public class TicketRequirementGroupDO extends BaseDO {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * bom版本id
     */
    private Long bomVersionId;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 计划日期
     */

    private LocalDate planDate;

    /**
     * 备注
     */
    private String remark;

    
    /**
     * 发布状态
     */
    private TicketRequirementReleaseStatus releaseStatus;
}
