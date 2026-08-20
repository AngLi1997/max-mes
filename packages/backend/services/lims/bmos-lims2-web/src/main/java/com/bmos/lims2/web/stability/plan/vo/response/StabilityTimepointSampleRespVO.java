package com.bmos.lims2.web.stability.plan.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 稳定性时间点取样列表行响应VO
 */
@Data
@ApiModel("稳定性时间点取样列表行")
public class StabilityTimepointSampleRespVO {

    @ApiModelProperty("时间点任务ID")
    private Long timepointTaskId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("稳定性考察编号")
    private String planCode;

    @ApiModelProperty("计划ID")
    private Long planId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("试验类型名称")
    private String experimentTypeName;

    @ApiModelProperty("试验储存条件")
    private String storageCondition;

    @ApiModelProperty("计划取样量")
    private String plannedSampleAmount;

    @ApiModelProperty("取样量单位")
    private String sampleUnit;

    @ApiModelProperty("取样量单位名称")
    private String sampleUnitName;

    @ApiModelProperty("物料标准单位ID")
    private Long materialUnitId;

    @ApiModelProperty("物料标准单位名称")
    private String materialUnitName;

    @ApiModelProperty("关联整体样品ID（取样对象默认值，StabilityPlanSample.id）")
    private Long planSampleId;

    @ApiModelProperty("批次ID")
    private Long batchId;

    @ApiModelProperty("方案检验计划ID")
    private Long schemePlanId;
}
