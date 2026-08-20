package com.bmos.lims2.server.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 任务状态变更历史实体类
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
@TableName("lm_task_status_history")
public class TaskStatusHistory extends BaseDO {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 操作类型
     */
    private TaskOperationTypeEnum operationType;

    /**
     * 操作前状态
     */
    private String fromStatus;

    /**
     * 操作后状态
     */
    private String toStatus;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 评论/备注
     */
    private String comment;

    /**
     * 说明
     */
    private String remark;

    /**
     * 操作详情（JSON）
     */
    private String detail;
}
