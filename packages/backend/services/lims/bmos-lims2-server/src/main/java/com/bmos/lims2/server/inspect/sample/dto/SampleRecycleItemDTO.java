package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description: 回收条目
 * @Author: yigaohui
 * @Date: 2025/09/10 10:30
 */
@Getter
@Setter
@ApiModel("回收条目")
public class SampleRecycleItemDTO {

    @ApiModelProperty("样品ID")
    @NotNull
    private Long sampleId;

    @ApiModelProperty("回收余量")
    @NotNull
    @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "回收余量最多整数6位，小数5位")
    private String recycleQuantity;

    @ApiModelProperty("回收单位ID")
    @NotNull
    private Long recycleUnitId;

    @ApiModelProperty("回收备注")
    private String recycleRemark;
}



