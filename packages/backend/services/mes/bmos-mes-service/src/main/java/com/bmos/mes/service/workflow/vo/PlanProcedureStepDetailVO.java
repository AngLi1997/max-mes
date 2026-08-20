package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("计划工步详情VO")
@Data
public class PlanProcedureStepDetailVO {

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品合并编码")
    private String productMergeCode;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工步名称")
    private String procedureStepName;

}
