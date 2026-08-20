package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("产品已完成的生产批次VO")
@Data
public class ProductPlanBatchPageVO {

    @ApiModelProperty("id")
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

    @ApiModelProperty("生产开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("生产结束时间")
    private LocalDateTime endTime;

}
