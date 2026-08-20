package com.bmos.lims2.web.stability.plan.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 稳定性方案试验类型响应VO（用于新增样品时下拉选项）
 */
@Data
@ApiModel("方案试验类型")
public class StabilitySchemeExperimentTypeRespVO {

    @ApiModelProperty("试验类型（字典值）")
    private String experimentType;

    @ApiModelProperty("试验类型名称（字典标签）")
    private String experimentTypeName;

    @ApiModelProperty("储存条件")
    private String storageCondition;

    @ApiModelProperty("整体取样量（来自方案检验计划）")
    private String totalSampleAmount;

    @ApiModelProperty("取样量单位ID")
    private String sampleUnit;

    @ApiModelProperty("取样量单位名称")
    private String unitName;
}
