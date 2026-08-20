package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("角色类型校验")
public class RoleTypeValidateDTO {

    @ApiModelProperty("id,用于编辑时的校验")
    private Long id;

    @ApiModelProperty(value = "角色类型名称",required = true)
    @NotNull
    private String roleTypeName;

    @ApiModelProperty(value = "上级id", required = true)
    @NotNull
    private Long parentId;
}
