package com.bmos.mes.service.exception.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel("手动录入异常DTO")
public class ExceptionManualRecordDTO {

    @ApiModelProperty("异常类型")
    @NotBlank
    private String exceptionType;

    @ApiModelProperty("异常管理")
    @NotBlank
    private String exceptionTypeCode;

    @ApiModelProperty("记录时间")
    @NotNull
    private LocalDateTime recordTime;

    @ApiModelProperty("记录人")
    @NotBlank
    private String recordUserId;

    @ApiModelProperty("异常描述")
    @NotBlank
    private String exceptionDescription;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("生产批次id")
    private Long productPlanId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

}
