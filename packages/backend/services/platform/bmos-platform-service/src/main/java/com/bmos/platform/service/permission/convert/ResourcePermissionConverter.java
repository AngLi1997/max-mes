package com.bmos.platform.service.permission.convert;

import com.bmos.platform.service.permission.model.ResourcePermission;
import com.bmos.platform.service.permission.service.dto.ResourcePermissionSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ResourcePermissionConverter {
    ResourcePermissionConverter INSTANCE = Mappers.getMapper(ResourcePermissionConverter.class);

    default List<ResourcePermission> convertList(ResourcePermissionSaveDTO dto) {
        return dto.getDeptIds()
                .stream()
                .map(deptId -> {
                    ResourcePermission resourcePermission = new ResourcePermission();
                    resourcePermission.setResourceId(dto.getResourceId());
                    resourcePermission.setDeptId(deptId);
                    return resourcePermission;
                }).collect(Collectors.toList());
    }
}
