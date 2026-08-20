package com.bmos.lims2.server.inspect.retention.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: 留样观察任务实体
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_retention_observation_task")
public class RetentionObservationTask extends BaseDO {

    /**
     * 样品ID
     */
    private Long sampleId;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 观察年度（第几年）
     */
    private Integer observationYear;

    /**
     * 任务到期日期（样品接收日期+观察年度）
     */
    private LocalDate dueDate;

    /**
     * 是否已完成（0-未完成，1-已完成）
     */
    private Boolean completed;

    /**
     * 观察结果（true-符合，false-不符合）
     */
    private Boolean observationResult;

    /**
     * 观察备注
     */
    private String observationRemark;

    /**
     * 观察人ID
     */
    private String observerId;

    /**
     * 观察人名称
     */
    private String observerName;

    /**
     * 观察时间
     */
    private LocalDateTime observationTime;
}
