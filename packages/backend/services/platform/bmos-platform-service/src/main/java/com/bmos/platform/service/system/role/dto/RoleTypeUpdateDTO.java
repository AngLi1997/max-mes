package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("角色类型编辑DTO")
@Getter
@Setter
@ToString
public class RoleTypeUpdateDTO{

    @ApiModelProperty("角色类型id")
    @NotNull
    private Long id;

    @ApiModelProperty("角色类型名称")
    private String roleTypeName;

}
