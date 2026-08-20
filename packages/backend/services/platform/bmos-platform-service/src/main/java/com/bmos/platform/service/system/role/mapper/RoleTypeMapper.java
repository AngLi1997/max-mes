package com.bmos.platform.service.system.role.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.role.dto.RoleTypeValidateDTO;
import com.bmos.platform.service.system.role.model.RoleType;
import com.bmos.platform.service.system.role.vo.RoleTypeTreeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RoleTypeMapper extends BaseMapperX<RoleType> {

    default Boolean existsSubType(Long id){
        return exists(new LambdaQueryWrapperX<RoleType>().eq(RoleType::getParentId,id));
    }

    default Boolean validateRoleType(RoleTypeValidateDTO dto){
        return exists(new LambdaQueryWrapperX<RoleType>()
                .eq(RoleType::getParentId,dto.getParentId())
                .eq(RoleType::getRoleTypeName,dto.getRoleTypeName())
                .neIfPresent(RoleType::getId,dto.getId()));
    }
}
