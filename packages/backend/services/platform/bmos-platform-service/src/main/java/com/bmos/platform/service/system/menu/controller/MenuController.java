package com.bmos.platform.service.system.menu.controller;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.service.system.menu.dto.CurrentMenuTreeQueryDTO;
import com.bmos.platform.service.system.menu.dto.MenuSaveDTO;
import com.bmos.platform.service.system.menu.dto.MenuUpdateDTO;
import com.bmos.platform.service.system.menu.service.MenuService;
import com.bmos.platform.service.system.menu.vo.FunctionVO;
import com.bmos.platform.service.system.menu.vo.MenuListVO;
import com.bmos.platform.service.system.menu.vo.MenuTreeVO;
import com.bmos.platform.service.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.role.dto.RoleRelateMenuSaveItemDTO;
import com.bmos.platform.service.system.role.vo.RoleTreeNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单接口")
@Validated
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/admin/root/list")
    @ApiOperation("查询【权限授权】顶层菜单")
    public ResponseInfo<List<MenuListVO>> getAdminMenuRootList() {
        return ResponseInfo.success(menuService.getAdminMenuRootList());
    }

    @GetMapping("/root/list")
    @ApiOperation("查询【菜单权限】顶层菜单")
    public ResponseInfo<List<MenuListVO>> getMenuRootList() {
        return ResponseInfo.success(menuService.getMenuRootList());
    }

    @GetMapping("/auth/menu/tree")
    @ApiOperation("查询【菜单权限】左侧树")
    @ApiImplicitParam(name = "rootMenuId",value = "根节点ID",required = false)
    public ResponseInfo<List<MenuTreeVO>> getAuthMenuTree(String rootMenuCode,Boolean containsFunc){
        return ResponseInfo.success(menuService.getAuthMenuTree(rootMenuCode,containsFunc));
    }


    @GetMapping("/auth/tree")
    @ApiOperation("查询当前登录人的菜单树")
    public ResponseInfo<List<MenuTreeVO>> getCurrentMenuTree(@Validated CurrentMenuTreeQueryDTO dto) {
        List<MenuTreeVO> currentMenuTree = menuService.getCurrentMenuTree(dto);
        if (Optional.ofNullable(dto.getIsFirst()).orElse(Boolean.FALSE)) {
            return ResponseInfo.success(currentMenuTree.stream()
                    .filter(menu -> menu.getParentId() == 0L && CollUtil.isNotEmpty(menu.getChildren()))
                    .collect(Collectors.toList()));
        }
        return ResponseInfo.success(currentMenuTree);
    }

    @GetMapping("/auth/all")
    @ApiOperation("查询当前登录人的菜单树")
    public ResponseInfo<List<MenuTreeVO>> getCurrentAllMenu(@Validated CurrentMenuTreeQueryDTO dto) {
        List<MenuTreeVO> currentMenuTree = menuService.getCurrentAllMenu(dto);
        return ResponseInfo.success(currentMenuTree);
    }

    @GetMapping("/admin/tree")
    @ApiOperation("菜单树全量查询")
    public ResponseInfo<List<MenuTreeVO>> treeAll(String rootMenuCode) {
        return ResponseInfo.success(menuService.treeAll(rootMenuCode));
    }

    @GetMapping("/admin/tree/operation")
    @ApiOperation("操作日志菜单树")
    public ResponseInfo<List<MenuTreeVO>> operationMenuTreeAll(){
        return ResponseInfo.success(menuService.getOperationMenuAll());
    }

    @GetMapping("/function")
    @ApiOperation("功能查询")
    public ResponseInfo<List<FunctionVO>> getFunction(@NotNull Long menuId, @NotNull Long roleId) {
        return ResponseInfo.success(menuService.getFunction(menuId, roleId));
    }

    @GetMapping("/role/function")
    @ApiOperation("【角色管理-权限授权】获取某个菜单下所有的功能并且标识当前角色id是否有功能的权限授权")
    public ResponseInfo<List<FunctionVO>> getRoleAuthFunction(@NotNull Long menuId, @NotNull Long roleId) {
        return ResponseInfo.success(menuService.getRoleAuthFunction(menuId, roleId));
    }

    @GetMapping("/auth/menu/tree/list")
    @ApiOperation("【角色管理】权限授权显示的菜单树")
    public ResponseInfo<List<MenuTreeVO>> getMenuTree() {
        return ResponseInfo.success(menuService.getMenuTree());
    }

    @PostMapping("/save")
    @ApiOperation("菜单新增")
    public ResponseInfo<Void> save(@RequestBody MenuSaveDTO dto) {
        menuService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("菜单编辑")
    public ResponseInfo<Void> update(@RequestBody MenuUpdateDTO dto) {
        menuService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("菜单删除")
    public ResponseInfo<Void> delete(@NotNull Long id) {
        menuService.delete(id);
        return ResponseInfo.success();
    }

    @GetMapping("/relate-role-data")
    @ApiOperation("菜单与角色关联-查询")
    public ResponseInfo<List<Long>> relateRoleData(@NotNull Long menuId) {
        return ResponseInfo.success(menuService.relateRoleData(menuId));
    }

    @PostMapping("/role/save")
    @ApiOperation("【菜单权限】授权菜单角色")
    @OperationLog
    public ResponseInfo<Void> saveMenuRole(@RequestBody RoleRelateMenuSaveItemDTO dto) {
        menuService.saveMenuRole(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/auth/role/save")
    @ApiOperation("【权限授权】保存菜单角色")
    @OperationLog
    public ResponseInfo<Void> saveAuthRole(@RequestBody RoleRelateMenuSaveItemDTO dto) {
        menuService.saveAuthMenuRole(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/role/tree")
    @ApiOperation("[菜单权限]查询菜单角色")
    public ResponseInfo<List<RoleTreeNodeVO>> getMenuRoleTree(@NotNull Long menuId){
        return ResponseInfo.success(menuService.getMenuRoleTree(menuId));
    }

}
