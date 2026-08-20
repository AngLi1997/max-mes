package com.bmos.lims2.web.inspect.order.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 检验取样信息保存请求VO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("检验取样信息保存请求")
public class InspectionSamplingSaveVO {

    @ApiModelProperty("取样信息ID（编辑时传入）")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty(value = "计划取样量", required = true)
    @NotNull(message = "计划取样量不能为空")
    @Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "计划取样量最多整数6位，小数5位")
    private String plannedQuantity;

    @ApiModelProperty(value = "单位ID", required = true)
    @NotNull(message = "单位ID不能为空")
    private Long unitId;

    @ApiModelProperty(value = "取样份数", required = true)
    @NotNull(message = "取样份数不能为空")
    @Min(value = 1, message = "取样份数不能小于1")
    private Integer sampleCount;

    @ApiModelProperty("备注")
    private String remark;
}