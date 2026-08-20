package com.bmos.platform.service.system.role.service;

import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AuthRoleMenuService {

    List<Long> getAuthRoleIdsByMenuId(Long menuId);

    void deleteByMenuIdAndRoleIds(Long rootMenuId, List<Long> roleIds);

    void deleteByMenuId(Long menuId);

    void saveBatch(List<AuthRoleMenu> authRoleMenus);

    List<Menu> getRootMenuListByUserId(String userId, Long maxRootId);

    List<Menu> getMenuListByUserId(String userId, String rootMenuCode, Boolean containsFunc);

    List<Long> getRoleIdByMenuId(Long menuId);

    /**
     * 查询当前角色是否有菜单权限
     * @param roleIdList
     * @return
     */
    Set<Long> selectByRoleIdList(List<Long> roleIdList);

    /**
     * 查询roleIdList下哪些有menuIdList的权限
     * @param menuIdList
     * @param roleIdList
     * @return
     */
    List<AuthRoleMenu> selectByRoleMenuIdList(List<Long> menuIdList, List<Long> roleIdList);

    /**
     * 查询菜单id下所有的角色
     * @param menuIdList
     * @return
     */
    Set<Long> selectRoleByMenuIdList(List<Long> menuIdList);

    /**
     * 查询角色id下的所有菜单
     * @param roleId
     * @return
     */
    List<AuthRoleMenu> selectByRoleId(Long roleId);

    /**
     * 根据角色id删除授权菜单
     * @param roleId
     */
    void deleteByRoleId(Long roleId);

    /**
     * 绑定角色与权限授权菜单
     * @param roleId
     * @param bindMenuIds
     */
    void bindRoleAuthMenu(Long roleId, Set<Long> bindMenuIds);

    /**
     * 删除角色id与权限菜单的绑定关系
     * @param roleId
     * @param deleteMenuIds
     */
    void deleteByRoleIdAndMenuIds(Long roleId, Collection<Long> deleteMenuIds);
}
