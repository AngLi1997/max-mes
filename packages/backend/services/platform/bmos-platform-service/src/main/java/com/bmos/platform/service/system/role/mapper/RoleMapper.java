package com.bmos.platform.service.system.role.mapper;

import com.bmos.common.util.AdminUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.system.role.vo.FeignRoleVO;
import com.bmos.platform.service.system.role.dto.RoleListQueryDTO;
import com.bmos.platform.service.system.role.dto.RoleQueryDTO;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.vo.RoleTypeTreeItemVO;
import com.bmos.platform.service.system.role.vo.RoleTypeTreeVO;
import com.bmos.platform.service.system.role.vo.RoleVO;
import com.bmos.platform.service.system.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapperX<Role> {

    List<RoleTypeTreeVO> selectAllRoleType(@Param("name") String name);

    List<RoleVO> selectRole(RoleQueryDTO dto);

    default boolean validateRole(String roleName, Long id) {
        return exists(new LambdaQueryWrapperX<Role>().eq(Role::getRoleName,roleName)
                .neIfPresent(Role::getId,id)
                .last("limit 1"));
    }

    List<Long> selectMenuByRoleId(@Param("roleId") Long roleId,@Param("isMenu") Boolean isMenu);

    List<User> selectUserByRoleId(@Param("roleId") Long roleId);

    List<Long> selectRoleByMenuId(@Param("menuId") Long menuId);

    List<RoleTypeTreeVO> getRoleTreeAll(@Param("menuId") Long menuId);

    List<RoleTypeTreeItemVO> getRoleAll();

    default List<Role> selectAdminRole(){
        return selectList(new LambdaQueryWrapperX<Role>().ne(Role::getId, AdminUtil.ADMIN_ROLE));
    }

    List<Role> selectRoleByUser(@Param("userId") String userId);

    List<RoleTypeTreeVO> selectTreeNodeByMenuId(@Param("menuId") Long menuId);

    List<RoleTypeTreeVO> selectTypeTreeList();


    default List<Role> selectCustomList(RoleListQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<Role>().inIfPresent(Role::getId,dto.getIds()));
    }

    default boolean existsByType(Long roleTypeId){
        return exists(new LambdaQueryWrapperX<Role>().eq(Role::getRoleTypeId,roleTypeId));
    }

    /**
     * 查询所有角色
     * @return
     */
    default List<Role> selectAll(){
        return selectList(new LambdaQueryWrapperX<>());
    }

    List<FeignRoleVO> getListByIds(@Param("ids") List<Long> ids);
}
