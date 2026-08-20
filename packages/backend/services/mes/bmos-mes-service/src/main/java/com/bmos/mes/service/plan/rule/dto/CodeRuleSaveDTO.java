package com.bmos.mes.service.plan.rule.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.plan.CodeRuleTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ApiModel("CodeRuleSaveDTO:编码规则保存Dto")
public class CodeRuleSaveDTO {
    @EnumValidate(value = CodeRuleTypeEnum.class)
    @ApiModelProperty("生产计划编码规则分类 PRODUCT_PLAN_NO 生产计划批号规则 PRODUCT_PLAN_BATCH_NO 生产批号规则")
    private String type;

    @NotEmpty
    @ApiModelProperty("编码规则code")
    private String codeRuleCode;

    @NotEmpty
    @ApiModelProperty("编码规则名称")
    private String codeRuleName;

    @NotEmpty
    @ApiModelProperty("生产工艺id")
    private List<Long> processIds;
}
