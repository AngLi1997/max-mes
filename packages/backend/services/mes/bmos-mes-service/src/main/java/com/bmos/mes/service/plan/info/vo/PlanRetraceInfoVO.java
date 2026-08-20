package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 追溯的生产批次的简单信息
 */
@ApiModel("追溯的生产批次的信息")
@Data
public class PlanRetraceInfoVO {

    @ApiModelProperty("生产批次id")
    private Long id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("生产开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("生产结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

}
