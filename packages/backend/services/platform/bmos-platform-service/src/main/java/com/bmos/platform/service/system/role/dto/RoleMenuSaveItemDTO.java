package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("角色菜单保存itemDTO")
public class RoleMenuSaveItemDTO {

    @ApiModelProperty("根节点菜单id")
    @NotNull
    private Long rootMenuId;

    @ApiModelProperty("将原有进行删除的功能列表id")
    private List<Long> delFuncIds;

    @ApiModelProperty("菜单集合")
    private List<Long> menuIds;

}
