package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("角色关联菜单保存子数据DTO")
@Getter
@Setter
@ToString
public class RoleRelateMenuSaveItemDTO {

    @ApiModelProperty("菜单id")
    @NotEmpty
    private List<Long> menuIds;

    @ApiModelProperty("菜单id 集合")
    @NotEmpty
    private List<Long> roleIds;

    @ApiModelProperty("是否是菜单")
    @NotNull
    private Boolean isMenu;

    @ApiModelProperty("删除的角色id 集合")
    private List<Long> deletedRoleIds;
}
