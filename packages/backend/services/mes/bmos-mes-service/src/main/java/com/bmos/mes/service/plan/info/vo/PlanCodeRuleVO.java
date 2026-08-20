package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("PlanCodeRuleVO:生产计划参数传参")
public class PlanCodeRuleVO {
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("计划类型")
    private String productPlanType;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("产线code")
    private String productionLineCode;

    @ApiModelProperty("产品阶段代码")
    private String productionStageCode;
}
