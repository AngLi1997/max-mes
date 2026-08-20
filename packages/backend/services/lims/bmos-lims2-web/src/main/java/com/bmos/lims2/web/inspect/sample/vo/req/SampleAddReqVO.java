package com.bmos.lims2.web.inspect.sample.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description: 新增样品请求VO
 * @Author: yigaohui
 * @Date: 2025/08/18 18:30
 */
@Data
@ApiModel("新增样品请求")
public class SampleAddReqVO {

    @ApiModelProperty(value = "检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty(value = "计划取样量", required = true)
    @NotNull(message = "计划取样量不能为空")
    @Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "计划取样量最多整数6位，小数5位")
    private String planQuantity;

    @ApiModelProperty(value = "实际取样量")
    @Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "实际取样量最多整数6位，小数5位")
    private String actualQuantity;

    @ApiModelProperty(value = "取样单位", required = true)
    @NotNull(message = "取样单位不能为空")
    private Long unitId;

    @ApiModelProperty(value = "份数", required = true)
    @NotNull(message = "份数不能为空")
    private Integer count;
}


