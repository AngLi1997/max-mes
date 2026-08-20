package com.bmos.lims2.web.inspect.sample.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ApiModel("回收条目（按样品提交）")
public class SampleRecycleItemReqVO {

    @ApiModelProperty("样品ID")
    @NotNull
    private Long sampleId;

    @ApiModelProperty("回收余量")
    @NotNull
    @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "回收余量最多整数6位，小数5位，且不得为负")
    private String recycleQuantity;

    @ApiModelProperty("回收单位ID")
    @NotNull
    private Long recycleUnitId;

    @ApiModelProperty("回收备注")
    private String recycleRemark;
}



