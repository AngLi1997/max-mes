package com.bmos.lims2.server.platform.system.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CodeRulePageVO:编码规则列表分页")
public class CodeRuleVO {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;
}
