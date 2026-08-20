package com.bmos.wms.service.platform.permission.convert;

import com.bmos.wms.service.platform.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.wms.service.platform.permission.model.ResourcePermission;
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
