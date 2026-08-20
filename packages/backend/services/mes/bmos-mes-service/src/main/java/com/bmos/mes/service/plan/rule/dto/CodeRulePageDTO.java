package com.bmos.mes.service.plan.rule.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CodeRulePageDTO:编号规则分页查询DTO")
public class CodeRulePageDTO extends BasePage {
    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("类型 生产批号规则 PRODUCT_PLAN_BATCH_NO 生产计划批号规则 PRODUCT_PLAN_NO")
    private String type;
}
