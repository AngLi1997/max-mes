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
@ApiModel("角色权限授权菜单保存DTO")
public class RoleAuthMenuSaveDTO {

    @ApiModelProperty("角色id")
    @NotNull
    private Long roleId;

    @ApiModelProperty("菜单集合")
    @NotEmpty
    private List<RoleMenuSaveItemDTO> items;

}
