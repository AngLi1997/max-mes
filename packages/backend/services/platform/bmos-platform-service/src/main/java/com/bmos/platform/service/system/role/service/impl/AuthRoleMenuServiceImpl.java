package com.bmos.platform.service.system.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.platform.service.system.menu.mapper.MenuMapper;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.role.convert.RoleConvert;
import com.bmos.platform.service.system.role.mapper.AuthRoleMenuMapper;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;
import com.bmos.platform.service.system.role.service.AuthRoleMenuService;
import com.bmos.platform.service.system.user.mapper.UserRelateRoleMapper;
import com.bmos.platform.service.system.user.model.UserRelateRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuMapper authRoleMenuMapper;

    @Autowired
    private UserRelateRoleMapper userRelateRoleMapper;

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public List<Long> getAuthRoleIdsByMenuId(Long menuId) {
        List<AuthRoleMenu> authRoleMenus = authRoleMenuMapper.selectAuthRoleIdsByMenuId(menuId);
        if (CollUtil.isEmpty(authRoleMenus)){
            return Collections.emptyList();
        }
        return CollectionUtils.convertList(authRoleMenus,AuthRoleMenu::getRoleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuIdAndRoleIds(Long rootMenuId, List<Long> roleIds) {
        authRoleMenuMapper.deleteByMenuIdAndRoleIds(rootMenuId,roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuId(Long menuId) {
        authRoleMenuMapper.deleteByMenuId(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<AuthRoleMenu> authRoleMenus) {
        authRoleMenuMapper.insertBatch(authRoleMenus);
    }

    @Override
    public List<Menu> getRootMenuListByUserId(String userId, Long maxRootId) {
        return authRoleMenuMapper.selectRootMenuListByUserId(userId,maxRootId);
    }

    @Override
    public List<Menu> getMenuListByUserId(String userId, String rootMenuCode, Boolean containsFunc) {
        // 查询当前用户所的角色
        List<UserRelateRole> userRelateRoles = userRelateRoleMapper.selectByUserId(userId);
        if (CollUtil.isEmpty(userRelateRoles)){
            return Collections.emptyList();
        }
        List<Long> roleIdList = CollectionUtils.convertList(userRelateRoles,UserRelateRole::getRoleId);
        // 查询当前角色绑定的所有菜单
        Set<Long> menuIdSet = this.selectByRoleIdList(roleIdList);
        // 查询当前所有菜单
        if (CollUtil.isEmpty(menuIdSet)){
            return Collections.emptyList();
        }
        List<Menu> menus = menuMapper.selectBatchIds(menuIdSet);
        return !containsFunc ? menus.stream().filter(menu -> menu.getIsMenu().equals(1)).collect(Collectors.toList()) : menus;
    }

    @Override
    public List<Long> getRoleIdByMenuId(Long menuId) {
        List<AuthRoleMenu> authRoleMenus = authRoleMenuMapper.selectRoleIdByMenuId(menuId);
        if (CollUtil.isEmpty(authRoleMenus)){
            return Collections.emptyList();
        }
        return CollectionUtils.convertList(authRoleMenus,AuthRoleMenu::getRoleId);
    }

    @Override
    public Set<Long> selectByRoleIdList(List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)){
            return new HashSet<>();
        }
        List<Long> authRoleMenus = authRoleMenuMapper.selectByRoleIdList(roleIdList);
        return new HashSet<>(authRoleMenus);
    }


    @Override
    public List<AuthRoleMenu> selectByRoleMenuIdList(List<Long> menuIdList, List<Long> roleIdList) {
        return authRoleMenuMapper.selectByRoleMenuIdList(menuIdList,roleIdList);
    }

    @Override
    public Set<Long> selectRoleByMenuIdList(List<Long> menuIdList) {
        if (CollUtil.isEmpty(menuIdList)){
            return new HashSet<>();
        }
        List<AuthRoleMenu> authRoleMenus = authRoleMenuMapper.selectRoleIdByMenuIdList(menuIdList);
        if (CollUtil.isEmpty(authRoleMenus)){
            return new HashSet<>();
        }
        return authRoleMenus.stream().map(AuthRoleMenu::getRoleId).collect(Collectors.toSet());
    }

    @Override
    public List<AuthRoleMenu> selectByRoleId(Long roleId) {
        return authRoleMenuMapper.selectByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        authRoleMenuMapper.deleteByRoleId(roleId);
    }

    @Override
    public void bindRoleAuthMenu(Long roleId, Set<Long> bindMenuIds) {
        List<AuthRoleMenu> authRoleMenuList = RoleConvert.INSTANCE.convert2AuthRoleMenu(bindMenuIds,roleId);
        if (CollUtil.isEmpty(authRoleMenuList)){
            return ;
        }
        authRoleMenuMapper.insertBatch(authRoleMenuList);
    }

    @Override
    public void deleteByRoleIdAndMenuIds(Long roleId, Collection<Long> deleteMenuIds) {
        if (CollUtil.isEmpty(deleteMenuIds)){
            return ;
        }
        authRoleMenuMapper.deleteByRoleIdAndMenuIds(roleId, deleteMenuIds);
    }

}
