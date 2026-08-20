package com.bmos.platform.service.system.menu.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.facade.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.menu.dto.MenuSaveDTO;
import com.bmos.platform.service.system.menu.dto.MenuUpdateDTO;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.menu.vo.FunctionVO;
import com.bmos.platform.service.system.menu.vo.MenuListVO;
import com.bmos.platform.service.system.menu.vo.MenuTreeVO;
import com.bmos.platform.service.system.role.dto.RoleRelateMenuSaveItemDTO;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface MenuConvert {
    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    Menu convert(MenuSaveDTO dto);

    Menu convert(MenuUpdateDTO dto);

    List<MenuListVO> convert(List<Menu> menus);

    default List<RoleRelateMenu> convertRoleMenuList(RoleRelateMenuSaveItemDTO dto) {
        List<RoleRelateMenu> result = new ArrayList<>();
        for (Long menuId : dto.getMenuIds()) {
            for (Long roleId : dto.getRoleIds()) {
                RoleRelateMenu roleRelateMenu = new RoleRelateMenu();
                roleRelateMenu.setMenuId(menuId);
                roleRelateMenu.setRoleId(roleId);
                result.add(roleRelateMenu);
            }
        }
        return result;
    }

    List<MenuTreeVO> convertTreeNode(List<Menu> list);

    default List<RoleRelateMenu> convert(Long rootMenuId, List<Long> roleIds) {
        return roleIds.stream()
                .map(e -> {
                    RoleRelateMenu roleRelateMenu = new RoleRelateMenu();
                    roleRelateMenu.setMenuId(rootMenuId);
                    roleRelateMenu.setRoleId(e);
                    return roleRelateMenu;
                }).collect(Collectors.toList());
    }

    default List<AuthRoleMenu> convertAuthRoleMenuList(RoleRelateMenuSaveItemDTO dto){

        List<AuthRoleMenu> result = new ArrayList<>();
        for (Long menuId : dto.getMenuIds()) {
            for (Long roleId : dto.getRoleIds()) {
                AuthRoleMenu roleRelateMenu = new AuthRoleMenu();
                roleRelateMenu.setMenuId(menuId);
                roleRelateMenu.setRoleId(roleId);
                result.add(roleRelateMenu);
            }
        }
        return result;
    }

    default List<AuthRoleMenu> convertAuthRoleMenu(Long rootMenuId, List<Long> roleIds){
        return roleIds.stream()
                .map(e -> {
                    AuthRoleMenu roleRelateMenu = new AuthRoleMenu();
                    roleRelateMenu.setMenuId(rootMenuId);
                    roleRelateMenu.setRoleId(e);
                    return roleRelateMenu;
                }).collect(Collectors.toList());
    }

    List<MenuListVO> convertList(List<Menu> menus);

    default List<FunctionVO> convert2MenuVOList(List<Menu> menuList, Set<Long> roleMenuIdSet){
        List<FunctionVO> functionVOS = new ArrayList<>();
        if (CollUtil.isEmpty(menuList)){
            return functionVOS;
        }
        for (Menu menu : menuList) {
            FunctionVO functionVO = new FunctionVO();
            functionVO.setId(menu.getId());
            functionVO.setName(menu.getName());
            functionVO.setParentId(menu.getParentId());
            functionVO.setFlag(roleMenuIdSet.contains(menu.getId()));
            functionVOS.add(functionVO);
        }
        return functionVOS;
    }

    List<MenuTreeVO> convert2MenuTreeVO(List<Menu> menus);

    default List<MenuVO> convert2MenuVO(List<MenuTreeVO> children){
        if (CollUtil.isEmpty(children)){
            return new ArrayList<>();
        }
        List<MenuVO> menuVOS = new ArrayList<>();
        for (MenuTreeVO menuTreeVO : children) {
            MenuVO menuVO = new MenuVO();
            menuVO.setId(menuTreeVO.getId());
            menuVO.setName(menuTreeVO.getName());
            menuVOS.add(menuVO);
            if (CollUtil.isNotEmpty(menuTreeVO.getChildren())){
                menuVOS.addAll(convert2MenuVO(menuTreeVO.getChildren()));
            }
        }
        return menuVOS;
    }

    List<MenuTreeVO> convert2VO(List<Menu> list);
}
