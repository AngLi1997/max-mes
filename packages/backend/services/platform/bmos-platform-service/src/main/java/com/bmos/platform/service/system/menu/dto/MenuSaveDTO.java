package com.bmos.platform.service.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("菜单新增DTO")
@Getter
@Setter
@ToString
public class MenuSaveDTO {

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("是否是菜单")
    private Integer isMenu;

    @ApiModelProperty("排序号")
    private Integer sort;
}
