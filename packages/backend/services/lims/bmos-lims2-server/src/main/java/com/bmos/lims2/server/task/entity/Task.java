package com.bmos.lims2.server.task.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 任务实体类
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
@TableName("lm_task")
public class Task extends BaseDO {

    /**
     * 检验单ID
     */
    private Long inspectionOrderId;

    /**
     * 方案版本ID（冗余）
     */
    private Long schemeVersionId;

    /**
     * 检验项目ID
     */
    private Long inspectItemId;


    /**
     * 检验项目编码
     */
    private String inspectItemCode;

    /**
     * 方案检验项目配置ID（冗余）
     */
    private Long itemConfigId;

    /**
     * 分析项ID
     */
    private Long parameterId;

    /**
     * 分析项编码
     */
    private String parameterCode;

    /**
     * 方案分析项配置ID（冗余）
     */
    private Long parameterConfigId;

    /**
     * 是否可执行
     */
    private Boolean isExecutable;

    /**
     * 是否可报告
     */
    private Boolean isReportable;

    /**
     * 执行方式：LIMS/ELN（来源于方案分析项配置）
     */
    private ExecuteMethodEnum executeMethod;

    /**
     * 方法ID（ELN执行方式）
     */
    private Long recordId;

    /**
     * 方法生效版本ID（ELN执行方式）
     */
    private Long recordVersionId;

    /**
     * 方法项ID（ELN执行方式，来源于方案分析项配置的recordItemId）
     */
    private Long recordItemId;

    /**
     * 任务状态
     */
    private TaskStatusEnum status;

    // 合并录入状态至任务状态后，不再单独维护entryStatus

    /**
     * 判定结果：1-通过，0-不通过
     */
    private Boolean judgedResult;

    /**
     * 是否异常(根据表达式计算)
     */
    private Boolean judgedAbnormal;

    /**
     * 判定时间
     */
    private LocalDateTime judgedTime;

    /**
     * 检验时间
     */
    private LocalDateTime testTime;


    /**
     * 复核不通过/异常原因
     */
    private String reviewedAbnormalReason;

    /**
     * 复核人ID
     */
    private String reviewedBy;

    /**
     * 复核时间
     */
    private LocalDateTime reviewedTime;

    /**
     * 样品审核人ID
     */
    private String sampleAuditBy;

    /**
     * 样品审核时间
     */
    private LocalDateTime sampleAuditTime;

    /**
     * 任务所有人ID
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String ownerId;

    /**
     * 任务所有人姓名
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String ownerName;

    /**
     * 分配人ID
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String assignerId;

    /**
     * 分配人姓名
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String assignerName;

    /**
     * 分配时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime assignTime;

    /**
     * 领取时间
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime claimTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 终止时间
     */
    private LocalDateTime terminateTime;

    /**
     * 终止原因
     */
    private String terminateReason;

    /**
     * 退回原因
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String returnReason;

    /**
     * 审批不通过原因
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String rejectReason;


    /**
     * 备注
     */
    private String remark;
}
