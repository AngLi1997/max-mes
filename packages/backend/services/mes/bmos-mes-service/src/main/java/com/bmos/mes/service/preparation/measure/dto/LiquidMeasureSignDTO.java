package com.bmos.mes.service.preparation.measure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("配液量取签名")
public class LiquidMeasureSignDTO {

    @ApiModelProperty("量取组件实例id")
    @NotNull
    private Long measureInstanceId;

    @ApiModelProperty("备注")
    private String remark;

}
