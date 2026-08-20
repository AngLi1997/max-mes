package com.bmos.mes.service.exception.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.execute.ExceptionRecordModeEnum;
import com.bmos.mes.common.enums.execute.ExceptionStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 执行异常记录
 */
@Getter
@Setter
@TableName("bm_execute_exception")
public class ExecuteException extends BaseDO {

    /**
     * 异常类型
     */
    private String exceptionType;

    /**
     * 异常类型code
     */
    private String exceptionTypeCode;

    /**
     * 异常描述
     */
    private String exceptionDescription;

    /**
     * 异常状态
     */
    private ExceptionStatusEnum exceptionStatus;

    /**
     * 记录方式
     */
    private ExceptionRecordModeEnum recordMode;

    /**
     * 记录人
     */
    private String recordUserId;

    /**
     * 记录人 名称-code
     */
    private String recordUserName;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品编码-名称
     */
    private String productFullName;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序模型id
     */
    private Long procedureModelId;

    /**
     * 工步/任务 名称
     */
    private String procedureStepName;

    /**
     * 工步id
     */
    private Long procedureStepId;

    /**
     * 工步模型id
     */
    private Long procedureStepModelId;

    /**
     * 处理人id
     */
    private String handleUserId;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理人名称
     */
    private String handleUserName;

    /**
     * 作废人id
     */
    private String cancelUserId;

    /**
     * 作废人名称
     */
    private String cancelUserName;

    /**
     * 作废原因
     */
    private String cancelReason;

    /**
     * 作废时间
     */
    private LocalDateTime cancelTime;

    /**
     * execute_form_data 主键id
     */
    private Long executeFormDataId;

}
