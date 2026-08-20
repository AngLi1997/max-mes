package com.bmos.mes.service.weigh.centre.task.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.weigh.centre.TaskProgramTypeEnum;
import com.bmos.mes.common.enums.weigh.centre.TaskStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 称量任务
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_task")
public class WeighTask extends BaseDO {


    /**
     * 称量任务编号
     */
    private String taskNo;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 需求量
     */
    private BigDecimal requirementQuantity;

    /**
     * 执行时间
     */
    private LocalDate executeDate;

    /**
     * 任务状态
     */
    private TaskStatusEnum taskStatus;

    /**
     * 下发时间
     */
    private LocalDateTime sendTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 规划类型
     */
    private TaskProgramTypeEnum taskProgramType;

    /**
     * 规划时间
     */
    private LocalDateTime processTime;

    /**
     * 规划人id
     */
    private String processOperatorId;

    /**
     * 称量人id
     */
    private String preWeigherId;

    /**
     * 复核人id
     */
    private String preReCheckerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 当前称量的物料批次id
     */
    private Long storageMaterialBatchId;
}
