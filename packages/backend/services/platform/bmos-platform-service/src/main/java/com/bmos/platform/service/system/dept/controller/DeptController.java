package com.bmos.platform.service.system.dept.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.dept.vo.*;
import com.bmos.platform.service.system.dept.dto.*;
import com.bmos.platform.service.system.dept.service.DeptService;
import com.bmos.platform.service.system.dept.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/dept")
@Api(tags = "部门接口")
@Validated
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/tree-all")
    @ApiOperation("部门树全量查询")
    public ResponseInfo<List<DeptTreeVO>> treeAll() {
        return ResponseInfo.success(deptService.treeAll());
    }


    @GetMapping("/user/tree")
    @ApiOperation("部门用户树")
    @ApiImplicitParam(name = "parentDeptCode", value = "父级部门编码")
    public ResponseInfo<List<DeptUserTreeVO>> deptUserTree(String parentDeptCode) {
        return ResponseInfo.success(deptService.getDeptUserTree(parentDeptCode));
    }

    @PostMapping("/save")
    @ApiOperation("部门新增")
    @OperationLog
    public ResponseInfo<Void> save(@Validated @RequestBody DeptSaveDTO dto) {
        deptService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("部门编辑")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody DeptUpdateDTO dto) {
        deptService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("部门删除")
    @OperationLog
    public ResponseInfo<Void> delete(@NotNull Long id) {
        deptService.delete(id);
        return ResponseInfo.success();
    }

    @GetMapping("/validate-dept")
    @ApiOperation("校验部门名称是否存在")
    public ResponseInfo<Boolean> validateDept(@NotBlank String deptName, Long id) {
        return ResponseInfo.success(deptService.validateDept(deptName, id));
    }

    @PostMapping("/relate-user-save")
    @ApiOperation("部门与用户关联-保存")
    @OperationLog
    public ResponseInfo<Void> relateUserSave(@Validated @RequestBody List<DeptRelateUserSaveDTO> list) {
        deptService.relateUserSave(list);
        return ResponseInfo.success();
    }

    @GetMapping("/relate-user-data")
    @ApiOperation("部门与用户关联-查询")
    public ResponseInfo<CommonPage<DeptAssignUserVO>> relateUserData(DeptRelateUserQueryDTO dto) {
        return ResponseInfo.success(deptService.relateUserData(dto));
    }

    @DeleteMapping("/remove/user")
    @ApiOperation("部门与用户关联移除")
    @OperationLog
    public ResponseInfo<Void> relateUserDel(@RequestBody @Validated DeptUserRemoveDTO dto) {
        deptService.removeUser(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/relate-user-delAll")
    @ApiOperation("部门与用户关联-全部移除")
    @OperationLog
    public ResponseInfo<Void> relateUserDelAll(@NotNull Long deptId) {
        deptService.relateUserDelAll(deptId);
        return ResponseInfo.success();
    }

    @GetMapping("/assign-person")
    @ApiOperation("查询所有可分配的人员")
    public ResponseInfo<List<DeptUnAssignUserVO>> assignPerson(DeptAssignQueryDTO dto) {
        return ResponseInfo.success(deptService.assignPerson(dto));
    }

    @GetMapping("/tree-unassigned")
    @ApiOperation("查询部门树里未分配的人员")
    public ResponseInfo<DeptTreeUserAllVO> unassigned(String name) {
        DeptTreeUserAllVO unassigned = deptService.unassigned(name);
        return ResponseInfo.success(unassigned);
    }

    @GetMapping("/tree-assigned")
    @ApiOperation("查询部门树里已分配的人员")
    public ResponseInfo<List<DeptTreeUserVO>> assigned(String name, @NotNull Long deptId) {
        return ResponseInfo.success(deptService.assigned(name, deptId));
    }

    @GetMapping("/id")
    @ApiOperation("通过当前登录用户查询所关联的部门及子部门集合")
    public ResponseInfo<List<Long>> getDeptList() {
        return ResponseInfo.success(deptService.getDeptList());
    }

    @GetMapping("/mine/id")
    @ApiOperation("通过当前登录用户查询所关联的部门及子部门集合")
    public ResponseInfo<List<Long>> getMineDeptList() {
        return ResponseInfo.success(deptService.getMineDeptIds());
    }

    @GetMapping("/tree")
    @ApiOperation("查询全量部门树")
    public ResponseInfo<List<CommonTreeVO>> getDeptTree() {
        return ResponseInfo.success(deptService.getDeptTree());
    }

    @GetMapping("/partition/tree")
    @ApiOperation("查询当前登录人及上级部门的树")
    public ResponseInfo<List<CommonTreeVO>> getPartitionTree() {
        return ResponseInfo.success(deptService.getPartitionTree());
    }

    @GetMapping("/interval/tree")
    @ApiOperation("部门内部管理-查询左侧部门树")
    public ResponseInfo<List<DeptIntervalTreeVO>> intervalTree() {
        return ResponseInfo.success(deptService.intervalTree());
    }

    @GetMapping("/dept/role")
    @ApiOperation("角色管理-获取当前部门所拥有的角色")
    public ResponseInfo<List<Long>> deptRole(@RequestParam("id") @ApiParam("部门id") @NotNull Long id) {
        return ResponseInfo.success(deptService.deptRole(id));
    }

    @PostMapping("/bind/role")
    @ApiOperation("部门管理/部门内部管理-部门分配角色")
    @OperationLog
    public ResponseInfo<Void> bindRole(@RequestBody DeptRoleBindDTO dto) {
        deptService.bindRole(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/role/bind/dept")
    @ApiOperation("部门管理/部门内部管理-部门分配角色")
    @OperationLog
    public ResponseInfo<Void> roleBindDept(@RequestBody RoleDeptBindDTO dto) {
        deptService.roleBindDept(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/role/dept/list")
    @ApiOperation("角色管理-获取当前角色绑定的所有部门id列表")
    public ResponseInfo<List<Long>> getRoleDeptList(@RequestParam("roleId") Long roleId) {
        return ResponseInfo.success(deptService.getRoleDeptList(roleId));
    }


}
