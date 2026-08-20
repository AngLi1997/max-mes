package com.bmos.platform.facade.system.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户查询Feign接口DTO
 */
@Data
public class UserQueryDTO {

    /**
     * 菜单id
     */
    @NotNull
    private Long menuId;

    /**
     * 部门id集合
     */
    @NotNull
    private List<Long> deptIds;

}
