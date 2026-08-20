package com.bmos.platform.service.system.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* 编码规则主表
*/
@Getter
@Setter
@ApiModel("CodeRulePermissionDTO:编码规则数据权限DTO")
public class CodeRulePermissionDTO {
    @NotNull
    @ApiModelProperty(value = "编码规则ID", required = true)
    private Long codeRuleId;

    @NotEmpty
    @ApiModelProperty(value = "deptIds", required = true)
    private List<Long> deptIds;
}
