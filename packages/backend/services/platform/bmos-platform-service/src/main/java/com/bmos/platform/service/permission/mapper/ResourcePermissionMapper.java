package com.bmos.platform.service.permission.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.permission.model.ResourcePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ResourcePermissionMapper extends BaseMapperX<ResourcePermission> {

    default void deleteByResourceId(Long resourceId) {
        delete(new LambdaQueryWrapperX<ResourcePermission>().eq(ResourcePermission::getResourceId, resourceId));
    }

    default List<Long> selectDeptIdsByResourceId(@Param("resourceId") Long resourceId){
        return selectList(new LambdaQueryWrapperX<ResourcePermission>().eq(ResourcePermission::getResourceId, resourceId))
                .stream().map(ResourcePermission::getDeptId).collect(Collectors.toList());
    }

    default List<ResourcePermission> selectListByResourceIdList(List<Long> resourceIdList){
        return selectList(new LambdaQueryWrapperX<ResourcePermission>().
                in(ResourcePermission::getResourceId, resourceIdList));
    }
}
