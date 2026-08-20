package com.bmos.platform.service.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("菜单更新DTO")
@Getter
@Setter
@ToString
public class MenuUpdateDTO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("上级id")
    private String parentId;

    @ApiModelProperty("是否是菜单")
    private Integer isMenu;

    @ApiModelProperty("排序号")
    private Integer sort;
}
