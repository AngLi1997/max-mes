package com.bmos.mes.service.preparation.measure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("配液量取改变量取人DTO")
public class LiquidMeasureChangeMeasurerDTO {

    @ApiModelProperty("量取人id")
    @NotBlank
    private String measurerId;

    @ApiModelProperty("复核人id")
    @NotBlank
    private String reCheckerId;

    @ApiModelProperty("量取组件实例id")
    @NotNull
    private Long measureInstanceId;


}
