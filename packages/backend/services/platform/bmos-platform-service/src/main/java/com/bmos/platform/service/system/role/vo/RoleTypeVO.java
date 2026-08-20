package com.bmos.platform.service.system.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("角色类型VO")
@Getter
@Setter
@ToString
public class RoleTypeVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色类型名称")
    private String roleTypeName;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("是否有子结构")
    private Boolean flag;
}
