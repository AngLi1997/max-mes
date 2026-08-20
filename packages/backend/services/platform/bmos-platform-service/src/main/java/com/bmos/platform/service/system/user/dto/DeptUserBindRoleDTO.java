package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 部门管理中用户绑定角色
 */
@Getter
@Setter
@ApiModel("部门管理中用户绑定角色")
public class DeptUserBindRoleDTO {

    @ApiModelProperty("用户的userId")
    @NotNull
    private String userId;

    @ApiModelProperty("所选中的角色id集合")
    private List<Long> roleIds;

    @ApiModelProperty("前端所能展示的所有的角色id集合")
    @NotEmpty
    private List<Long> allRoleIds;
}
