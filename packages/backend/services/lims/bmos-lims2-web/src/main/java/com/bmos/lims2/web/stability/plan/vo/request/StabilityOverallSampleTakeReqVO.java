package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 整体批量取样请求VO
 */
@Data
@ApiModel("整体批量取样请求")
public class StabilityOverallSampleTakeReqVO {

    @ApiModelProperty(value = "取样人ID", required = true)
    @NotBlank(message = "取样人ID不能为空")
    private String samplerId;

    @ApiModelProperty(value = "取样人姓名", required = true)
    @NotBlank(message = "取样人姓名不能为空")
    private String samplerName;

    @ApiModelProperty(value = "各试验类型取样明细", required = true)
    @NotEmpty(message = "取样明细不能为空")
    @Valid
    private List<SampleTakeItemVO> items;

    @Data
    @ApiModel("整体取样-明细行")
    public static class SampleTakeItemVO {

        @ApiModelProperty(value = "整体样品ID（lm_stability_plan_sample.id）", required = true)
        @NotNull(message = "样品ID不能为空")
        private Long sampleId;

        @ApiModelProperty("实际取样量")
        private String actualSampleAmount;
    }
}
