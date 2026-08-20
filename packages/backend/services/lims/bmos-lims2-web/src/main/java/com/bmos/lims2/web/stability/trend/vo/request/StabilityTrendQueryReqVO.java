package com.bmos.lims2.web.stability.trend.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 稳定性趋势查询请求VO
 */
@Getter
@Setter
@ApiModel("稳定性趋势查询请求")
public class StabilityTrendQueryReqVO {

    @NotNull(message = "稳定性方案版本ID不能为空")
    @ApiModelProperty(value = "稳定性方案版本ID", required = true)
    private Long versionId;

    @NotBlank(message = "试验类型不能为空")
    @ApiModelProperty(value = "试验类型", required = true)
    private String experimentType;

    @NotBlank(message = "储存条件不能为空")
    @ApiModelProperty(value = "储存条件", required = true)
    private String storageCondition;

    @NotNull(message = "分析项ID不能为空")
    @ApiModelProperty(value = "分析项ID", required = true)
    private Long parameterId;

    @NotBlank(message = "数据点名称不能为空")
    @ApiModelProperty(value = "数据点名称", required = true)
    private String dataPointName;

    @ApiModelProperty("批号（可选）")
    private String batchNo;
}
