package com.bmos.platform.service.system.role.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleMenuMapper extends BaseMapperX<AuthRoleMenu> {

    default List<AuthRoleMenu> selectAuthRoleIdsByMenuId(Long menuId){
        return selectList(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getMenuId, menuId));
    }

    default void deleteByMenuIdAndRoleIds(Long rootMenuId, List<Long> roleIds){
        delete(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getMenuId,rootMenuId).in(AuthRoleMenu::getRoleId,roleIds));
    }

    default void deleteByMenuId(Long menuId){
        delete(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getMenuId,menuId));
    }

    List<Menu> selectRootMenuListByUserId(@Param("userId") String userId, @Param("maxRootId") Long maxRootId);

    List<Menu> selectMenuListByUserId(@Param("userId") String userId,
                                      @Param("rootMenuCode") String rootMenuCode,
                                      @Param("maxRootId") Long maxRootId,
                                      @Param("containsFunc") Boolean containsFunc);

    default List<AuthRoleMenu> selectRoleIdByMenuId(Long menuId){
         return selectList(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getMenuId, menuId));
    }

    default void deleteByMenuIds(List<Long> menuIds){
        delete(new LambdaQueryWrapperX<AuthRoleMenu>().in(AuthRoleMenu::getMenuId,menuIds));
    }

    default List<AuthRoleMenu> selectByMenuIdLikeAndRoleIds(Long parentMenuId, List<Long> roleIds){
        return selectList(new LambdaQueryWrapperX<AuthRoleMenu>()
                .in(AuthRoleMenu::getRoleId,roleIds)
                .ge(AuthRoleMenu::getMenuId,parentMenuId * 1000L)
                .le(AuthRoleMenu::getMenuId,parentMenuId * 1000L + 999L));
    }

    List<Long> selectByRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 删除角色绑定的菜单id集合
     * @param menuIds
     * @param roleIds
     */
    default void deleteByRoleIdsMenuIds(List<Long> menuIds, List<Long> roleIds){
        delete(new LambdaQueryWrapperX<AuthRoleMenu>()
                .in(AuthRoleMenu::getMenuId,menuIds)
                .in(AuthRoleMenu::getRoleId,roleIds));
    }

    /**
     * 查询roleIdList下哪些有menuIdList的权限
     * @param menuIdList
     * @param roleIdList
     * @return
     */
    default List<AuthRoleMenu> selectByRoleMenuIdList(List<Long> menuIdList, List<Long> roleIdList){
        return selectList(new LambdaQueryWrapperX<AuthRoleMenu>()
                .in(AuthRoleMenu::getMenuId,menuIdList)
                .in(AuthRoleMenu::getRoleId,roleIdList));
    }

    /**
     * 查询菜单id集合下的角色id集合
     * @param menuIdList
     * @return
     */
    default List<AuthRoleMenu> selectRoleIdByMenuIdList(List<Long> menuIdList){
        return selectList(new LambdaQueryWrapperX<AuthRoleMenu>().in(AuthRoleMenu::getMenuId,menuIdList));
    }

    default List<AuthRoleMenu> selectByRoleId(Long roleId){
        return selectList(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getRoleId,roleId));
    }

    default void deleteByRoleId(Long roleId){
        delete(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getRoleId,roleId));
    }

    default void deleteByRoleIdAndMenuIds(Long roleId, Collection<Long> deleteMenuIds){
        delete(new LambdaQueryWrapperX<AuthRoleMenu>().eq(AuthRoleMenu::getRoleId,roleId).in(AuthRoleMenu::getMenuId,deleteMenuIds));
    }
}
