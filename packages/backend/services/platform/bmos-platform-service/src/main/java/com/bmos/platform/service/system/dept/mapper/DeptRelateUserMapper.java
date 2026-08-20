package com.bmos.platform.service.system.dept.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.dept.dto.DeptUserRemoveDTO;
import com.bmos.platform.service.system.dept.model.Dept;
import com.bmos.platform.service.system.dept.model.DeptRelateUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeptRelateUserMapper extends BaseMapperX<DeptRelateUser> {

    default List<DeptRelateUser> getListByDeptId(Long deptId){
        return selectList(new LambdaQueryWrapperX<DeptRelateUser>().eq(DeptRelateUser::getDeptId, deptId));
    }

    default List<DeptRelateUser> getListByUserId(String userId){
        return selectList(new LambdaQueryWrapperX<DeptRelateUser>().eq(DeptRelateUser::getUserId, userId));
    }

    default List<DeptRelateUser> getByDeptIds(Set<Long> deptIds){
        return selectList(new LambdaQueryWrapperX<DeptRelateUser>().in(DeptRelateUser::getDeptId,deptIds));
    }

    default boolean existsUser(Long deptId){
        return exists(new LambdaQueryWrapperX<DeptRelateUser>().eq(DeptRelateUser::getDeptId,deptId));
    }

    default List<DeptRelateUser> getListByUserIds(Set<String> userIds){
        return selectList(new LambdaQueryWrapperX<DeptRelateUser>().in(DeptRelateUser::getUserId,userIds));
    }

    default void deleteByDeptAndUser(DeptUserRemoveDTO dto){
        delete(new LambdaQueryWrapperX<DeptRelateUser>()
                .eq(DeptRelateUser::getDeptId,dto.getDeptId())
                .eq(DeptRelateUser::getUserId,dto.getUserId()));
    }

    List<Long> selectDeptIdsByUserId(@Param("userId") String userId);

    default List<DeptRelateUser> selectDeptIdsByUserIds(List<String> userIdList){
        return selectList(new LambdaQueryWrapperX<DeptRelateUser>().in(DeptRelateUser::getUserId,userIdList));
    }

    default List<DeptRelateUser> getDeptIdByUserId(String userId){
        return selectList(new LambdaQueryWrapperX<DeptRelateUser>().eq(DeptRelateUser::getUserId,userId));
    }
}
