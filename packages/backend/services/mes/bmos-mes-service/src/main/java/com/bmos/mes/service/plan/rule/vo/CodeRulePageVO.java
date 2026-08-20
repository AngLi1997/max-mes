package com.bmos.mes.service.plan.rule.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CodeRulePageVO:生产计划编码规则分页VO")
public class CodeRulePageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("编码规则code")
    private String codeRuleCode;

    @ApiModelProperty("编码规则名称")
    private String codeRuleName;
}
