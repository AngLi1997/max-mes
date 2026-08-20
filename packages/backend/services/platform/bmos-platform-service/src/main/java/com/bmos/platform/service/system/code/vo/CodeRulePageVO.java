package com.bmos.platform.service.system.code.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CodeRulePageVO:编码规则列表分页")
public class CodeRulePageVO  {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("启用版本")
    private String version;
}
