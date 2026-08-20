package com.bmos.platform.service.system.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("角色全量VO")
@Getter
@Setter
@ToString
public class RoleTypeTreeItemVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色类型id")
    private Long roleTypeId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("名称")
    private String name;

}
