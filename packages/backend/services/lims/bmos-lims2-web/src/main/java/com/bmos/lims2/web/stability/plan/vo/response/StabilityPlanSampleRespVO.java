package com.bmos.lims2.web.stability.plan.vo.response;

import com.bmos.lims2.common.enums.StabilityTimepointTaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性考察样品响应VO（含稳定性上下文）
 */
@Data
@ApiModel("稳定性考察样品")
public class StabilityPlanSampleRespVO {

    @ApiModelProperty("样品ID")
    private Long sampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("是否已取样")
    private Boolean sampled;

    @ApiModelProperty("是否已接收")
    private Boolean received;

    @ApiModelProperty("取样人")
    private String samplerName;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人")
    private String receiverName;

    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("计划取样量")
    private String planQuantity;

    @ApiModelProperty("取样量单位ID")
    private Long unitId;

    @ApiModelProperty("时间点任务ID")
    private Long timepointTaskId;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("储存条件")
    private String storageCondition;

    @ApiModelProperty("时间点数值")
    private Integer timeValue;

    @ApiModelProperty("时间单位（DAY/WEEK/MONTH/YEAR）")
    private String timeUnit;

    @ApiModelProperty("计划检验日期")
    private LocalDate plannedDate;

    @ApiModelProperty("时间点任务状态")
    private StabilityTimepointTaskStatusEnum taskStatus;

    @ApiModelProperty("批号")
    private String batchNo;
}
