package com.bmos.platform.service.system.dept.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 部门绑定角色
 */
@ApiModel("部门绑定角色")
@Getter
@Setter
public class DeptRoleBindDTO {

    @ApiModelProperty("部门id")
    @NotNull
    private Long id;

    @ApiModelProperty("角色id集合")
    private List<Long> roleIdList;

}
