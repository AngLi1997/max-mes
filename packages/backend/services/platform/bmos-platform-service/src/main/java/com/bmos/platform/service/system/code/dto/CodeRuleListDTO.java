package com.bmos.platform.service.system.code.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("CodeRuleListDTO:规则编码列表")
public class CodeRuleListDTO {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private Boolean status;
}
