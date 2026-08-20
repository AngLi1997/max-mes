package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("角色关联菜单保存DTO")
@Getter
@Setter
@ToString
public class RoleRelateMenuSaveDTO {

    @ApiModelProperty("tab的id")
    private Long tabId;

    @ApiModelProperty("角色id")
    @NotNull
    private Long roleId;

    @ApiModelProperty("子数据的集合")
    private List<RoleRelateMenuSaveItemDTO> items;

}
