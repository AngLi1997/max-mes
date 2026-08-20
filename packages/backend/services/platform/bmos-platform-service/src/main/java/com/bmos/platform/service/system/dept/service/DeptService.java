package com.bmos.platform.service.system.dept.service;

import com.bmos.common.tree.CommonTreeVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.dept.vo.*;
import com.bmos.platform.service.system.dept.dto.*;
import com.bmos.platform.service.system.dept.model.Dept;
import com.bmos.platform.service.system.dept.model.DeptRole;
import com.bmos.platform.service.system.dept.vo.*;
import com.bmos.platform.service.system.user.model.User;

import java.util.List;

public interface DeptService {

    List<DeptTreeVO> treeAll();

    void save(DeptSaveDTO dto);

    void update(DeptUpdateDTO dto);

    void delete(Long id);

    Boolean validateDept(String deptName, Long id);

    void relateUserSave(List<DeptRelateUserSaveDTO> list);

    CommonPage<DeptAssignUserVO> relateUserData(DeptRelateUserQueryDTO dto);

    void relateUserDel(Long id);

    void relateUserDelAll(Long deptId);

    List<DeptUnAssignUserVO> assignPerson(DeptAssignQueryDTO dto);

    DeptTreeUserAllVO unassigned(String name);

    List<DeptTreeUserVO> assigned(String name, Long deptId);

    List<Long> getDeptList();

    List<DeptUserTreeVO> getDeptUserTree(String parentDeptCode);

    List<DeptUserTreeVO> getDeptUserTreeByUsers(List<User> users);

    void removeUser(DeptUserRemoveDTO dto);

    List<CommonTreeVO> getDeptTree();

    List<CommonTreeVO> getPartitionTree();

    List<Long> getMineDeptIds();

    List<Dept> getByIds(List<Long> deptIdList);

    /**
     * 部门内部管理-查询左侧部门树
     * <p> 只查询当前登陆人所属的部门以及其上属的部门和其所有下级部门 </p>
     * @return
     */
    List<DeptIntervalTreeVO> intervalTree();

    /**
     * 根据部门id查询角色与部门的绑定关系
     * @param deptId
     * @return
     */
    List<DeptRole> selectByDeptId(Long deptId);

    /**
     * 根据用户id查询所属部门
     * @param userId
     * @return
     */
    List<Long> getDeptByUserId(String userId);

    /**
     * 获取当前部门所拥有的角色id
     * @return
     */
    List<Long> deptRole(Long id);

    /**
     * 部门分配角色
     * @param dto
     */
    void bindRole(DeptRoleBindDTO dto);

    /**
     * 获取角色所拥有的所有部门id
     * @param roleId
     * @return
     */
    List<Long> getRoleDeptList(Long roleId);

    /**
     * 角色绑定部门
     * @param dto
     */
    void roleBindDept(RoleDeptBindDTO dto);
}
