package com.bmos.lims2.web.stability.plan.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 时间点取样"取样对象"下拉选项响应VO
 */
@Data
@ApiModel("时间点取样对象选项")
public class StabilityTimepointSourceSampleRespVO {

    @ApiModelProperty("整体样品ID（StabilityPlanSample.id）")
    private Long planSampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("实际取样量")
    private String actualSampleAmount;

    @ApiModelProperty("取样量单位")
    private String sampleUnit;

    @ApiModelProperty("储存位置")
    private String storageLocation;

    @ApiModelProperty("当前剩余数量")
    private String currentQuantity;
}
