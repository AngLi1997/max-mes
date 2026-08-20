package com.bmos.mes.service.plan.rule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("CodeRuleUpdateDTO:编码规则更新Dto")
public class CodeRuleUpdateDTO {
    @ApiModelProperty("生产计划编码规则id")
    private Long id;

    @NotEmpty
    @ApiModelProperty("编码类型")
    private String type;

    @NotNull
    @ApiModelProperty("生产工艺id")
    private Long processId;

    @NotEmpty
    @ApiModelProperty("编码规则code")
    private String codeRuleCode;

    @NotEmpty
    @ApiModelProperty("编码规则名称")
    private String codeRuleName;
}
