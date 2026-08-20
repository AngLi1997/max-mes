package com.bmos.platform.service.system.role.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("角色菜单VO")
public class RoleMenuIdVO {

    private Long rootMenuId;

    /**
     * 功能列表id
     */
    private List<Long> funcIds;

    /**
     * 菜单id集合
     */
    private List<Long> menuIds;
}
