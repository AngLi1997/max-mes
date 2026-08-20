package com.bmos.mes.service.workflow.vo;


import com.bmos.mes.common.enums.plan.ProductPlanTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel("生产历史分页VO")
@Data
public class WorkflowPlanHistoryPageVO {

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("生产时间")
    private LocalDate productDate;

    @ApiModelProperty("计划类型")
    private ProductPlanTypeEnum type;

    @ApiModelProperty("产品Id")
    private Long productId;

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

    @ApiModelProperty("生产工艺数量")
    private Integer processNum;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("生产执行流程实例")
    private String executeProcessInstanceId;

    @ApiModelProperty("确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty("工艺模型id")
    private String processModelId;

    @ApiModelProperty("工艺版本号")
    private Long processVersionId;

    @ApiModelProperty("产线名称")
    private String lineName;

}
