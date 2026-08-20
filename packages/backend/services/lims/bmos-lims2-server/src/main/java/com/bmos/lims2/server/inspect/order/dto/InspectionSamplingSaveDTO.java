package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Pattern;

/**
 * 检验取样信息保存DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("检验取样信息保存DTO")
public class InspectionSamplingSaveDTO {

    @ApiModelProperty("ID（更新时必填）")
    private Long id;

    @ApiModelProperty("检验项目ID（可为空，代表整体取样）")
    private Long inspectItemId;

    @ApiModelProperty("计划取样量")
    @NotNull(message = "计划取样量不能为空")
    @Positive(message = "计划取样量须大于0")
    @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "计划取样量最多整数6位，小数5位")
    private String plannedQuantity;

    @ApiModelProperty("单位ID")
    @NotNull(message = "单位ID不能为空")
    private Long unitId;

    @ApiModelProperty("计划取样份数")
    @NotNull(message = "计划取样份数不能为空")
    @Positive(message = "计划取样份数须大于0")
    private Integer sampleCount;

    @ApiModelProperty("取样方式")
    private String samplingMethod;

    @ApiModelProperty("取样地点")
    private String samplingLocation;

    @ApiModelProperty("取样说明")
    private String samplingDescription;

    @ApiModelProperty("备注")
    private String remark;
}