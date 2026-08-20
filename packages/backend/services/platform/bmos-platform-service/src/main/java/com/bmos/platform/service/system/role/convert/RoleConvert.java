package com.bmos.platform.service.system.role.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.role.dto.RoleMenuSaveDTO;
import com.bmos.platform.service.system.role.dto.RoleMenuSaveItemDTO;
import com.bmos.platform.service.system.role.dto.RoleSaveDTO;
import com.bmos.platform.service.system.role.dto.RoleTypeSaveDTO;
import com.bmos.platform.service.system.role.dto.RoleTypeUpdateDTO;
import com.bmos.platform.service.system.role.dto.RoleUpdateDTO;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import com.bmos.platform.service.system.role.model.RoleType;
import com.bmos.platform.service.system.role.vo.RoleTreeNodeVO;
import com.bmos.platform.service.system.role.vo.RoleTypeTreeItemVO;
import com.bmos.platform.service.system.role.vo.RoleTypeVO;
import com.bmos.platform.service.system.role.vo.RoleVO;
import com.bmos.platform.service.system.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface RoleConvert {
    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    Role convert(RoleSaveDTO dto);

    Role convert(RoleUpdateDTO dto);

    RoleType convert(RoleTypeSaveDTO dto);

    RoleType convert(RoleTypeUpdateDTO dto);

    List<RoleVO> convert(List<RoleTypeVO> vo);

    default List<RoleRelateMenu> convert(RoleMenuSaveDTO dto) {
        List<RoleRelateMenu> roleRelateMenus = new ArrayList<>();
        for (RoleMenuSaveItemDTO item : dto.getItems()) {
            if(CollUtil.isNotEmpty(item.getMenuIds())){
                RoleRelateMenu roleRelateRootMenu = new RoleRelateMenu();
                roleRelateRootMenu.setRoleId(dto.getRoleId());
                roleRelateRootMenu.setMenuId(item.getRootMenuId());
                roleRelateMenus.add(roleRelateRootMenu);
                for (Long menuId : item.getMenuIds()) {
                    RoleRelateMenu roleRelateMenu = new RoleRelateMenu();
                    roleRelateMenu.setRoleId(dto.getRoleId());
                    roleRelateMenu.setMenuId(menuId);
                    roleRelateMenus.add(roleRelateMenu);
                }
            }
        }
        return roleRelateMenus;
    }

    default List<RoleRelateMenu> convert(Long roleId, Collection<Long> menuIds) {
        if (CollUtil.isEmpty(menuIds)){
            return Collections.emptyList();
        }
        List<RoleRelateMenu>  roleRelateMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleRelateMenu roleRelateMenu = new RoleRelateMenu();
            roleRelateMenu.setRoleId(roleId);
            roleRelateMenu.setMenuId(menuId);
            roleRelateMenus.add(roleRelateMenu);
        }
        return roleRelateMenus;
    }

    default List<RoleTreeNodeVO> convertTreeNode(List<Role> roles, List<RoleType> roleTypes) {
        List<RoleTreeNodeVO> typeNodes = roleTypes.stream().map(e -> {
            RoleTreeNodeVO node = new RoleTreeNodeVO();
            node.setId(e.getId());
            node.setParentId(e.getParentId());
            node.setName(e.getRoleTypeName());
            node.setRoleTypeFlag(true);
            node.setCreateTime(e.getCreateTime());
            return node;
        }).collect(Collectors.toList());
        List<RoleTreeNodeVO> roleNodes = convertTreeNode(roles);
        typeNodes.addAll(roleNodes);
        return typeNodes;
    }

    default List<RoleTreeNodeVO> convertTreeNode(List<Role> roles) {
        return roles.stream().map(e -> {
            RoleTreeNodeVO node = new RoleTreeNodeVO();
            node.setId(e.getId());
            node.setParentId(e.getRoleTypeId());
            node.setName(e.getRoleName());
            node.setRoleTypeFlag(false);
            node.setCreateTime(e.getCreateTime());
            return node;
        }).collect(Collectors.toList());
    }

    List<RoleVO> convertList(List<Role> roles);

    RoleVO convertVO(Role role);

    List<RoleTypeTreeItemVO> convert2RoleTypeVOList(List<Role> roles);

    default List<FeignUserVO> convert2FeignUserVO(List<User> userList){
        if (CollUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        List<FeignUserVO> feignUserVOS = new ArrayList<>();
        for (User user : userList) {
            FeignUserVO feignUserVO = new FeignUserVO();
            feignUserVO.setUserId(user.getUserId());
            feignUserVO.setUserName(user.getUserName());
            feignUserVO.setLoginName(user.getLoginName());
            feignUserVOS.add(feignUserVO);
        }
        return feignUserVOS;
    }

    default List<AuthRoleMenu> convert2AuthRoleMenu(Set<Long> bindMenuIds, Long roleId){
        List<AuthRoleMenu> authRoleMenuList = new ArrayList<>();
        if (CollUtil.isEmpty(bindMenuIds)){
            return authRoleMenuList;
        }
        for (Long menuId : bindMenuIds) {
            AuthRoleMenu authRoleMenu = new AuthRoleMenu();
            authRoleMenu.setRoleId(roleId);
            authRoleMenu.setMenuId(menuId);
            authRoleMenuList.add(authRoleMenu);
        }
        return authRoleMenuList;
    }
}
