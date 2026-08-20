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
@ApiModel("CodeRulePageDTO:编码规则列表分页")
public class CodeRulePageDTO extends BasePage {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @JsonIgnore
    @ApiModelProperty("部门Id")
    private List<Long> deptIds;
}
