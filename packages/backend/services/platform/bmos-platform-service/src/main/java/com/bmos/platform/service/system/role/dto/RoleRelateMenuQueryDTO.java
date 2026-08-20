package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("角色关联菜单查询DTO")
@Getter
@Setter
@ToString
public class RoleRelateMenuQueryDTO {

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("菜单id")
    private Long menuId;

}
