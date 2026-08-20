package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 检验方案取样量配置保存请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案取样量配置保存请求")
public class InspectionSchemeSamplingSaveReqVO {

    @ApiModelProperty("检验项目ID，为NULL时表示整体取样")
    private Long inspectItemId;

    @ApiModelProperty(value = "取样量", required = true)
    @NotNull(message = "取样量不能为空")
    private String samplingAmount;

    @ApiModelProperty(value = "取样单位", required = true)
    @NotNull(message = "取样单位不能为空")
    private String samplingUnit;

    @ApiModelProperty(value = "取样份数", required = true)
    @NotNull(message = "取样份数不能为空")
    @Positive(message = "取样份数必须大于0")
    private Integer samplingCount;
} 