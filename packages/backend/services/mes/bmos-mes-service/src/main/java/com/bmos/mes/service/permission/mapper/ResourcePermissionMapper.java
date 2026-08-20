package com.bmos.mes.service.permission.mapper;

import com.bmos.mes.service.permission.model.ResourcePermission;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourcePermissionMapper extends BaseMapperX<ResourcePermission> {

    default void deleteByResourceId(Long resourceId) {
        delete(new LambdaQueryWrapperX<ResourcePermission>().eq(ResourcePermission::getResourceId, resourceId));
    }

    List<Long> selectDeptIdsByResourceId(@Param("resourceId") Long resourceId);

}
