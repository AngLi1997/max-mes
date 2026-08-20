package com.bmos.mes.service.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 辅助记录表
 */
@Getter
@Setter
@TableName("bm_execute_subsidiary_record")
public class ExecuteSubsidiaryRecord extends BaseDO {

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 工序模型id
     */
    private Long procedureModelId;

    /**
     * 工步名称
     */
    private String procedureStepName;

    /**
     * 工步模型id
     */
    private Long procedureStepModelId;

    /**
     * 记录是否复用
     */
    private Boolean reuse;

    /**
     * 工步id
     */
    private Long procedureStepId;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录版本id
     */
    private Long recordVersionId;

    /**
     * 工艺换班班次
     */
    private Integer processChangeNumber;

    /**
     * 工序换班班次
     */
    private Integer procedureChangeNumber;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 完成人
     */
    private String completeUserId;

    /**
     * 归档URL
     */
    private String archiveUrl;


}
