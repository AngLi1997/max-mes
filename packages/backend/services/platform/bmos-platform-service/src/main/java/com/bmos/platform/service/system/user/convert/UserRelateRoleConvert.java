package com.bmos.platform.service.system.user.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.service.system.user.dto.UserRelateRoleSaveItemDTO;
import com.bmos.platform.service.system.user.model.UserRelateRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserRelateRoleConvert {
    UserRelateRoleConvert INSTANCE = Mappers.getMapper(UserRelateRoleConvert.class);

    List<UserRelateRole> convert(List<UserRelateRoleSaveItemDTO> dto);

    default List<UserRelateRole> convert2RelateRole(String userId, List<Long> roleIds){
        if (CollUtil.isEmpty(roleIds)){
            return new ArrayList<>();
        }
        List<UserRelateRole> userRelateRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRelateRole userRelateRole = new UserRelateRole();
            userRelateRole.setUserId(userId);
            userRelateRole.setRoleId(roleId);
            userRelateRoles.add(userRelateRole);
        }
        return userRelateRoles;
    }
}
