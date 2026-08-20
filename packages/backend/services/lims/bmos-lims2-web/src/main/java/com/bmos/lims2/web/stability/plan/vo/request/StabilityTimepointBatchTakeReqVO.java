package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 稳定性时间点批量取样请求VO
 */
@Data
@ApiModel("稳定性时间点批量取样请求")
public class StabilityTimepointBatchTakeReqVO {

    @ApiModelProperty(value = "取样明细列表", required = true)
    @NotEmpty(message = "取样明细不能为空")
    @Valid
    private List<ItemVO> items;

    @Data
    @ApiModel("时间点取样明细行")
    public static class ItemVO {

        @ApiModelProperty(value = "时间点任务ID", required = true)
        @javax.validation.constraints.NotNull(message = "时间点任务ID不能为空")
        private Long timepointTaskId;

        @ApiModelProperty(value = "取样对象（StabilityPlanSample.id）", required = true)
        @javax.validation.constraints.NotNull(message = "取样对象不能为空")
        private Long sourcePlanSampleId;

        @ApiModelProperty("实际取样量")
        private String actualSampleAmount;

        @ApiModelProperty("取样量单位（可选，覆盖默认单位）")
        private String sampleUnit;
    }
}
