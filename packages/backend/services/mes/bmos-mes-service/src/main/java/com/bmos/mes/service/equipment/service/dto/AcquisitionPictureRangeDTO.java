package com.bmos.mes.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("设备数采绘图区间DTO")
public class AcquisitionPictureRangeDTO {

    @ApiModelProperty("采集最大值")
    private BigDecimal maxValue;

    @ApiModelProperty("采集最小值")
    private BigDecimal minValue;

    @ApiModelProperty("工步模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("顶层组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("设备数采数据code")
    @NotBlank
    private String acquisitionDataCode;

}
