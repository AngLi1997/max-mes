package com.bmos.mes.service.exception.dto;

import com.bmos.mes.service.exception.model.ExecuteException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@ApiModel("异常操作历史信息DTO")
@AllArgsConstructor
public class ExceptionOperationLogDTO {

    /**
     * 异常类型
     */
    private String exceptionType;

    /**
     * 异常描述
     */
    private String exceptionDescription;

    /**
     * 记录方式
     */
    private String recordMode;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordTime;

    /**
     * 产品编码-名称
     */
    private String productFullName;

    @ApiModelProperty("重新调查原因")
    private String reInvestigateReason;

    /**
     * 生产批号
     */
    private String batchNo;

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
     * 工步/任务 名称
     */
    private String procedureStepName;

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

    public void compareAndClearSameProperties(ExecuteException oldModel) {
        exceptionType = Objects.equals(oldModel.getExceptionType(), exceptionType) ? null : exceptionType;
        exceptionDescription = Objects.equals(oldModel.getExceptionDescription(), exceptionDescription) ? null : exceptionDescription;
        recordMode = Objects.equals(oldModel.getRecordMode().getName(), recordMode) ? null : recordMode;
        productFullName = Objects.equals(oldModel.getProductFullName(), productFullName) ? null : productFullName;
        batchNo = Objects.equals(oldModel.getBatchNo(), batchNo) ? null : batchNo;
        processName = Objects.equals(oldModel.getProcessName(), processName) ? null : processName;
        processVersion = Objects.equals(oldModel.getProcessVersion(), processVersion) ? null : processVersion;
        procedureName = Objects.equals(oldModel.getProcedureName(), procedureName) ? null : procedureName;
        procedureStepName = Objects.equals(oldModel.getProcedureStepName(), procedureStepName) ? null : procedureStepName;
        exceptionDescription = Objects.equals(oldModel.getExceptionDescription(), exceptionDescription) ? null : exceptionDescription;
        recordTime = Objects.equals(oldModel.getRecordTime(), recordTime) ? null : recordTime;
    }

}
