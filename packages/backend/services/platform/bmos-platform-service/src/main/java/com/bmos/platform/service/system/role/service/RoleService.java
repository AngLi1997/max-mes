package com.bmos.platform.service.system.role.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import com.bmos.platform.facade.system.role.vo.FeignRoleVO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.role.dto.*;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.vo.*;
import com.bmos.platform.service.system.user.dto.UserRelateRoleSaveDTO;

import java.util.List;
import java.util.Set;

public interface RoleService {
    /**
     * 【角色管理】分页查询角色
     * @param dto
     * @return
     */
    CommonPage<RoleVO> getRole(RoleQueryDTO dto);


    List<RoleTypeTreeVO> treeAll(String name);


    /**
     * 查询当前菜单下绑定的角色
     * 若菜单id为null则查询所有角色
     * @param menuId
     * @return
     */
    List<RoleTypeTreeVO> getRoleTree(Long menuId);

    void saveRoleType(RoleTypeSaveDTO dto);

    void saveRole(RoleSaveDTO dto);

    void updateRoleType(RoleTypeUpdateDTO dto);

    void deleteRoleType(Long id);

    Boolean validateRole(String roleName, Long id);

    Boolean validateRoleType(RoleTypeValidateDTO dto);

    void updateRole(RoleUpdateDTO dto);

    void deleteRole(Long id);

    void relateUserSave(UserRelateRoleSaveDTO dto);

    /**
     * 获取角当前登陆人能够授权的菜单id下当前角色拥有的菜单权限
     * @param roleId
     * @return
     */
    List<RoleMenuIdVO> getMenuIds(Long roleId);

    /**
     * 获取当前角色拥有的权限菜单
     * @param roleId
     * @return
     */
    List<RoleAuthMenuVO> getAuthMenuIds(Long roleId);

    List<DeptUserTreeVO> relateUserData(Long roleId);

    List<Long> getRoleIdList();

    void saveRoleMenu(RoleMenuSaveDTO dto);

    /**
     * 保存角色菜单权限
     * @param dto
     */
    void saveAuthRoleMenu(RoleMenuSaveDTO dto);

    List<RoleTreeNodeVO> getAggregateTree();

    List<RoleTreeNodeVO> getAuthRoleTree(Long menuId);

    List<Long> getAuthRoleList(Long menuId);

    List<RoleVO> getRoleList(RoleListQueryDTO dto);

    RoleVO getDetail(Long id);

    /**
     * 菜单权限页面，查询当前菜单id有哪些角色
     * @param menuId
     * @return
     */
    List<RoleTypeTreeVO> getMenuRoleTree(Long menuId);

    /**
     * 查询当前菜单具有的角色权限
     * @param curMenu
     * @return
     */
    List<Long> getRoleListByMenuId(Long curMenu);

    /**
     * 查询当前菜单集合的角色权限
     * @param menuIdList
     * @return
     */
    Set<Long> selectRoleByMenuIdList(List<Long> menuIdList);

    /**
     * 获取具有对应权限的用户列表
     *
     * @param authCode
     * @return
     */
    List<FeignUserVO> authUserList(String authCode);

    /**
     * 根据角色id集合获取角色列表
     * @param roleIdList
     * @return
     */
    List<Role> getByIds(List<Long> roleIdList);

    /**
     * 查询某个部门下绑定的角色树
     * @param deptId
     * @return
     */
    List<RoleTreeNodeVO> getDeptRoleTree(Long deptId);

    List<FeignRoleVO> getListByIds(List<Long> ids);
}
