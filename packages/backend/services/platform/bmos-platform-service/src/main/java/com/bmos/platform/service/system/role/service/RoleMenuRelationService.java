package com.bmos.platform.service.system.role.service;

import com.bmos.platform.service.system.role.model.RoleRelateMenu;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RoleMenuRelationService {
    void deleteByMenuId(Long menuId);

    void saveBatch(List<RoleRelateMenu> list);

    void deleteByRoleId(Long roleId);

    void deleteByIds(List<Long> ids);

    List<RoleRelateMenu> getByRoleId(Long roleId);

    Boolean existUser(Long roleId);

    void deleteByRoleIdAndRootMenuIds(Long roleId, Collection<Long> rootMenuIds);

    void deleteByMenuIdAndRoleIds(Long menuId, List<Long> roleIds);

    void deleteByMenuIds(List<Long> menuIds);

    List<RoleRelateMenu> selectByMenuIdLikeAndRoleIds(Long parentMenu, List<Long> roleIds);

    List<RoleRelateMenu> getByMenuId(Long menuId);

    /**
     * 获取当前角色id在menuIds下的拥有的授权菜单
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    List<RoleRelateMenu> selectByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds);

}