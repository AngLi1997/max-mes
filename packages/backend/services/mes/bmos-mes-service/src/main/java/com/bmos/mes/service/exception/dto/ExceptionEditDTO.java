package com.bmos.mes.service.exception.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel("异常编辑DTO")
public class ExceptionEditDTO {

    @ApiModelProperty("异常id")
    private Long id;

    @ApiModelProperty("异常类型")
    @NotBlank
    private String exceptionType;

    @ApiModelProperty("异常类型code")
    @NotBlank
    private String exceptionTypeCode;

    @ApiModelProperty("记录时间")
    @NotNull
    private LocalDateTime recordTime;

    @ApiModelProperty("编辑人id")
    @NotBlank
    private String editUserId;

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
