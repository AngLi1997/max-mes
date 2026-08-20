package com.bmos.platform.service.system.user.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.user.model.UserRelateRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRelateRoleMapper extends BaseMapperX<UserRelateRole> {

    default List<UserRelateRole> getListByUserId(String userId) {
        return selectList(new LambdaQueryWrapperX<UserRelateRole>().eq(UserRelateRole::getUserId, userId));
    }

    default List<UserRelateRole> getListByRoleId(Long roleId) {
        return selectList(new LambdaQueryWrapperX<UserRelateRole>().eq(UserRelateRole::getRoleId, roleId));
    }

    default boolean existUser(Long roleId) {
        return exists(new LambdaQueryWrapperX<UserRelateRole>().eq(UserRelateRole::getRoleId,roleId));
    }

    default List<UserRelateRole> selectByUserId(String userId){
        return selectList(new LambdaQueryWrapperX<UserRelateRole>().eq(UserRelateRole::getUserId,userId));
    }

    /**
     * 查询角色id集合与用户的绑定关系
     * @param roleIds
     * @return
     */
    default List<UserRelateRole> selectByRoleIdList(List<Long> roleIds){
        return selectList(new LambdaQueryWrapperX<UserRelateRole>().in(UserRelateRole::getRoleId,roleIds));
    }

    /**
     * 根据用户id集合查询角色id集合
     * @param userIdList
     * @return
     */
    default List<UserRelateRole> getListByUserIds(List<String> userIdList){
        return selectList(new LambdaQueryWrapperX<UserRelateRole>().in(UserRelateRole::getUserId,userIdList));
    }

    default void deleteByUserIdAndRoleIdList(String userId, List<Long> allRoleIds){
        delete(new LambdaQueryWrapperX<UserRelateRole>().eq(UserRelateRole::getUserId,userId).in(UserRelateRole::getRoleId, allRoleIds));
    }
}
