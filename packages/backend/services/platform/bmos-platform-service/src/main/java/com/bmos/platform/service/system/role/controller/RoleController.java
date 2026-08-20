package com.bmos.platform.service.system.role.controller;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import com.bmos.platform.service.system.role.dto.*;
import com.bmos.platform.service.system.role.service.AuthRoleMenuService;
import com.bmos.platform.service.system.role.service.RoleService;
import com.bmos.platform.service.system.role.vo.*;
import com.bmos.platform.service.system.user.dto.UserRelateRoleSaveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "角色接口")
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;

    // ------------------------- 角色类型树 ---------------------------------
    @GetMapping("/tree-all")
    @ApiOperation("【角色管理】角色类型树全量查询")
    public ResponseInfo<List<RoleTypeTreeVO>> treeAll(String name) {
        return ResponseInfo.success(roleService.treeAll(name));
    }

    @PostMapping("/save-type")
    @ApiOperation("【角色管理】新增角色类型")
    @OperationLog
    public ResponseInfo<Void> saveRoleType(@Validated @RequestBody RoleTypeSaveDTO dto) {
        roleService.saveRoleType(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update-type")
    @ApiOperation("【角色管理】编辑角色类型")
    @OperationLog
    public ResponseInfo<Void> updateRoleType(@Validated @RequestBody RoleTypeUpdateDTO dto) {
        roleService.updateRoleType(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete-type")
    @ApiOperation("【角色管理】删除角色类型")
    @OperationLog
    public ResponseInfo<Void> deleteRoleType(@NotNull Long id) {
        roleService.deleteRoleType(id);
        return ResponseInfo.success();
    }

    // ------------------------- 角色管理 ---------------------------------

    @PostMapping("/save-role")
    @ApiOperation("【角色管理】新增角色")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> saveRole(@Validated @RequestBody RoleSaveDTO dto) {
        if (dto.getRoleTypeId() == 0L) {
            throw new BmosException(PlatformResponseCode.ROLE_TYPE_NOT_ALL);
        }
        roleService.saveRole(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/update-role")
    @ApiOperation("【角色管理】编辑角色")
    @OperationLog
    public ResponseInfo<Void> updateRole(@Validated @RequestBody RoleUpdateDTO dto) {
        if (dto.getRoleTypeId() == 0L) {
            throw new BmosException(PlatformResponseCode.ROLE_TYPE_NOT_ALL);
        }
        roleService.updateRole(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete-role")
    @ApiOperation("【角色管理】删除角色")
    @OperationLog
    public ResponseInfo<Void> deleteRole(@NotNull Long id) {
        roleService.deleteRole(id);
        return ResponseInfo.success();
    }

    @GetMapping("/get-role")
    @ApiOperation("【角色管理】分页查询角色")
    public ResponseInfo<CommonPage<RoleVO>> getRole(@Validated RoleQueryDTO dto) {
        return ResponseInfo.success(roleService.getRole(dto));
    }

    @PostMapping("/relate-user-save")
    @ApiOperation("【角色管理】角色与人员绑定")
    @OperationLog
    public ResponseInfo<Void> relateUserSave(@Validated @RequestBody UserRelateRoleSaveDTO dto) {
        roleService.relateUserSave(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/menu/save")
    @ApiOperation("【角色管理】角色与菜单绑定")
    public ResponseInfo<Void> saveRoleMenu(@Validated @RequestBody RoleMenuSaveDTO dto) {
        roleService.saveRoleMenu(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/auth/menu/save")
    @ApiOperation("【角色管理-权限授权功能】角色与权限授权菜单绑定")
    public ResponseInfo<Void> saveAuthRoleMenu(@Validated @RequestBody RoleMenuSaveDTO dto) {
        roleService.saveAuthRoleMenu(dto);
        return ResponseInfo.success();
    }



    @GetMapping("/list")
    @ApiOperation("查询角色集合")
    public ResponseInfo<List<RoleVO>> getList(RoleListQueryDTO dto) {
        return ResponseInfo.success(roleService.getRoleList(dto));
    }

    //--------------------------------------- 权限授权 -----------------------------------------
    @GetMapping("/auth/role/tree")
    @ApiOperation("【权限授权】查询菜单下有哪些角色")
    public ResponseInfo<List<RoleTreeNodeVO>> getAuthRoleTree(@NotNull Long menuId) {
        return ResponseInfo.success(roleService.getAuthRoleTree(menuId));
    }

    @GetMapping("/auth/list")
    @ApiOperation("【权限授权】查询菜单下有哪些角色id")
    public ResponseInfo<List<Long>> getAuthRoleList(@NotNull Long menuId) {
        return ResponseInfo.success(roleService.getAuthRoleList(menuId));
    }

    @GetMapping("/menu/role/tree")
    @ApiOperation("【菜单权限】查询菜单下有哪些角色")
    public ResponseInfo<List<RoleTypeTreeVO>> getMenuRoleTree(@NotNull Long menuId) {
        return ResponseInfo.success(roleService.getRoleTree(menuId));
    }

    @GetMapping("/aggregate/tree")
    @ApiOperation("查询角色类型与角色聚合树")
    public ResponseInfo<List<RoleTreeNodeVO>> getAggregateTree() {
        return ResponseInfo.success(roleService.getAggregateTree());
    }

    @GetMapping("/role-tree-all")
    @ApiOperation("角色类型树形全量查询含角色")
    public ResponseInfo<List<RoleTypeTreeVO>> getRoleTree(Long menuId) {
        return ResponseInfo.success(roleService.getRoleTree(menuId));
    }

    @GetMapping("/validate-role")
    @ApiOperation("校验角色名称是否存在")
    public ResponseInfo<Boolean> validateRole(@NotBlank String roleName, Long id) {
        return ResponseInfo.success(roleService.validateRole(roleName, id));
    }

    @GetMapping("/validate-roleType")
    @ApiOperation("校验角色类型名称是否存在")
    public ResponseInfo<Boolean> validateRoleType(@Validated RoleTypeValidateDTO dto) {
        return ResponseInfo.success(roleService.validateRoleType(dto));
    }

    @GetMapping("/menu/id")
    @ApiOperation("【角色管理-菜单分配】查询角色下拥有的菜单和功能")
    public ResponseInfo<List<RoleMenuIdVO>> getMenuIds(@NotNull Long roleId) {
        return ResponseInfo.success(roleService.getMenuIds(roleId));
    }

    @GetMapping("/auth/menu/id")
    @ApiOperation("【角色管理-权限分配】查询角色下拥有的权限授权的菜单和功能")
    public ResponseInfo<List<RoleAuthMenuVO>> getAuthMenuIds(@RequestParam("roleId") @NotNull Long roleId) {
        return ResponseInfo.success(roleService.getAuthMenuIds(roleId));
    }

    @GetMapping("/relate-user-data")
    @ApiOperation("角色与人员关联-查询")
    @OperationLog
    public ResponseInfo<List<DeptUserTreeVO>> relateUserData(@NotNull Long roleId) {
        return ResponseInfo.success(roleService.relateUserData(roleId));
    }

    @GetMapping("/id")
    @ApiOperation("通过当前登录用户查询所关联的角色集合")
    public ResponseInfo<List<Long>> getRoleList() {
        return ResponseInfo.success(roleService.getRoleIdList());
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("查询单个角色")
    public ResponseInfo<RoleVO> getDetail(@PathVariable("id") Long id) {
        return ResponseInfo.success(roleService.getDetail(id));
    }

    @GetMapping("/dept/role/tree")
    @ApiOperation("查询部门下所绑定的角色树")
    public ResponseInfo<List<RoleTreeNodeVO>> getDeptRoleTree(@NotNull Long deptId) {
        return ResponseInfo.success(roleService.getDeptRoleTree(deptId));
    }
}
