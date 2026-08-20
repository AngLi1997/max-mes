package com.bmos.platform.service.system.user.controller;

import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.system.dept.dto.DeptRelateUserSaveDTO;
import com.bmos.platform.service.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.user.dto.*;
import com.bmos.platform.service.system.user.service.ActiveService;
import com.bmos.platform.service.system.user.service.UserService;
import com.bmos.platform.service.system.user.vo.*;
import com.bmos.platform.service.util.UploadFileUtils;
import com.bmos.web.interceptor.Activate;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.SocketException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActiveService activeService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResponseInfo<UserLoginVO> login(@Validated @RequestBody UserLoginDTO dto) {
        return userService.login(dto);
    }

    @PostMapping("/active")
    @ApiOperation("激活")
    public ResponseInfo<String> active(@RequestBody String activeStr) {
        return ResponseInfo.success(activeService.save(activeStr));
    }

    @PostMapping("/actived")
    @ApiOperation("是否激活")
    public ResponseInfo<RsaVO> exists() {
        return ResponseInfo.success(activeService.actived());
    }

    @GetMapping("/mac")
    @ApiOperation("电脑mac地址")
    public ResponseInfo<Set<String>> mac() throws SocketException {
        return ResponseInfo.success(Activate.getAllMACAddress());
    }

    @PostMapping("/loginNoValidate")
    @ApiOperation("用户获取token")
    public ResponseInfo<UserLoginVO> loginNoValidate(@NotEmpty String loginName) {
        return userService.loginNoValidate(loginName);
    }

    @DeleteMapping("/logout")
    @ApiOperation("登出")
    public ResponseInfo<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseInfo.success();
    }

    @GetMapping("/status")
    @ApiOperation("查询当前用户信息")
    public ResponseInfo<UserInfoVO> getUserStatus(HttpServletRequest request) {
        return ResponseInfo.success(userService.getCurrentUserStatus(request));
    }

    @GetMapping("/permission")
    @ApiOperation("登录获取菜单权限")
    public ResponseInfo<List<MenuVO>> permission(@NotNull String userId, Long menuId) {
        return ResponseInfo.success(userService.permission(userId, menuId));
    }

    @GetMapping("/page")
    @ApiOperation("用户分页查询")
    public ResponseInfo<CommonPage<UserPageVO>> getPage(UserPageQueryDTO dto) {
        return ResponseInfo.success(userService.getPage(dto));
    }

    @PostMapping("/save")
    @ApiOperation("用户新增")
    @OperationLog
    public ResponseInfo<Void> save(@Validated @RequestBody UserSaveDTO dto) {
        userService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/resetPwd")
    @ApiOperation("用户管理页面对用户进行重置密码操作")
    @OperationLog
    public ResponseInfo<Void> resetPwd(@RequestBody UserResetPwdDTO dto){
        userService.resetPassword(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/changeLoginUserPassword")
    @ApiOperation("修改当前登录人密码")
    @OperationLog
    public ResponseInfo<Void> changeLoginUserPassword(@RequestBody @Validated ChangeLoginPwdDTO dto) {
        return userService.changeLoginUserPassword(dto);
    }

    @PutMapping("/changePwd")
    @ApiOperation("用户管理界面列表对某一个用户进行密码修改")
    @OperationLog
    public ResponseInfo<Void> changePassword(@RequestBody @Validated ChangePwdDTO dto) {
        return userService.changePassword(dto);
    }

    @PutMapping("/activeUser")
    @ApiOperation("账号激活")
    @OperationLog
    public ResponseInfo<Void> activeUser(@RequestBody @Validated ActiveUserDTO dto) {
        return userService.activeUser(dto);
    }

    @PutMapping("/expireUserChangePwd")
    @ApiOperation("账户过期时，自动弹出的修改密码")
    @OperationLog
    public ResponseInfo<Void> expireUserChangePwd(@RequestBody @Validated ExpireUserChangePwdDTO dto) {
        return userService.expireUserChangePwd(dto);
    }

    @PostMapping("/validatePassword")
    @ApiOperation("校验当前人的密码")
    public ResponseInfo<Void> validatePassword(@Validated @RequestBody ValidatePasswordDTO dto) {
        return userService.validatePassword(dto);
    }

    @PostMapping("/validatePwd")
    @ApiOperation("校验密码")
    public ResponseInfo<UserInfoVO> validatePwd(@Validated @RequestBody ValidatePwdDTO dto) {
        return ResponseInfo.success(userService.validatePwd(dto));
    }

    @PutMapping("/update")
    @ApiOperation("用户编辑")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody UserUpdateDTO dto) {
        userService.update(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/start")
    @ApiOperation("用户启用/停用")
    @OperationLog
    public ResponseInfo<Void> changeStatus(@RequestBody UserStartDTO dto) {
        userService.changeStatus(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/validate-user")
    @ApiOperation("校验用户名称是否存在")
    public ResponseInfo<Boolean> validateUser(@NotBlank String userName) {
        return ResponseInfo.success(userService.validateUser(userName));
    }

    @GetMapping("/export")
    @ApiOperation("导出表格")
    public ResponseInfo<Void> exportData(HttpServletResponse resp) {
        userService.exportData(resp);
        return ResponseInfo.success();
    }

    @GetMapping("/import")
    @ApiOperation("导入表格")
    public ResponseInfo<Void> importData(MultipartFile file) {
        userService.importData(file);
        return ResponseInfo.success();
    }

    @GetMapping("/relate-role-data")
    @ApiOperation("用户与角色关联-查找")
    public ResponseInfo<List<Long>> relateRoleData(@NotBlank String userId) {
        return ResponseInfo.success(userService.relateRoleData(userId));
    }

    @GetMapping("/relate-dept-data")
    @ApiOperation("用户与部门关联-查找")
    public ResponseInfo<List<Long>> relateDeptData(@NotBlank String userId) {
        return ResponseInfo.success(userService.relateDeptData(userId));
    }

    @PostMapping("/relate-role-save")
    @ApiOperation("用户与角色关联-保存")
    @OperationLog
    public ResponseInfo<Void> relateRoleSave(@RequestBody List<UserRelateRoleSaveItemDTO> list) {
        userService.relateRoleSave(list);
        return ResponseInfo.success();
    }

    @PostMapping("/relate-dept-save")
    @ApiOperation("用户与部门关联-保存")
    public ResponseInfo<Void> relateDeptSave(@RequestBody List<DeptRelateUserSaveDTO> list) {
        userService.relateDeptSave(list);
        return ResponseInfo.success();
    }

    @GetMapping("/list")
    @ApiOperation("用户列表")
    public ResponseInfo<List<UserListItemVO>> getList(@Validated UserListQueryDTO dto) {
        return ResponseInfo.success(userService.getList(dto));
    }

    @GetMapping("/{userId}")
    @ApiOperation("根据用户id查询用户信息")
    public ResponseInfo<BaseUserDO> getByUserId(@PathVariable("userId") String userId) {
        return ResponseInfo.success(userService.getByUserId(userId));
    }

    @GetMapping("/listByRole")
    @ApiOperation("根据角色id查询用户信息")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true)
    public ResponseInfo<List<UserListItemVO>> listByRole(@NotNull Long roleId) {
        return ResponseInfo.success(userService.listByRole(roleId));
    }

    @GetMapping("/listByDeptIdList")
    @ApiOperation("根据部门id集合查询用户集合")
    @ApiImplicitParam(name = "deptIds", value = "部门id", required = true)
    public ResponseInfo<List<UserListItemVO>> listByDeptList(@NotEmpty @RequestParam("deptIds") List<Long> deptIds){
        return ResponseInfo.success(userService.listByDeptList(deptIds));
    }

    @GetMapping("/listByMenuId")
    @ApiOperation("根据功能权限按钮id查询用户列表")
    public ResponseInfo<List<UserListItemVO>> listByMenuId(@RequestParam("menuId") Long menuId){
        return ResponseInfo.success(userService.listByMenuId(menuId));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("查看当前详情信息")
    public ResponseInfo<UserDetailInfoVO> getUserDetailInfo(@NotNull @ApiParam("用户主键id") @PathVariable("id") String userId) {
        return ResponseInfo.success(userService.getUserDetailInfo(userId));
    }

    @GetMapping("/dept/user/bind/role")
    @ApiOperation("【部门(内部)管理】用户绑定角色")
    @OperationLog
    public ResponseInfo<Void> deptUserBindRole(@Validated DeptUserBindRoleDTO dto) {
        userService.deptUserBindRole(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/info")
    @ApiOperation("解锁账户")
    @OperationLog
    public ResponseInfo<Void> unLockUser(@RequestBody UnLockUserDTO dto) {
        userService.unLockUser(dto.getUserId());
        return ResponseInfo.success();
    }

    @GetMapping("/download/template")
    @ApiOperation("下载用户导入模板")
    public void downloadTemplate(HttpServletResponse response){
        userService.downloadTemplate(response);
    }

    @PostMapping("/import/user")
    @ApiOperation("导入用户信息")
    public void importUser(HttpServletResponse response,MultipartFile file){
        UploadFileUtils.checkExcel(file);
        userService.importUser(response,file);
    }

    @GetMapping("/export/user")
    @ApiOperation("导出用户信息")
    public void exportUser(HttpServletResponse response,UserPageQueryDTO dto){
        userService.exportUser(response,dto);
    }
}
