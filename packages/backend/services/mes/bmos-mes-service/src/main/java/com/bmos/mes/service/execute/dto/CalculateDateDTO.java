package com.bmos.mes.service.execute.dto;

import com.bmos.mes.common.enums.execute.CalculateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("时间差计算dto")
public class CalculateDateDTO {

    @ApiModelProperty("开始时间")
    @NotBlank
    private String startTime;

    @ApiModelProperty("结束时间")
    @NotBlank
    private String endTime;

    @ApiModelProperty("计算时间类型")
    private String dateType = CalculateTypeEnum.DD_HH_MM_SS.getName();

    @ApiModelProperty("舍入方式 向上 true; 向下：false")
    private Boolean roundingRule = Boolean.TRUE;
}
