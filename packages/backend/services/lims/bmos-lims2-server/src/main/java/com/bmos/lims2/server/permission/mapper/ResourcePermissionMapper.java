package com.bmos.lims2.server.permission.mapper;

import com.bmos.lims2.server.permission.model.ResourcePermission;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourcePermissionMapper extends BaseMapperX<ResourcePermission> {

    default void deleteByResourceId(Long resourceId) {
        delete(new LambdaQueryWrapperX<ResourcePermission>().eq(ResourcePermission::getResourceId, resourceId));
    }

    default List<ResourcePermission> selectDeptIdsByResourceId(Long resourceId) {
        return selectList(new LambdaQueryWrapperX<ResourcePermission>().eq(ResourcePermission::getResourceId, resourceId));
    }

    default List<ResourcePermission> selectByDeptIdsAndModule(java.util.List<Long> deptIds, String module) {
        return selectList(new LambdaQueryWrapperX<ResourcePermission>()
                .in(ResourcePermission::getDeptId, deptIds)
                .eq(ResourcePermission::getModule, module));
    }

}
