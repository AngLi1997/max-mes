package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("角色保存DTO")
@Getter
@Setter
@ToString
public class RoleSaveDTO{

    @ApiModelProperty("角色名称")
    @NotBlank
    private String roleName;

    @ApiModelProperty("角色分类id")
    @NotNull
    private Long roleTypeId;

    @ApiModelProperty("描述")
    private String description;
}
