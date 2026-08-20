package com.bmos.platform.service.system.code.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CodeRuleVersionPageDTO:编码规则版本列表分页")
public class CodeRuleVersionPageDTO extends BasePage {
    @ApiModelProperty("规则编码")
    private String code;
}
