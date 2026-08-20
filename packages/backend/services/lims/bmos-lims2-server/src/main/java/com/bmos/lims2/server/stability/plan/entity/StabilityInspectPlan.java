package com.bmos.lims2.server.stability.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 稳定性考察计划实体类
 */
@Getter
@Setter
@TableName("lm_stability_inspect_plan")
public class StabilityInspectPlan extends BaseDO {

    /**
     * 考察计划编号（自动生成，格式：T + 9位序号）
     */
    private String code;

    /**
     * 关联的稳定性方案ID
     */
    private Long schemeId;

    /**
     * 关联的稳定性方案名称（冗余）
     */
    private String schemeName;

    /**
     * 关联的稳定性方案版本ID
     */
    private Long schemeVersionId;

    /**
     * 关联的稳定性方案版本号（冗余）
     */
    private String schemeVersionNo;

    /**
     * 检品ID（物料ID）
     */
    private Long materialId;

    /**
     * 检品名称（冗余）
     */
    private String materialName;

    /**
     * 检品编码（冗余）
     */
    private String materialCode;

    /**
     * 检品规格
     */
    private String materialSpec;

    /**
     * 计划状态
     */
    private StabilityInspectPlanStatusEnum status;

    /**
     * 计划开始时间（最早样品接收时间，自动计算）
     */
    private LocalDate startDate;

    /**
     * 计划结束时间（根据方案时间点计算）
     */
    private LocalDate planEndDate;

    /**
     * 暂停时间
     */
    private java.time.LocalDateTime pauseTime;

    /**
     * 暂停操作人ID
     */
    private String pauseBy;

    /**
     * 暂停操作人姓名
     */
    private String pauseByName;

    /**
     * 暂停理由
     */
    private String pauseReason;

    /**
     * 备注
     */
    private String remark;
}
