package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("角色编辑DTO")
@Getter
@Setter
@ToString
public class RoleUpdateDTO{

    @ApiModelProperty("角色名称id")
    @NotNull
    private Long id;

    @ApiModelProperty("角色名称")
    @NotBlank
    private String roleName;

    @ApiModelProperty("角色分类id")
    @NotNull
    private Long roleTypeId;

    @ApiModelProperty("描述")
    private String description;
}
