package com.bmos.platform.service.system.menu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("菜单VO")
@Getter
@Setter
@ToString
public class MenuVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("是否是菜单")
    private Boolean isMenu;

    @ApiModelProperty("排序号")
    private String sort;

    @ApiModelProperty("终端类型")
    private Integer type;

}
