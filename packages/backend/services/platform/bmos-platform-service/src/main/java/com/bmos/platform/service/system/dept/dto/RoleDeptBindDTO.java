package com.bmos.platform.service.system.dept.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色绑定部门
 */
@ApiModel("角色绑定部门")
@Getter
@Setter
public class RoleDeptBindDTO {

    @ApiModelProperty("角色id")
    @NotNull
    private Long id;

    @ApiModelProperty("部门id集合")
    private List<Long> deptIdList;

}
