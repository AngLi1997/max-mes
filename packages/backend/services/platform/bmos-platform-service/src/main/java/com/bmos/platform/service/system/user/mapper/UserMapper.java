package com.bmos.platform.service.system.user.mapper;

import com.bmos.common.base.user.SysUser;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.user.dto.UserListQueryDTO;
import com.bmos.platform.service.system.user.dto.UserPageQueryDTO;
import com.bmos.platform.service.system.user.enums.ActiveEnum;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.vo.UserImportVO;
import com.bmos.platform.service.system.user.vo.UserListItemVO;
import com.bmos.platform.service.system.user.vo.UserPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper extends BaseMapperX<User> {

    List<UserPageVO> selectUserPage(UserPageQueryDTO dto);

    List<UserImportVO> selectUserImport();

    List<Long> validateUser(@Param("userName") String userName);

    List<Long> relateRoleData(@Param("userId") String userId);

    List<Long> relateDeptData(@Param("userId") String userId);

    List<MenuVO> getMenuByUserId(@Param("userId") String userId,
                                 @Param("menuId") Long menuId,
                                 @Param("parentId") Long parentId);

    /**
     * 查询所有被激活的永固
     * @return
     */
    default List<User> selectActiveUserList(){
        return selectList(new LambdaQueryWrapperX<User>()
                .eq(User::getActiveStatus, ActiveEnum.ACTIVATE.getCode()));
    }

    default Long getIdByLoginName(String loginName) {
        return selectOne(new LambdaQueryWrapperX<User>().eq(User::getLoginName, loginName)).getId();
    }

    default Boolean existsByUserIdAndActiveStatus(String userId, ActiveEnum activate) {
        return exists(new LambdaQueryWrapperX<User>().eq(User::getUserId, userId).eq(User::getActiveStatus, activate.getCode()));
    }

    default User selectByUserId(String userId) {
        return selectOne(new LambdaQueryWrapperX<User>().eq(User::getUserId, userId));
    }

    default List<User> selectByUserIds(Collection<String> userIds) {
        return selectList(new LambdaQueryWrapperX<User>().in(User::getUserId, userIds));
    }

    default List<User> selectCustomList(UserListQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<User>().eq(User::getState, dto.getState()));
    }

    List<UserListItemVO> selectByRole(@Param("roleId") Long roleId);

    default Boolean existsByLoginNameAndPwd(String loginName, String pwd) {
        return exists(new LambdaQueryWrapperX<User>().eq(User::getLoginName, loginName)
                .eq(User::getPassword, pwd)
                .last("limit 1"));
    }

    default User selectByLoginNameAndPwd(String loginName, String pwd) {
        return selectOne(new LambdaQueryWrapperX<User>().eq(User::getLoginName,loginName)
                .eq(User::getPassword,pwd));
    }

    default List<User> selectByLoginNames(List<String> loginNames) {
        return selectList(new LambdaQueryWrapperX<User>()
                .in(User::getLoginName, loginNames));
    }

    default User selectByLoginName(String loginName){
        return selectOne(new LambdaQueryWrapperX<User>()
                .eq(User::getLoginName,loginName));
    }

    List<UserListItemVO> selectByDeptIds(@Param("deptIds") List<Long> deptIds);

    List<UserListItemVO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    default List<User> selectNeedUnLockUser(){
        return selectList(new LambdaQueryWrapperX<User>().eq(User::getActiveStatus, ActiveEnum.PASSWORD_LOCK.getCode())
                .le(User::getUnlockTime, LocalDateTime.now()));
    }
}
