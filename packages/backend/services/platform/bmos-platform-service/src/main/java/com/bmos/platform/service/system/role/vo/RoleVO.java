package com.bmos.platform.service.system.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("角色VO")
@Getter
@Setter
@ToString
public class RoleVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色类型id")
    private String roleTypeId;

    @ApiModelProperty("角色类型")
    private String roleTypeName;

    @ApiModelProperty("描述")
    private String description;
}
