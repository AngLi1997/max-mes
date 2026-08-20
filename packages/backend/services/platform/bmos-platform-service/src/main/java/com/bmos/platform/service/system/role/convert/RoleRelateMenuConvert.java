package com.bmos.platform.service.system.role.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.service.system.role.dto.RoleRelateMenuSaveItemDTO;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface RoleRelateMenuConvert {
    RoleRelateMenuConvert INSTANCE = Mappers.getMapper(RoleRelateMenuConvert.class);

    List<RoleRelateMenu> convert(List<RoleRelateMenuSaveItemDTO> dto);

    default List<RoleRelateMenu> convert2RoleRelate(Long roleId, Collection<Long> bindMenuIds){
        List<RoleRelateMenu> roleRelateMenuList = new ArrayList<>();
        if (CollUtil.isEmpty(bindMenuIds)){
            return roleRelateMenuList;
        }
        for (Long bindMenuId : bindMenuIds) {
            RoleRelateMenu roleRelateMenu = new RoleRelateMenu();
            roleRelateMenu.setRoleId(roleId);
            roleRelateMenu.setMenuId(bindMenuId);
            roleRelateMenuList.add(roleRelateMenu);
        }
        return roleRelateMenuList;
    }
}
