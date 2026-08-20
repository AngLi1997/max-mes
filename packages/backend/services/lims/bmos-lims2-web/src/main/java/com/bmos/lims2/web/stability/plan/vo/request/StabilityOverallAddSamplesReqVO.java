package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 整体取样-新增样品请求VO
 * 用于在取样页面手动追加试验类型样品（不来源于方案）
 */
@Data
@ApiModel("整体取样-新增样品请求")
public class StabilityOverallAddSamplesReqVO {

    @ApiModelProperty(value = "新增样品明细列表", required = true)
    @NotEmpty(message = "样品明细不能为空")
    @Valid
    private List<SampleItemVO> items;

    @Data
    @ApiModel("整体取样-新增样品明细行")
    public static class SampleItemVO {

        @ApiModelProperty(value = "试验类型（字典值）", required = true)
        @NotBlank(message = "试验类型不能为空")
        private String experimentType;

        @ApiModelProperty(value = "试验储存条件", required = true)
        @NotBlank(message = "试验储存条件不能为空")
        private String storageCondition;

        @ApiModelProperty(value = "计划取样量", required = true)
        @NotNull(message = "计划取样量不能为空")
        private String plannedSampleAmount;

        @ApiModelProperty("实际取样量（可选）")
        private String actualSampleAmount;

        @ApiModelProperty("取样量单位ID")
        private String sampleUnit;
    }
}
