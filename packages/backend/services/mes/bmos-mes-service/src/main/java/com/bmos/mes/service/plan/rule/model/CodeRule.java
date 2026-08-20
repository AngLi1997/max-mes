package com.bmos.mes.service.plan.rule.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.plan.CodeRuleTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

/**
* 生产计划编码规则
*/
@Getter
@Setter
@SuperBuilder
@With
@AllArgsConstructor
@ToString
@TableName(value = "bm_product_plan_code_rule")
public class CodeRule extends BaseDO {
    @Tolerate
    public CodeRule() {}
    @ApiModelProperty("生产计划编码规则分类")
    private CodeRuleTypeEnum type;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("编码规则code")
    private String codeRuleCode;

    @ApiModelProperty("编码规则名称")
    private String codeRuleName;
}
