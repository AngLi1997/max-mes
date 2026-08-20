package com.bmos.platform.service.system.role.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface RoleRelateMenuMapper extends BaseMapperX<RoleRelateMenu> {

    default List<RoleRelateMenu> getListByRoleId(Long roleId, Collection<Long> menuIdList) {
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>()
                .eq(RoleRelateMenu::getRoleId, roleId)
                .in(RoleRelateMenu::getMenuId, menuIdList));
    }

    default List<RoleRelateMenu> getList(List<Long> roleIdList) {
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>()
                .in(RoleRelateMenu::getRoleId, roleIdList));
    }

    default List<RoleRelateMenu> getListByMenuId(Long menuId) {
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getMenuId, menuId));
    }

    default void deleteByMenuId(Long menuId) {
        delete(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getMenuId, menuId));
    }

    default void deleteByRoleId(Long roleId) {
        delete(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getRoleId, roleId));
    }

    default List<RoleRelateMenu> getByRoleId(Long roleId){
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getRoleId,roleId));
    }

    default Boolean existsUser(Long roleId){
        return exists(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getRoleId,roleId).last("limit 1"));
    }

    default void deleteByRoleIdAndRootMenuIds(Long roleId, Collection<Long> rootMenuIds){
        delete(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getRoleId,roleId).in(RoleRelateMenu::getMenuId,rootMenuIds));
    }

    default void deleteByMenuIdAndRoleIds(Long menuId, List<Long> roleIds){
        delete(new LambdaQueryWrapperX<RoleRelateMenu>().eq(RoleRelateMenu::getMenuId,menuId).in(RoleRelateMenu::getRoleId,roleIds));
    }

    default void deleteByMenuIds(List<Long> menuIds){
        delete(new LambdaQueryWrapperX<RoleRelateMenu>().in(RoleRelateMenu::getMenuId,menuIds));
    }

    default List<RoleRelateMenu> selectByMenuIdLikeAndRoleIds(Long parentMenuId, List<Long> roleIds){
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>()
                .in(RoleRelateMenu::getRoleId,roleIds)
                .ge(RoleRelateMenu::getMenuId,parentMenuId * 1000L)
                .le(RoleRelateMenu::getMenuId,parentMenuId * 1000L + 999L));
    }

    default List<RoleRelateMenu> selectByMenuId(Long menuId){
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>()
                .eq(RoleRelateMenu::getMenuId, menuId));
    }

    /**
     * 根据菜单id集合查询角色
     * @param menuIdList
     * @return
     */
    default List<RoleRelateMenu> selectByMenuIdList(Collection<Long> menuIdList){
        return selectList(new LambdaQueryWrapperX<RoleRelateMenu>()
                .in(RoleRelateMenu::getMenuId, menuIdList));
    }
}
