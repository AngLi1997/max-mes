package com.bmos.platform.service.system.user.service;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.user.dto.UserQueryDTO;
import com.bmos.platform.facade.system.user.dto.UserResourceQueryDTO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.dept.dto.DeptRelateUserSaveDTO;
import com.bmos.platform.service.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.user.dto.*;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseInfo<UserLoginVO> login(UserLoginDTO dto);

    ResponseInfo<UserLoginVO> loginNoValidate(String loginName);

    List<MenuVO> permission(String userId, Long menuId);

    CommonPage<UserPageVO> getPage(UserPageQueryDTO dto);

    /**
     * 重置密码 用户管理页面重置密码，由前端生成新密码
     * @param dto
     */
    void resetPassword(UserResetPwdDTO dto);

    void save(UserSaveDTO dto);

    void update(UserUpdateDTO dto);

    void changeStatus(UserStartDTO dto);

    Boolean validateUser(String userName);

    void exportData(HttpServletResponse resp);

    void importData(MultipartFile file);

    void relateRoleSave(List<UserRelateRoleSaveItemDTO> list);

    void relateDeptSave(List<DeptRelateUserSaveDTO> list);

    List<Long> relateRoleData(String userId);

    List<Long> relateDeptData(String userId);

    void logout(HttpServletRequest request);

    UserInfoVO getCurrentUserStatus(HttpServletRequest request);

    List<User> getByUserIds(Collection<String> userIds);

    List<UserListItemVO> getList(UserListQueryDTO dto);

    /**
     * 修改当前登陆人密码
     * @param dto
     * @return
     */
    ResponseInfo<Void> changeLoginUserPassword(ChangeLoginPwdDTO dto);

    ResponseInfo<Void> validatePassword(ValidatePasswordDTO dto);

    BaseUserDO getByUserId(String userId);

    List<UserListItemVO> listByRole(Long roleId);

    UserInfoVO validatePwd(ValidatePwdDTO dto);

    List<User> getByLoginNames(List<String> loginNames);

    List<UserListItemVO> listByDeptList(List<Long> deptIds);

    List<UserListItemVO> listByMenuId(Long menuId);

    /**
     * 用户管理界面列表对某一个用户进行密码修改
     * @param dto
     * @return
     */
    ResponseInfo<Void> changePassword(ChangePwdDTO dto);

    /**
     * 账号激活操作（即当前登录人为未激活状态，需要进行一次密码修改后才能进行激活）
     * @param dto
     * @return
     */
    ResponseInfo<Void> activeUser(ActiveUserDTO dto);

    /**
     * 账户过期时，自动弹出的修改密码
     * @param dto
     * @return
     */
    ResponseInfo<Void> expireUserChangePwd(ExpireUserChangePwdDTO dto);

    /**
     * 移动端修改密码
     * @param dto
     */
    void changeMobilePwd(MobileChangePwdDTO dto);

    /**
     * 根据菜单id和部门id查询用户集合
     * 取交集
     * @param dto
     * @return
     */
    List<FeignUserVO> listByMenuIdAndDeptIds(UserQueryDTO dto);

    /**
     * 根据userId集合查询用户信息
     * @param userIds
     * @return
     */
    Map<String, FeignUserVO> getFeignUserByUserIds(Collection<String> userIds);

    /**
     *
     * @param dto
     * @return
     */
    List<FeignUserVO> listByMenuIdAndResourceId(UserResourceQueryDTO dto);

    /**
     * 获取用户详情
     *
     * @param id
     * @return
     */
    UserDetailInfoVO getUserDetailInfo(String id);

    /**
     * 校验密码长度规则
     * @param password 密码
     * @param businessParameterCode 业务规则code
     */
    void validPwdRuleLen(String password, String businessParameterCode);

    /**
     * 校验密码字符规则
     * @param password 密码
     * @param businessParameterCode 业务规则code
     */
    void validPwdRuleCharacter(String password, String businessParameterCode);

    /**
     * @param dto
     */
    void deptUserBindRole(DeptUserBindRoleDTO dto);

    void downloadTemplate(HttpServletResponse response);

    void importUser(HttpServletResponse response, MultipartFile file);

    void exportUser(HttpServletResponse response, UserPageQueryDTO dto);
    /**
     * 解锁账户
     * @param userId
     */
    void unLockUser(String userId);

    /**
     * 密码有效期自动变更
     */
    void pwdExpireValid();

    /**
     * 账号锁定时间到了之后自动解锁
     * 当前定时器每分钟执行一次
     * 若配置的时间为0，则可能会有1min的延迟
     */
    void userAutoUnLockExpireValid();

    List<FeignUserVO> listUserListByDeptIds(List<Long> deptIds);

    List<FeignUserVO> listUserListByRoleIds(List<Long> roles);

    List<FeignUserVO> listByName(String userName);
}
