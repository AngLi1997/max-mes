package com.bmos.mes.service.preparation.measure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("完成量取DTO")
public class LiquidMeasureCompleteDTO {

    @ApiModelProperty("量取批次id")
    @NotNull
    private Long measureBatchId;


    @ApiModelProperty("完成人id")
    @NotNull
    private String completeUserId;

}
