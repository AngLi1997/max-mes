package com.bmos.platform.service.system.dept.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.dept.model.DeptRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptRoleMapper extends BaseMapperX<DeptRole> {

    default void deleteByDeptId(Long deptId){
        delete(new LambdaQueryWrapperX<DeptRole>().eq(DeptRole::getDeptId, deptId));
    }

    default List<DeptRole> selectByDeptId(Long deptId){
        return selectList(new LambdaQueryWrapperX<DeptRole>().eq(DeptRole::getDeptId, deptId));
    }

    default List<DeptRole> selectByRoleId(Long roleId){
        return selectList(new LambdaQueryWrapperX<DeptRole>().eq(DeptRole::getRoleId, roleId));
    }

    default void deleteByRoleId(Long roleId){
        delete(new LambdaQueryWrapperX<DeptRole>().eq(DeptRole::getRoleId, roleId));
    }
}
