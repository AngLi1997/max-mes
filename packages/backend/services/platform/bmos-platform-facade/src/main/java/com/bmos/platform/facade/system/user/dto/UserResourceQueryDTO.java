package com.bmos.platform.facade.system.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户根据资源id以及菜单id查询具有数据权限以及权限码的用户DTO
 */
@Data
public class UserResourceQueryDTO {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 部门id集合
     */
    @NotNull
    private Long resourceId;

}
