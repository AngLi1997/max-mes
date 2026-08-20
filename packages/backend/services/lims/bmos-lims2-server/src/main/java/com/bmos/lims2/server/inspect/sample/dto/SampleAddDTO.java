package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @className: SampleAddDTO
 * @author: yigaohui
 * @date: 2025/8/18 14:50
 * @Version: 1.0
 * @description:
 */

@ApiModel("新增样品信息")
@Data
public class SampleAddDTO {
    @ApiModelProperty(value = "请验单ID",required = true)
    @NotNull(message = "请验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty(value = "检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty(value = "计划取样量",required = true)
    @NotNull(message = "计划取样量不能为空")
    @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "计划取样量最多整数6位，小数5位")
    private String planQuantity;

    @ApiModelProperty(value = "取样单位",required = true)
    @NotNull(message = "取样单位不能为空")
    private Long unitId;

    @ApiModelProperty(value = "份数",required = true)
    private int count;

    @ApiModelProperty(value = "实际取样量")
    @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "实际取样量最多整数6位，小数5位")
    private String quantity;
}
