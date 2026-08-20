package com.bmos.mes.service.plan.production.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel("生产计划ItemVO")
@Data
public class ProductionPlanItemVO {

    @ApiModelProperty("生产计划itemId")
    private Long productionPlanItemId;

    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @ApiModelProperty("生产指令单编号")
    private String planNo;

    @ApiModelProperty("生产批次编号")
    private String batchNo;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("生产批量单位id")
    private Long unitId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("产线code")
    private String productionLineCode;

    @ApiModelProperty("产品阶段代码")
    private String productionStageCode;

    @ApiModelProperty("工序数量")
    private Integer processNum;

}
