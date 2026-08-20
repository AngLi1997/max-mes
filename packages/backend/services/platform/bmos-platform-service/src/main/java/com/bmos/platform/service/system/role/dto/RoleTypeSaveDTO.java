package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("角色类型保存DTO")
@Getter
@Setter
@ToString
public class RoleTypeSaveDTO{

    @ApiModelProperty("父级id")
    @NotNull
    private Long parentId;

    @ApiModelProperty("角色分类名称")
    @NotBlank
    private String roleTypeName;

}
