package com.bmos.lims2.web.inspect.division.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

/**
 * @Description: 分样剩余量响应VO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("分样剩余量响应")
public class SampleDivisionRemainRespVO {

    @ApiModelProperty("原样品总量")
    private String originalQuantity;

    @ApiModelProperty("原样品单位ID")
    private Long originalUnitId;

    @ApiModelProperty("原样品单位名称")
    private String originalUnitName;

    @ApiModelProperty("已分样总量（转换为原样品单位）")
    private String dividedQuantity;

    @ApiModelProperty("剩余量（转换为原样品单位）")
    private String remainingQuantity;
}
