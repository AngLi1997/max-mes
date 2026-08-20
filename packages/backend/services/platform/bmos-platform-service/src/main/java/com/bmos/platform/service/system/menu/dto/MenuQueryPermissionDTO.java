package com.bmos.platform.service.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("菜单带有权限的查询DTO")
@Getter
@Setter
@ToString
public class MenuQueryPermissionDTO {

    @ApiModelProperty("模糊查询字段")
    private String name;

    @ApiModelProperty("终端类型")
    @NotNull
    private Long typeId;
}
