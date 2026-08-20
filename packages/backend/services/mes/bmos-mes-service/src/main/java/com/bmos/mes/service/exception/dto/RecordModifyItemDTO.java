package com.bmos.mes.service.exception.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RecordModifyItemDTO {

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("原值")
    private String originalValue;

    @ApiModelProperty("操作人")
    private String userId;

    @ApiModelProperty("复核人")
    private String reviewerId;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;
}
