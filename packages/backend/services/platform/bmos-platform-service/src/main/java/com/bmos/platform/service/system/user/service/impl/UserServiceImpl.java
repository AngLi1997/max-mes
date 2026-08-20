package com.bmos.platform.service.system.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.constant.SecurityConstant;
import com.bmos.common.exception.AuthorizationException;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelReaderUtils;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.OptionBo;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.response.ResponseItem;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.common.util.jwt.JwtUtils;
import com.bmos.common.util.web.ServletUtils;
import com.bmos.common.util.web.TokenUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.logging.annotation.defined.OperationUserDefined;
import com.bmos.logging.aspect.defined.OperationUserDefinedContext;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.common.enums.TerminalTypeEnums;
import com.bmos.platform.common.enums.UserActiveEnums;
import com.bmos.platform.common.enums.system.user.UserConstants;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.common.utils.RsaUtils;
import com.bmos.platform.common.utils.TimeUtils;
import com.bmos.platform.facade.system.dept.vo.DeptTreeVO;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.user.dto.UserQueryDTO;
import com.bmos.platform.facade.system.user.dto.UserResourceQueryDTO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.execute.parameter.model.PlatformPwdRule;
import com.bmos.platform.service.execute.parameter.repository.BusinessParameterRepository;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.factory.controller.vo.StationTreeNodeVO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.EquipmentStationUser;
import com.bmos.platform.service.factory.service.EquipmentStationUserService;
import com.bmos.platform.service.factory.service.FactoryModuleService;
import com.bmos.platform.service.factory.service.FactoryStationService;
import com.bmos.platform.service.log.model.LoginLogModel;
import com.bmos.platform.service.log.service.LoginLogService;
import com.bmos.platform.service.permission.service.ResourcePermissionService;
import com.bmos.platform.service.system.dept.convert.DeptRelateUserConvert;
import com.bmos.platform.service.system.dept.dto.DeptRelateUserSaveDTO;
import com.bmos.platform.service.system.dept.mapper.DeptRelateUserMapper;
import com.bmos.platform.service.system.dept.model.DeptRelateUser;
import com.bmos.platform.service.system.dept.service.DeptService;
import com.bmos.platform.service.system.menu.constant.MenuConstant;
import com.bmos.platform.service.system.menu.util.MenuI8nUtil;
import com.bmos.platform.service.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.role.mapper.RoleMapper;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import com.bmos.platform.service.system.role.service.RoleMenuRelationService;
import com.bmos.platform.service.system.role.service.RoleService;
import com.bmos.platform.service.system.role.vo.RoleTypeTreeVO;
import com.bmos.platform.service.system.user.constant.UserConstant;
import com.bmos.platform.service.system.user.convert.UserConvert;
import com.bmos.platform.service.system.user.convert.UserRelateRoleConvert;
import com.bmos.platform.service.system.user.dto.*;
import com.bmos.platform.service.system.user.entity.PasswordHistory;
import com.bmos.platform.service.system.user.enums.ActiveEnum;
import com.bmos.platform.service.system.user.enums.GenderEnum;
import com.bmos.platform.service.system.user.enums.ActiveEnum;
import com.bmos.platform.service.system.user.enums.LoginActionEnum;
import com.bmos.platform.service.system.user.enums.UserStatusEnum;
import com.bmos.platform.service.system.user.listener.ImportUserListener;
import com.bmos.platform.service.system.user.mapper.PasswordHistoryMapper;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.mapper.UserRelateRoleMapper;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.model.UserRelateRole;
import com.bmos.platform.service.system.user.redis.PermissionRedisDao;
import com.bmos.platform.service.system.user.service.UserService;
import com.bmos.platform.service.system.user.vo.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Pattern PATTERN = Pattern.compile(UserConstant.REGEX);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRelateRoleMapper userRelateRoleMapper;

    @Autowired
    private DeptRelateUserMapper deptRelateUserMapper;

    @Autowired
    PasswordHistoryMapper passwordHistoryMapper;

    @Autowired
    private PermissionRedisDao permissionRedisDao;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    BusinessParameterRepository businessParameterRepository;

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    @Value("${bmos.secret-key}")
    private String secretKey;

    private DES des;
    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Autowired
    EquipmentStationUserService equipmentStationUserService;

    @Autowired
    FactoryModuleService factoryModuleService;

    @Autowired
    FactoryStationService factoryStationService;

    @PostConstruct
    public void init() {
        des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKey.getBytes());
    }

    @Override
    public ResponseInfo<UserLoginVO> login(UserLoginDTO dto) {
        User sysUser = userMapper.selectByLoginName(dto.getLoginName());
        if (ObjectUtil.isNull(sysUser)) {
            loginLogService.insert(getLoginLogModel(dto, false, LoginActionEnum.LOG_IN, PlatformResponseCode.LOGIN_ERROR, null));
            throw new BmosException(PlatformResponseCode.LOGIN_ERROR);
        }
        if (!sysUser.getLoginName().equals(dto.getLoginName())) {
            loginLogService.insert(getLoginLogModel(sysUser, false, LoginActionEnum.LOG_IN, PlatformResponseCode.LOGIN_ERROR, null));
            throw new BmosException(PlatformResponseCode.LOGIN_ERROR);
        }
        // 登录校验
        Long expireDays = validateLogin(sysUser, dto);
        User userInfo = permissionRedisDao.getUserInfo(sysUser.getUserId());
        // 存在缓存意味着已在其他地方登录
        String anotherTerminalToken = permissionRedisDao.hasLogin(sysUser.getUserId(), String.valueOf(dto.getTerminalType()));
        if (StrUtil.isNotEmpty(anotherTerminalToken)) {
            //如果已在其他地方登录，退出账号
            validateLoginBehavior(dto, userInfo, anotherTerminalToken);
        }
        sysUser.setTerminalType(dto.getTerminalType());
        sysUser.setServiceType(dto.getServiceType());
        sysUser.setLoginTime(System.currentTimeMillis());

        String token = IdUtil.fastUUID();
        //jwt token
        String authToken = createLoginToken(sysUser.getUserId(), token);

        sysUser.setActivated(!UserActiveEnums.TO_BE_ACTIVATE.getValue().equals(sysUser.getActiveStatus()));
        //存放缓存
        sysUser.setToken(token);
        permissionRedisDao.cacheUserInfo(sysUser);
        permissionRedisDao.setUserLogin(sysUser.getUserId(), dto.getTerminalType(), sysUser.getToken());
        permissionRedisDao.cacheTokenIp(sysUser.getToken(), ServletUtils.getClientIP());
        sysUser.setPwdErrorCount(0);
        userMapper.updateById(sysUser);
        // 登录成功日志记录
        if (UserActiveEnums.PASSWORD_EXPIRED.getValue().equals(sysUser.getActiveStatus())) {
            loginLogService.insert(getLoginLogModel(sysUser, false, LoginActionEnum.LOG_IN, PlatformResponseCode.PASSWORD_VALID_EXPIRED, null));
        } else if (UserActiveEnums.TO_BE_ACTIVATE.getValue().equals(sysUser.getActiveStatus())) {
            loginLogService.insert(getLoginLogModel(sysUser, false, LoginActionEnum.LOG_IN, PlatformResponseCode.USER_TO_ACTIVE, null));
        } else {
            loginLogService.insert(getLoginLogModel(sysUser, true, LoginActionEnum.LOG_IN, PlatformResponseCode.LOGIN_SUCCESS, null));
        }
        sysUser.setToken(authToken);
        UserLoginVO userLoginVO = UserConvert.INSTANCE.convertVO(sysUser);
        userLoginVO.setExpireDays(expireDays);
        BusinessParameterDetailVO detailVO = businessParameterRepository.detailByCode(BusinessParameterCodeConstants.PLATFORM_USER_PWD_EXPIRED_REMIND_PERIOD);
        if (Objects.isNull(detailVO) || StrUtil.isEmpty(detailVO.getValue())) {
            userLoginVO.setRemindExpire(false);
        } else {
            userLoginVO.setRemindExpire(expireDays <= Integer.parseInt(detailVO.getValue()));
        }
        return ResponseInfo.success(userLoginVO);
    }

    @Override
    public ResponseInfo<UserLoginVO> loginNoValidate(String loginName) {
        User sysUser = userMapper.selectByLoginName(loginName);
        if (ObjectUtil.isNull(sysUser)) {
            throw new BmosException(PlatformResponseCode.LOGIN_ERROR);
        }
        if (!sysUser.getLoginName().equals(loginName)) {
            throw new BmosException(PlatformResponseCode.LOGIN_ERROR);
        }
        String token = IdUtil.fastUUID();
        //jwt token
        String authToken = createLoginToken(sysUser.getUserId(), token);

        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //存放缓存
        sysUser.setToken(token);
        permissionRedisDao.cacheUserInfo(sysUser);
        permissionRedisDao.setUserLogin(sysUser.getUserId(), TerminalTypeEnums.PC.getValue(), sysUser.getToken());
        permissionRedisDao.cacheTokenIp(sysUser.getToken(), ServletUtils.getClientIP());
        // 登录成功日志记录
        loginLogService.insert(getLoginLogModel(sysUser, true, LoginActionEnum.LOG_IN, PlatformResponseCode.LOGIN_SUCCESS, null));
        sysUser.setToken(authToken);
        return ResponseInfo.success(UserConvert.INSTANCE.convertVO(sysUser));
    }

    private String createLoginToken(String userId, String token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstant.USER_ID, userId);
        claims.put(SecurityConstant.LOGIN_TOKEN, token);
        return JwtUtils.createToken(claims);
    }

    private LoginLogModel getLoginLogModel(UserLoginDTO dto, boolean success, LoginActionEnum operationAction, ResponseItem descriptionCode, String ip) {
        LoginLogModel loginLogModel = new LoginLogModel();
        loginLogModel.setLoginName(dto.getLoginName());
        loginLogModel.setUserName(null);
        loginLogModel.setOperationState(success);
        loginLogModel.setOperationAction(operationAction.getCode());
        loginLogModel.setDescriptionCode(descriptionCode.getCode());
        if (Objects.nonNull(descriptionCode.getArgs())) {
            // 将descriptionCode.getArgs()数组以逗号做分割
            loginLogModel.setDescriptionParam(StrUtil.join(",", descriptionCode.getArgs()));
        }
        loginLogModel.setUserId(null);
        loginLogModel.setIp(ip);
        if (ObjectUtil.isEmpty(ip)) {
            loginLogModel.setIp(ServletUtils.getClientIP());
        }
        return loginLogModel;
    }


    private LoginLogModel getLoginLogModel(User user, boolean success, LoginActionEnum operationAction, ResponseItem descriptionCode, String ip) {
        LoginLogModel loginLogModel = new LoginLogModel();
        loginLogModel.setLoginName(user.getLoginName());
        loginLogModel.setUserName(user.getUserName());
        loginLogModel.setOperationState(success);
        loginLogModel.setOperationAction(operationAction.getCode());
        loginLogModel.setDescriptionCode(descriptionCode.getCode());
        if (Objects.nonNull(descriptionCode.getArgs())) {
            // 将descriptionCode.getArgs()数组以逗号做分割
            loginLogModel.setDescriptionParam(StrUtil.join(",", descriptionCode.getArgs()));
        }
        loginLogModel.setUserId(user.getUserId());
        loginLogModel.setIp(ip);
        if (ObjectUtil.isEmpty(ip)) {
            loginLogModel.setIp(ServletUtils.getClientIP());
        }
        return loginLogModel;
    }

    private void validateUserState(User user) {
        if (UserStatusEnum.OFF.getValue().equals(user.getState())) {
            ResponseItem res = PlatformResponseCode.USER_STATE_FALSE;
            BmosException bmosException = new BmosException(res);
            loginLogService.insert(getLoginLogModel(user, false, LoginActionEnum.LOG_IN, bmosException.getResponseItem(), null));
            throw bmosException;
        }
    }


    @Override
    public List<MenuVO> permission(String userId, Long menuId) {
        return userMapper.getMenuByUserId(userId, menuId, MenuConstant.PARENT_ID);
    }

    @Override
    public CommonPage<UserPageVO> getPage(UserPageQueryDTO dto) {
        List<UserPageVO> list = userMapper.selectUserPage(dto);
        CommonPage<UserPageVO> userPageVOCommonPage = CommonPage.convertPage(list);
        List<UserPageVO> userPageVOList = userPageVOCommonPage.getList();
        if (CollUtil.isEmpty(userPageVOList)) {
            return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
        }
        return userPageVOCommonPage;
    }

    private List<Long> getChildDept(Long deptId, List<DeptTreeVO> deptTree) {

        DeptTreeVO deptTreeVO = helpFindTargetDeptTree(deptId, deptTree);
        if (ObjectUtil.isNull(deptTreeVO)) {
            return new ArrayList<>();
        }
        return helpFindAllChildDept(deptTreeVO);
    }

    private List<Long> helpFindAllChildDept(DeptTreeVO deptTreeVO) {
        List<Long> childDeptIdList = new ArrayList<>();
        if (CollUtil.isEmpty(deptTreeVO.getChildren())) {
            return childDeptIdList;
        }
        for (DeptTreeVO child : deptTreeVO.getChildren()) {
            childDeptIdList.add(child.getId());
            childDeptIdList.addAll(helpFindAllChildDept(child));
        }
        return childDeptIdList;
    }

    private DeptTreeVO helpFindTargetDeptTree(Long deptId, List<DeptTreeVO> deptTree) {
        for (DeptTreeVO deptTreeVO : deptTree) {
            if (deptId.equals(deptTreeVO.getId())) {
                return deptTreeVO;
            }
            if (CollUtil.isNotEmpty(deptTreeVO.getChildren())) {
                DeptTreeVO target = helpFindTargetDeptTree(deptId, deptTreeVO.getChildren());
                if (ObjectUtil.isNotNull(target)) {
                    return target;
                }
            }
        }
        return null;
    }

    private boolean judgeChildBindUser(List<DeptRelateUser> curUserDeptRelate, List<Long> childDeptIds, Long deptId) {
        if (CollUtil.isEmpty(childDeptIds)) {
            return false;
        }
        Set<Long> childDeptIdSet = new HashSet<>(childDeptIds);
        for (DeptRelateUser deptRelateUser : curUserDeptRelate) {
            if (childDeptIdSet.contains(deptRelateUser.getDeptId()) && !deptId.equals(deptRelateUser.getDeptId())) {
                return true;
            }
        }
        return false;
    }

    private String getRoleChain(Role role, List<RoleTypeTreeVO> roleTree) {
        List<String> res = new ArrayList<>();
        helpGetRoleChain(role.getRoleTypeId(), roleTree, res);
        res.add(role.getRoleName());
        return StrUtil.join(StrUtil.SLASH, res);
    }

    private boolean helpGetRoleChain(Long id, List<RoleTypeTreeVO> roleTree, List<String> res) {
        for (RoleTypeTreeVO roleTypeTreeVO : roleTree) {
            res.add(roleTypeTreeVO.getRoleTypeName());
            if (id.equals(roleTypeTreeVO.getId())) {
                return true;
            }
            boolean flg = false;
            if (CollUtil.isNotEmpty(roleTypeTreeVO.getChildren())) {
                flg = helpGetRoleChain(id, roleTypeTreeVO.getChildren(), res);
            }
            if (!flg) {
                res.remove(res.size() - 1);
            } else {
                return true;
            }
        }
        return false;
    }

    private String getDeptChain(Long deptId, List<DeptTreeVO> deptTree) {
        // 使用深度优先算法进行寻找
        List<String> res = new ArrayList<>();
        helpGetDeptChain(deptId, deptTree, res);
        if (CollUtil.isEmpty(res)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.join(StrUtil.SLASH, res);
    }

    private boolean helpGetDeptChain(Long deptId, List<DeptTreeVO> deptTree, List<String> res) {
        // 使用深度优先算法进行寻找
        for (DeptTreeVO deptTreeVO : deptTree) {
            res.add(deptTreeVO.getName());
            if (deptId.equals(deptTreeVO.getId())) {
                return true;
            }
            boolean flg = false;
            if (CollUtil.isNotEmpty(deptTreeVO.getChildren())) {
                flg = helpGetDeptChain(deptId, deptTreeVO.getChildren(), res);
            }
            if (!flg) {
                res.remove(res.size() - 1);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(UserResetPwdDTO dto) {
        //todo admin校验？
        User user = null;
        if (ObjectUtil.isNotNull(dto.getUserId())) {
            user = userMapper.selectById(dto.getUserId());
        }
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        //重置密码时重置密码有效期
        user.setValidTime(getPwdValidTime());
        if (UserActiveEnums.PASSWORD_LOCK.getValue().equals(user.getActiveStatus())) {
            this.doUnLockUser(user);
        }
        user.setActiveStatus(UserActiveEnums.TO_BE_ACTIVATE.getValue());
        String password = RsaUtils.decryptPwd(dto.getPassword());
        user.setPassword(des.encryptHex(password));
        // 修改我们系统的数据
        userMapper.updateById(user);
        // 历史密码个数+1
        passwordHistoryMapper.insert(PasswordHistory.builder().userId(user.getUserId()).pwd(user.getPassword()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "saveUser")
    public void save(UserSaveDTO dto) {
        User sysUser = userMapper.selectByLoginName(dto.getLoginName());
        if (Objects.nonNull(sysUser)) {
            throw new BmosException(PlatformResponseCode.USER_EXISTS);
        }
        //赋值为默认密码t
        User user = UserConvert.INSTANCE.convertUser(dto);
        user.setPassword(UserConstant.USER_PASSWORD);
        user.setUserId(String.valueOf(IdUtil.getSnowflakeNextId()));
        // 新增我们系统的数据
        user.setPassword(des.encryptHex(user.getPassword()));
        user.setValidTime(getPwdValidTime());
        userMapper.insert(user);
        permissionRedisDao.setUserInfo(UserConvert.INSTANCE.convertUserVO2(user));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateDTO dto) {
        if (AdminUtil.isAdminUser(String.valueOf(dto.getId()))) {
            throw new BmosException(PlatformResponseCode.ADMIN_USER_CAN_NOT_BE_MODIFY);
        }
        User user = UserConvert.INSTANCE.convertUser(dto);
        if (dto.getStatus() == 1) {
            user.setPwdErrorCount(0);
            user.setActiveStatus(UserActiveEnums.ACTIVATE.getValue());
        }
        userMapper.updateById(user);
        permissionRedisDao.removeUserInfo(user.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(UserStartDTO dto) {
        if (UserStatusEnum.OFF.getValue().equals(dto.getState())) {
            //更改用户-部门关联的数据
            List<DeptRelateUser> deptRelateList = deptRelateUserMapper.getListByUserId(dto.getUserId());
            if (CollUtil.isNotEmpty(deptRelateList)) {
                deptRelateUserMapper.deleteBatchIds(deptRelateList);
            }
            //更改用户-部门关联的数据
            List<UserRelateRole> roleRelateList = userRelateRoleMapper.getListByUserId(dto.getUserId());
            if (CollUtil.isNotEmpty(roleRelateList)) {
                userRelateRoleMapper.deleteBatchIds(roleRelateList);
            }
        }
        //更改用户表的数据
        userMapper.updateById(UserConvert.INSTANCE.convertUser(dto));
    }

    @Override
    public Boolean validateUser(String userName) {
        return CollUtil.isNotEmpty(userMapper.validateUser(userName));
    }

    @Override
    public void exportData(HttpServletResponse resp) {
        try {
            EasyExcel.write(resp.getOutputStream(), User.class)
                    .sheet("导出数据")
                    .doWrite(() -> userMapper.selectUserImport());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), UserImportVO.class, new ImportUserListener()).doReadAllSync();
        } catch (IOException e) {
            log.error("导入用户失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relateRoleSave(List<UserRelateRoleSaveItemDTO> dtoList) {
        //先删除过去的数据
        List<UserRelateRole> deleteList = userRelateRoleMapper.getListByUserId(CollUtil.getFirst(dtoList).getUserId());
        if (CollUtil.isNotEmpty(deleteList)) {
            userRelateRoleMapper.deleteBatchIds(deleteList);
        }
        //再新增要添加的数据
        if (CollUtil.getFirst(dtoList).getRoleId() != null) {
            userRelateRoleMapper.insertBatch(UserRelateRoleConvert.INSTANCE.convert(dtoList));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void relateDeptSave(List<DeptRelateUserSaveDTO> dtoList) {
        //先删除过去的数据
        List<DeptRelateUser> deleteList = deptRelateUserMapper.getListByUserId(CollUtil.getFirst(dtoList).getUserId());
        if (CollUtil.isNotEmpty(deleteList)) {
            deptRelateUserMapper.deleteBatchIds(deleteList);
        }
        //再新增要添加的数据
        if (CollUtil.getFirst(dtoList).getDeptId() != null) {
            deptRelateUserMapper.insertBatch(DeptRelateUserConvert.INSTANCE.convert(dtoList));
        }
    }

    @Override
    public List<Long> relateRoleData(String userId) {
        return userMapper.relateRoleData(userId);
    }

    @Override
    public List<Long> relateDeptData(String userId) {
        return userMapper.relateDeptData(userId);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String terminalType = request.getHeader("terminalType");
        if (StrUtil.isEmpty(terminalType)) {
            throw new BmosException(PlatformResponseCode.LOGOUT_PARAM_ERROR);
        }
        String userId = SysUserHolder.getUser().getUserId();
        permissionRedisDao.removeToken(TokenUtils.getToken(request));
        permissionRedisDao.removeTokenIp(TokenUtils.getToken(request));
        permissionRedisDao.removeLoginInfo(userId, terminalType);
        User user = permissionRedisDao.getUserInfo(userId);
        LoginLogModel loginLogModel = getLoginLogModel(user, true, LoginActionEnum.lOG_OUT, PlatformResponseCode.LOGOUT_SUCCESS, null);
        loginLogService.insert(loginLogModel);
    }

    @Override
    public UserInfoVO getCurrentUserStatus(HttpServletRequest request) {
        String userId = SysUserHolder.getUser().getUserId();
        if (StrUtil.isEmpty(userId)) {
            throw new AuthorizationException();
        }

        User userInfo = permissionRedisDao.getUserInfo(userId);
        if (ObjectUtil.isNull(userInfo)) {
            throw new AuthorizationException();
        }

        if (StrUtil.isEmpty(userInfo.getToken())) {
            String terminalType = request.getHeader("terminalType");
            if (StrUtil.isNotEmpty(terminalType))
                userInfo.setToken(permissionRedisDao.hasLogin(userId, terminalType));
        }
        return UserConvert.INSTANCE.convertUserVO(userInfo);
    }

    @Override
    public List<User> getByUserIds(Collection<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectByUserIds(userIds);
    }

    @Override
    public List<UserListItemVO> getList(UserListQueryDTO dto) {
        return UserConvert.INSTANCE.convertVoList(userMapper.selectCustomList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseInfo<Void> changeLoginUserPassword(ChangeLoginPwdDTO dto) {
        SysUser loginUser = SysUserHolder.getUser();
        if (ObjectUtil.isNull(loginUser)) {
            throw new AuthorizationException();
        }
        User user = userMapper.selectByUserId(loginUser.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        if (Objects.equals(ActiveEnum.PASSWORD_LOCK.getCode(), user.getActiveStatus())) {
            throw new BmosException(PlatformResponseCode.USER_PASSWORD_LOCK);
        }
        // 旧密码校验
        String oldPassword = RsaUtils.decryptPwd(dto.getOldPassword());
        if (!StrUtil.equals(des.decryptStr(user.getPassword()), oldPassword)) {
            throw new BmosException(PlatformResponseCode.USER_OLD_PASSWORD_ERROR);
        }
        String newPassword = RsaUtils.decryptPwd(dto.getNewPassword());
        changePwd(user, newPassword);
        return ResponseInfo.success();
    }

    @Override
    public ResponseInfo<Void> changePassword(ChangePwdDTO dto) {
        User user = userMapper.selectById(dto.getId());
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        // 旧密码校验
        String oldPassword = RsaUtils.decryptPwd(dto.getOldPassword());
        if (!StrUtil.equals(des.decryptStr(user.getPassword()), oldPassword)) {
            throw new BmosException(PlatformResponseCode.USER_OLD_PASSWORD_ERROR);
        }
        String newPassword = RsaUtils.decryptPwd(dto.getNewPassword());
        changePwd(user, newPassword);
        return ResponseInfo.success();
    }

    @Override
    public ResponseInfo<Void> activeUser(ActiveUserDTO dto) {
        User user = userMapper.selectByLoginName(dto.getLoginName());
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        String newPassword = RsaUtils.decryptPwd(dto.getNewPassword());
        changePwd(user, newPassword);
        return ResponseInfo.success();
    }

    @Override
    public ResponseInfo<Void> expireUserChangePwd(ExpireUserChangePwdDTO dto) {
        User user = userMapper.selectByLoginName(dto.getLoginName());
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        changePwd(user, RsaUtils.decryptPwd(dto.getNewPassword()));
        return ResponseInfo.success();
    }

    @Override
    public void changeMobilePwd(MobileChangePwdDTO dto) {
        SysUser sysUser = SysUserHolder.getUser();
        if (ObjectUtil.isNull(sysUser)) {
            throw new AuthorizationException();
        }
        User user = userMapper.selectByUserId(sysUser.getUserId());
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        this.changePwd(user, RsaUtils.decryptPwd(dto.getPassword()));
    }

    @Override
    public List<FeignUserVO> listByMenuIdAndDeptIds(UserQueryDTO dto) {
        List<UserListItemVO> listByDept = listByDeptList(dto.getDeptIds());
        List<FeignUserVO> deptUser = UserConvert.INSTANCE.convertToPlatformUserVOList(listByDept);
        if (Objects.isNull(dto.getMenuId())) {
            return deptUser;
        }
        List<UserListItemVO> listByMenu = listByMenuId(dto.getMenuId());
        List<FeignUserVO> menuUser = UserConvert.INSTANCE.convertToPlatformUserVOList(listByMenu);
        return new ArrayList<>(CollUtil.intersectionDistinct(deptUser, menuUser));
    }

    @Override
    public Map<String, FeignUserVO> getFeignUserByUserIds(Collection<String> userIds) {
        List<User> byUserIds = this.getByUserIds(userIds);
        if (CollUtil.isEmpty(byUserIds)) {
            return Collections.emptyMap();
        }
        Map<String, FeignUserVO> map = new HashMap<>(byUserIds.size());
        for (User byUserId : byUserIds) {
            map.put(byUserId.getUserId(), UserConvert.INSTANCE.convertToPlatformUserVO(byUserId));
        }
        return map;
    }

    @Override
    public List<FeignUserVO> listByMenuIdAndResourceId(UserResourceQueryDTO dto) {
        List<Long> deptIdList = resourcePermissionService.getDeptListByResourceId(dto.getResourceId());
        if (CollUtil.isEmpty(deptIdList)) {
            return new ArrayList<>();
        }
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setMenuId(dto.getMenuId());
        userQueryDTO.setDeptIds(deptIdList);
        return this.listByMenuIdAndDeptIds(userQueryDTO);
    }

    @Override
    public UserDetailInfoVO getUserDetailInfo(String userId) {
        User user = userMapper.selectByUserId(userId);
        if (Objects.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        // 获取用户所属部门
        UserDetailInfoVO userDetailInfoVO = UserConvert.INSTANCE.convert2DetailVO(user);
        userDetailInfoVO.setDeptNameList(this.getDeptNameByUserId(user.getUserId()));
        userDetailInfoVO.setRoleNameList(this.getRoleNameByUserId(user.getUserId()));
        userDetailInfoVO.setStationNameList(this.getStationNameByUserId(user.getUserId()));
        if (ActiveEnum.PASSWORD_LOCK.getCode().equals(user.getActiveStatus())) {
            if (Objects.isNull(user.getUnlockTime())) {
                // 获取HttpServletRequest
                HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                userDetailInfoVO.setUnLockTime(I18nUtils.getResponseMessage(PlatformResponseCode.USER_LOCK.getCode(), PlatformResponseCode.USER_LOCK.getMessage(), null, request));
            } else {
                userDetailInfoVO.setUnLockTime(LocalDateTimeUtil.format(user.getUnlockTime(), GlobalConstants.DATE_TIME_FORMAT));
            }
        }
        return userDetailInfoVO;
    }

    private List<String> getStationNameByUserId(String userId) {
        // 获取当前用户绑定的工位
        List<EquipmentStationUser> userBindStations = equipmentStationUserService.getStationByUserId(userId);
        if (CollUtil.isEmpty(userBindStations)) {
            return new ArrayList<>();
        }
        List<Long> stationIdList = userBindStations.stream().map(EquipmentStationUser::getStationId).collect(Collectors.toList());
        List<EquipmentStation> stationInfos = factoryStationService.selectByIds(stationIdList);
        if (CollUtil.isEmpty(stationInfos)) {
            return new ArrayList<>();
        }
        Map<Long, String> stationNameMap = new HashMap<>();
        List<StationTreeNodeVO> stationTreeNodeVOS = factoryStationService.stationTree();
        for (EquipmentStation stationInfo : stationInfos) {
            if (stationNameMap.containsKey(stationInfo.getId())) {
                continue;
            }
            stationNameMap.put(stationInfo.getId(), getStationChain(stationInfo, stationTreeNodeVOS));
        }
        return stationNameMap.values().stream().filter(StrUtil::isNotEmpty).collect(Collectors.toList());
    }

    private String getStationChain(EquipmentStation stationInfo, List<StationTreeNodeVO> stationTreeNodeVOS) {
        List<String> res = new ArrayList<>();
        helpGetStationChain(stationInfo.getModuleId(), stationTreeNodeVOS, res);
        res.add(stationInfo.getName());
        return StrUtil.join(StrUtil.SLASH, res);
    }

    private boolean helpGetStationChain(Long id, List<StationTreeNodeVO> stationTreeNodeVOS, List<String> res) {
        for (StationTreeNodeVO stationTreeNodeVO : stationTreeNodeVOS) {
            res.add(stationTreeNodeVO.getName());
            if (id.equals(stationTreeNodeVO.getId())) {
                res.remove(res.size() - 1);
                res.add(StrUtil.format("{}{}{}", stationTreeNodeVO.getCode(), StrUtil.DASHED, stationTreeNodeVO.getName()));
                return true;
            }
            boolean flg = false;
            if (CollUtil.isNotEmpty(stationTreeNodeVO.getChildren())) {
                flg = helpGetStationChain(id, stationTreeNodeVO.getChildren(), res);
            }
            if (!flg) {
                res.remove(res.size() - 1);
            } else {
                return true;
            }
        }
        return false;
    }

    private List<String> getRoleNameByUserId(String userId) {
        List<UserRelateRole> userRelateRoles = userRelateRoleMapper.selectByUserId(userId);
        if (CollUtil.isEmpty(userRelateRoles)) {
            return new ArrayList<>();
        }
        List<Long> roleIdList = userRelateRoles.stream().map(UserRelateRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectBatchIds(roleIdList);
        Map<Long, String> roleMap = new HashMap<>();
        // 查询所有角色树
        List<RoleTypeTreeVO> roleTree = roleService.treeAll(null);
        for (Role role : roles) {
            if (roleMap.containsKey(role.getId())) {
                continue;
            }
            roleMap.put(role.getId(), getRoleChain(role, roleTree));
        }
        return roleMap.values().stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    private List<String> getDeptNameByUserId(String userId) {
        List<DeptTreeVO> deptTree = deptService.treeAll();
        List<Long> deptIds = deptRelateUserMapper.selectDeptIdsByUserId(userId);
        Map<Long, String> deptMap = new HashMap<>();
        Map<Long, List<Long>> deptChildDeptMap = new HashMap<>();
        for (Long deptId : deptIds) {
            if (deptChildDeptMap.containsKey(deptId)) {
                continue;
            }
            List<Long> childDeptList = getChildDept(deptId, deptTree);
            deptChildDeptMap.put(deptId, childDeptList);
        }
        for (Long deptId : deptIds) {
            if (deptMap.containsKey(deptId)) {
                continue;
            }
            // 获取当前部门id所在树的分支
            deptMap.put(deptId, getDeptChain(deptId, deptTree));
        }
        return deptMap.values().stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }


    @Override
    public ResponseInfo<Void> validatePassword(ValidatePasswordDTO dto) {
        String userId = SysUserHolder.getUser().getUserId();
        User user = userMapper.selectByUserId(userId);
        if (ObjectUtil.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        String password = RsaUtils.decryptPwd(user.getPassword());
        if (!StrUtil.equals(password, des.encryptHex(RsaUtils.decryptPwd(dto.getPassword())))) {
            if (!AdminUtil.isAdminUser(user.getUserId())) {
                userMapper.updateById(userPasswordErrorCountValidate(user));
            }
            return ResponseInfo.failure(PlatformResponseCode.USER_PASSWORD_ERROR);
        }
        return ResponseInfo.success();
    }

    @Override
    public BaseUserDO getByUserId(String userId) {
        BaseUserDO baseUseInfo = permissionRedisDao.getBaseUseInfo(userId);
        if (Objects.nonNull(baseUseInfo)) {
            return baseUseInfo;
        }
        baseUseInfo = UserConvert.INSTANCE.convertUserVO2(userMapper.selectByUserId(userId));
        if (Objects.isNull(baseUseInfo)) {
            return null;
        }
        permissionRedisDao.setUserInfo(baseUseInfo);
        return baseUseInfo;
    }

    @Override
    public List<UserListItemVO> listByRole(Long roleId) {
        return userMapper.selectByRole(roleId);
    }

    @Override
    public UserInfoVO validatePwd(ValidatePwdDTO dto) {
        return UserConvert.INSTANCE.convertUserVO(
                userMapper.selectByLoginNameAndPwd(dto.getLoginName(), des.encryptHex(RsaUtils.decryptPwd(dto.getPassword())))
        );
    }

    @Override
    public List<User> getByLoginNames(List<String> loginNames) {
        return userMapper.selectByLoginNames(loginNames);
    }

    @Override
    public List<UserListItemVO> listByDeptList(List<Long> deptIds) {
        return userMapper.selectByDeptIds(deptIds);
    }

    @Override
    public List<UserListItemVO> listByMenuId(Long menuId) {
        if (ObjectUtil.isNull(menuId)) {
            List<DeptRelateUser> listByUserId = deptRelateUserMapper.getListByUserId(SysUserHolder.getUser().getUserId());
            List<Long> deptIds = CollectionUtils.convertList(listByUserId, DeptRelateUser::getDeptId);
            if (CollUtil.isEmpty(deptIds)) {
                return Collections.emptyList();
            }
            return userMapper.selectByDeptIds(deptIds);
        }
        List<RoleRelateMenu> roleRelate = roleMenuRelationService.getByMenuId(menuId);
        if (CollUtil.isEmpty(roleRelate)) {
            return Collections.emptyList();
        }
        List<Long> roleIds = CollectionUtils.convertList(roleRelate, RoleRelateMenu::getRoleId);
        return userMapper.selectByRoleIds(roleIds);

    }


    private void validateLoginBehavior(UserLoginDTO dto, User user, String token) {
        String tokenIp = permissionRedisDao.getTokenIp(token);
        // 被挤登出
        loginLogService.insert(getLoginLogModel(user, true, LoginActionEnum.lOG_OUT, PlatformResponseCode.SECOND_LOGIN_AUTOMATIC_LOGOUT, tokenIp));
        //刪除 token 缓存
        permissionRedisDao.removeToken(token);
        permissionRedisDao.removeTokenIp(token);
    }

    private void validateUserActiveStatus(User user, Boolean flg) {
        if (!UserActiveEnums.PASSWORD_LOCK.getValue().equals(user.getActiveStatus())) {
            return;
        }
        String unUnLockTime;
        ResponseItem res;
        if (flg) {
            res = PlatformResponseCode.PASSWORD_VALID_LOCKED_LAST_ERROR_PWD;
        } else {
            res = PlatformResponseCode.PASSWORD_VALID_LOCKED;
        }
        if (Objects.isNull(user.getUnlockTime())) {
            unUnLockTime = String.valueOf(PlatformResponseCode.USER_LOCK.getCode());
        } else {
            unUnLockTime = LocalDateTimeUtil.format(user.getUnlockTime(), GlobalConstants.DATE_TIME_FORMAT);
        }
        BmosException exception = new BmosException(res, unUnLockTime);
        // 记录登录失败日志
        loginLogService.insert(getLoginLogModel(user, false, LoginActionEnum.LOG_IN, exception.getResponseItem(), null));
        throw exception;
    }

    /**
     * 密码规则校验
     *
     * @param user
     * @param password ：对前端传递过来的密码进行解密（此处为明文密码）
     */
    private void validatePassword(User user, String password) {
        if (StrUtil.isEmpty(password)) {
            throw new BmosException(PlatformResponseCode.PASSWORD_EMPTY);
        }
        // 校验密码中是否含有配置中的特定字符
        this.validPwdRuleCharacter(password, BusinessParameterCodeConstants.PLATFORM_USER_PWD_RULE_CHARACTER);
        // 校验密码长度
        this.validPwdRuleLen(password, BusinessParameterCodeConstants.PLATFORM_USER_PWD_RULE_MIN_LEN);
        // 校验密码历史是否相同
        validPwdRuleHistory(user, password);
    }

    /**
     * 校验密码长度
     *
     * @param password
     */
    @Override
    public void validPwdRuleLen(String password, String businessParameterCode) {
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterRepository.detailByCode(businessParameterCode);
        if (Objects.isNull(businessParameterDetailVO) || StrUtil.isEmpty(businessParameterDetailVO.getValue())) {
            return;
        }
        Integer pwdLimitLen = Integer.parseInt(businessParameterDetailVO.getValue());
        if (pwdLimitLen > password.length()) {
            throw new BmosException(PlatformResponseCode.PASSWORD_VALID_MIN_LENGTH);
        }
    }

    /**
     * 校验密码历史是否相同
     *
     * @param user
     * @param password
     */
    private void validPwdRuleHistory(User user, String password) {
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterRepository.detailByCode(BusinessParameterCodeConstants.PLATFORM_USER_PWD_RULE_HIS_NUM);
        if (Objects.isNull(businessParameterDetailVO) || StrUtil.isEmpty(businessParameterDetailVO.getValue())) {
            return;
        }
        int pwdHistoryNum = Integer.parseInt(businessParameterDetailVO.getValue());
        if (Objects.equals(pwdHistoryNum, 0)) {
            return;
        }
        List<PasswordHistory> passwordHistories = passwordHistoryMapper.selectLastNPwd(user.getUserId(), pwdHistoryNum);
        if (CollectionUtil.isEmpty(passwordHistories)) {
            return;
        }
        for (PasswordHistory passwordHistory : passwordHistories) {
            String oldPwd = des.decryptStr(passwordHistory.getPwd());
            if (StrUtil.equals(oldPwd, password)) {
                throw new BmosException(PlatformResponseCode.PASSWORD_VALID_HISTORY);
            }
        }
    }

    /**
     * 校验密码中是否含有配置中的特定字符
     *
     * @param password
     * @param businessParameterCode
     */
    @Override
    public void validPwdRuleCharacter(String password, String businessParameterCode) {
        BusinessParameterDetailVO detailVO = businessParameterRepository.detailByCode(businessParameterCode);
        if (Objects.isNull(detailVO) || StrUtil.isEmpty(detailVO.getValue())) {
            return;
        }
        PlatformPwdRule pwdRule = JsonUtils.parseObject(detailVO.getValue(), PlatformPwdRule.class);
        boolean hasLowerCase = !(!Objects.isNull(pwdRule.getLowerCase()) && pwdRule.getLowerCase());
        boolean hasUpperCase = !(!Objects.isNull(pwdRule.getUpperCase()) && pwdRule.getUpperCase());
        boolean hasDigit = !(!Objects.isNull(pwdRule.getDigit()) && pwdRule.getDigit());
        char[] chars = password.toCharArray();
        for (char ch : chars) {
            if (!hasLowerCase && Character.isLowerCase(ch)) {
                hasLowerCase = true;
                continue;
            }
            if (!hasUpperCase && Character.isUpperCase(ch)) {
                hasUpperCase = true;
                continue;
            }
            if (!hasDigit && Character.isDigit(ch)) {
                hasDigit = true;
            }
        }
        // 校验是否包含特殊字符
        boolean hasSpecialCharacter = CollectionUtil.isEmpty(pwdRule.getSpecialCharacters());
        if (!hasSpecialCharacter) {
            for (String specialCharacter : pwdRule.getSpecialCharacters()) {
                if (password.contains(specialCharacter)) {
                    hasSpecialCharacter = true;
                    break;
                }
            }
        }
        // 使用配置校验密码复杂度
        if (!hasUpperCase) {
            throw new BmosException(PlatformResponseCode.PASSWORD_VALID_UPPERCASE);
        }
        if (!hasLowerCase) {
            throw new BmosException(PlatformResponseCode.PASSWORD_VALID_LOWERCASE);
        }
        if (!hasDigit) {
            throw new BmosException(PlatformResponseCode.PASSWORD_VALID_DIGIT);
        }
        if (!hasSpecialCharacter) {
            throw new BmosException(PlatformResponseCode.PASSWORD_VALID_SPECIAL, JsonUtils.toJsonString(pwdRule.getSpecialCharacters()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deptUserBindRole(DeptUserBindRoleDTO dto) {
        // 1. 删除用户与角色之间的绑定关系
        userRelateRoleMapper.deleteByUserIdAndRoleIdList(dto.getUserId(), dto.getAllRoleIds());
        // 进行绑定
        List<UserRelateRole> userRelateRoleList = UserRelateRoleConvert.INSTANCE.convert2RelateRole(dto.getUserId(), dto.getRoleIds());
        if (CollUtil.isEmpty(userRelateRoleList)) {
            return;
        }
        userRelateRoleMapper.insertBatch(userRelateRoleList);
    }

    @Override
    public void unLockUser(String userId) {
        User user = userMapper.selectByUserId(userId);
        if (Objects.isNull(user)) {
            throw new BmosException(PlatformResponseCode.USER_NOT_EXIST);
        }
        if (!ActiveEnum.PASSWORD_LOCK.getCode().equals(user.getActiveStatus())) {
            throw new BmosException(PlatformResponseCode.USER_NOT_LOCK);
        }
        this.doUnLockUser(user);
        userMapper.updateById(user);
    }

    @Override
    public void pwdExpireValid() {
        log.info("密码有效期自动变更定时器开始");
        List<User> users = userMapper.selectActiveUserList();
        // 校验过期时间是否过期
        if (CollectionUtil.isEmpty(users)) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        List<User> expiredUsers = new ArrayList<>();
        for (User user : users) {
            if (currentTimeMillis >= user.getValidTime()) {
                user.setActiveStatus(ActiveEnum.PASSWORD_EXPIRED.getCode());
                expiredUsers.add(user);
            }
        }
        if (CollectionUtil.isNotEmpty(expiredUsers)) {
            userMapper.updateBatch(expiredUsers);
        }
        log.info("密码有效期自动变更定时器结束");
    }

    @Override
    public void userAutoUnLockExpireValid() {
        log.info("开始执行账户自动解锁定时器");
        List<User> userList = userMapper.selectNeedUnLockUser();
        if (CollectionUtil.isEmpty(userList)) {
            log.info("账户自动解锁定时器执行完成，没有需要解锁的账户");
            return;
        }
        List<String> unLockAccountList = new ArrayList<>();
        for (User user : userList) {
            this.doUnLockUser(user);
            unLockAccountList.add(user.getUserName());
        }
        userMapper.updateBatch(userList);
        log.info("账户自动解锁定时器执行完成，解锁账户：{}", unLockAccountList);
    }

    @Override
    public List<FeignUserVO> listUserListByDeptIds(List<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return new ArrayList<>();
        }
        List<UserListItemVO> userListItem = userMapper.selectByDeptIds(deptIds);
        return BeanUtil.copyToList(userListItem, FeignUserVO.class);
    }

    @Override
    public List<FeignUserVO> listUserListByRoleIds(List<Long> roles) {
        if (CollUtil.isEmpty(roles)) {
            return new ArrayList<>();
        }
        List<UserListItemVO> userListItem = userMapper.selectByRoleIds(roles);
        return BeanUtil.copyToList(userListItem, FeignUserVO.class);
    }

    @Override
    public List<FeignUserVO> listByName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        lambda.like(User::getUserName, userName);
        List<User> users = userMapper.selectList(lambda);
        return BeanUtil.copyToList(users, FeignUserVO.class);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        SheetDataBo sheetDataBo = new SheetDataBo(ExcelI18nUtil.getI18n(UserConstant.IMPORT_NAME), UserTemplateVO.class, null,
                getOptions());
        try {
            ExcelWriterUtils.write(UserConstant.TEMPLATE_NAME, response, Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("生成模板出错", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationUserDefined(operationObject = "#operationObject")
    public void importUser(HttpServletResponse response, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<UserTemplateVO> userList = ExcelReaderUtils.read(inputStream, UserTemplateVO.class,
                    UserConstant.IMPORT_NAME);
            if (CollUtil.isEmpty(userList)) {
                throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_IMPORT_FILE_ERROR);
            }
            Pair<Boolean, List<UserImportErrorVO>> booleanListPair = this.importUserValid(userList);
            if (!booleanListPair.getLeft()) {
                this.writeErrorExcel(response, booleanListPair.getRight());
                return;
            }
            this.saveUserBatch(userList);
            OperationUserDefinedContext.putVariable("operationObject", JsonUtils.toJsonString(userList));
        } catch (Exception e) {
            log.error("读取文件失败", e);
        }
    }

    @Override
    @OperationUserDefined(operationObject = "#operationObject")
    public void exportUser(HttpServletResponse response, UserPageQueryDTO dto) {
        if (ObjectUtil.isNotNull(dto.getIsFlay()) && BooleanUtil.isTrue(dto.getIsFlay())) {
            dto.setPageNum(1);
            dto.setPageSize(1000000);
        }
        CommonPage<UserPageVO> page = this.getPage(dto);
        List<UserExportVO> exportList = UserConvert.INSTANCE.convertUserExportVO(page.getList());
        SheetDataBo sheetDataBo = new SheetDataBo(UserConstant.IMPORT_NAME, UserExportVO.class,
                exportList, null);
        try {
            ExcelWriterUtils.write(UserConstant.USER_EXPORT_NAME, response, Lists.newArrayList(sheetDataBo));
            OperationUserDefinedContext.putVariable("operationObject", JsonUtils.toJsonString(exportList));
        } catch (Exception e) {
            log.error("导出数据出错", e);
            throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_EXPORT_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUserBatch(List<UserTemplateVO> userList) {
        //赋值为默认密码t
        List<User> users = UserConvert.INSTANCE.convertUserList(userList);
        users.forEach(user -> {
            user.setPassword(UserConstant.USER_PASSWORD);
            user.setUserId(String.valueOf(IdUtil.getSnowflakeNextId()));
            // 新增我们系统的数据
            user.setPassword(des.encryptHex(user.getPassword()));
            user.setValidTime(getPwdValidTime());
        });
        userMapper.insertBatch(users);
        permissionRedisDao.batchSetUserInfo(UserConvert.INSTANCE.convertUserVOList(users));
    }

    private void writeErrorExcel(HttpServletResponse response, List<UserImportErrorVO> userImportError) {

        SheetDataBo sheetDataBo = new SheetDataBo(ExcelI18nUtil.getI18n(UserConstant.IMPORT_NAME), UserImportErrorVO.class,
                userImportError, getOptions());
        try {
            response.setHeader("error-message", URLEncoder.encode("存在错误数据请处理后重新上传", "utf-8"));
            ExcelWriterUtils.write(UserConstant.EXPORT_ERROR_FILE_NAME, response,
                    Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("导出数据出错", e);
        }
    }

    private Pair<Boolean, List<UserImportErrorVO>> importUserValid(List<UserTemplateVO> userList) {
        List<String> loginNameList = CollectionUtils.convertList(userList, UserTemplateVO::getLoginName);
        List<User> users = userMapper.selectByLoginNames(loginNameList);
        boolean noeError = true;
        List<UserImportErrorVO> errorVo = new ArrayList<>();
        for (UserTemplateVO user : userList) {
            StringBuffer errorMsgBuilder = new StringBuffer();
            if (StrUtil.isBlank(user.getUserName())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.USER_NAME_NULL_ERROR.getCode(), "")).append(";");
            }
            if (StrUtil.isBlank(user.getLoginName())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.LOGIN_NAME_NULL_ERROR.getCode(), "")).append(";");
            }
            if (ObjectUtil.isEmpty(user.getGenderEnum())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.GENDER_NULL_ERROR.getCode(), "")).append(";");
            }
            Map<String, User> userMap = CollectionUtils.convertMap(users, User::getLoginName);
            if (ObjectUtil.isNotEmpty(userMap.get(user.getLoginName()))) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.USER_EXISTS.getCode(), "")).append(";");
            }
            if (StrUtil.isNotBlank(user.getLoginName()) &&
                    (BooleanUtil.isFalse(PATTERN.matcher(user.getLoginName()).matches()) || user.getLoginName().length() > 18)) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.LOGIN_NAME_ERROR.getCode(), "")).append(";");
            }
            user.setGender(ObjectUtil.isEmpty(user.getGenderEnum()) ? null : user.getGenderEnum().getCode());
            noeError &= StringUtils.isEmpty(errorMsgBuilder.toString());
            UserImportErrorVO error = BeanUtil.copyProperties(user, UserImportErrorVO.class);
            error.setErrorMsg(errorMsgBuilder.toString());
            errorVo.add(error);
        }
        return Pair.of(noeError, errorVo);
    }

    private List<OptionBo> getOptions() {
        List<String> nameList = GenderEnum.getNameList();
        OptionBo nameDownList = new OptionBo(ExcelI18nUtil.getI18n(UserConstant.PULL_DOWN_NAME), 2,
                nameList.stream().map(ExcelI18nUtil::getI18n).collect(Collectors.toList()));
        return Lists.newArrayList(nameDownList);
    }

    /**
     * 获取密码有效期时间
     *
     * @return
     */
    private Long getPwdValidTime() {
        // 有效期启用配置
        Integer pwdValidDates = UserConstant.PASSWORD_VALIDATE.getPwdValidDates();
        BusinessParameterDetailVO detailVO = businessParameterRepository.detailByCode(BusinessParameterCodeConstants.PLATFORM_USER_PWD_RULE_VALIDITY);
        if (StrUtil.isNotEmpty(detailVO.getValue())) {
            pwdValidDates = Integer.parseInt(detailVO.getValue());
        }
        return System.currentTimeMillis() + pwdValidDates * 24 * 60 * 60 * 1000L;

    }

    /**
     * 对用户进行密码修改
     *
     * @param user
     * @param newPassword
     */
    private void changePwd(User user, String newPassword) {
        // 密码规则校验
        validatePassword(user, newPassword);
        //设置密码有效期
        if (!AdminUtil.isAdminUser(user.getUserId())) {
            // 非Admin用户需要更新密码有效期
            user.setValidTime(getPwdValidTime());
        }
        // 密码输入错误次数置0
        user.setPwdErrorCount(0);
        if (Objects.equals(UserActiveEnums.PASSWORD_LOCK.getValue(), user.getActiveStatus())) {
            this.doUnLockUser(user);
        }
        // 对新密码进行加密
        user.setPassword(des.encryptHex(newPassword));
        // 修改密码后 密码状态为已激活
        user.setActiveStatus(UserActiveEnums.ACTIVATE.getValue());
        User userInfo = permissionRedisDao.getUserInfo(user.getUserId());
        userInfo.setActivated(true);
        // 锁定时间以及锁定前的状态也要变更
        userInfo.setLockPreviewStatus(null);
        userInfo.setUnlockTime(null);
        // 更新缓存
        permissionRedisDao.cacheUserInfo(userInfo);
        userMapper.updateById(user);
        // 当前登录人密码次数+1
        passwordHistoryMapper.insert(PasswordHistory.builder().userId(user.getUserId()).pwd(user.getPassword()).build());
    }

    /**
     * 历史错误密码校验
     *
     * @param user
     * @return
     */
    private User userPasswordErrorCountValidate(User user) {
        int pwdErrorCount = user.getPwdErrorCount() + 1;
        user.setPwdErrorCount(pwdErrorCount);
        BusinessParameterDetailVO detailVO = businessParameterRepository.detailByCode(BusinessParameterCodeConstants.PLATFORM_USER_PWD_RULE_TRY_NUM);
        if (Objects.isNull(detailVO) || StrUtil.isEmpty(detailVO.getValue())) {
            return user;
        }
        int pwdErrorLimitNum = Integer.parseInt(detailVO.getValue());
        if (pwdErrorLimitNum == 0) {
            return user;
        }
        // 若密码重试次数超过配置 则锁定用户
        if (user.getPwdErrorCount() > pwdErrorLimitNum) {
            this.lockUser(user);
        }
        return user;
    }

    /**
     * 锁定密码操作
     * 1. 账户状态设为密码已锁定
     * 2. 账户锁定时间，从参数配置中获取
     *
     * @param user
     */
    private void lockUser(User user) {
        if (ObjectUtil.isNull(user.getPwdErrorCount())) {
            return;
        }
        // 记录之前的账户状态 账户状态设为密码已锁定
        user.setLockPreviewStatus(user.getActiveStatus());
        user.setActiveStatus(UserActiveEnums.PASSWORD_LOCK.getValue());
        // 从配置中获取锁定时间配置
        BusinessParameterDetailVO detailVO = businessParameterRepository.detailByCode(BusinessParameterCodeConstants.PLATFORM_USER_LOGIN_AUTO_UNLOCK_TIME);
        if (ObjectUtil.isNull(detailVO) || StrUtil.isEmpty(detailVO.getValue())) {
            return;
        }
        try {
            int unLockLimitMinute = Integer.parseInt(detailVO.getValue());
            if (unLockLimitMinute == BusinessParameterCodeConstants.PERMANENT_UNLOCK_PLACEHOLDER) {
                // 永久锁定
                return;
            }
            LocalDateTime unLockTime = LocalDateTimeUtil.offset(LocalDateTime.now(), unLockLimitMinute, ChronoUnit.MINUTES);
            user.setUnlockTime(unLockTime);
        } catch (Exception e) {
            log.error("密码锁定时间配置错误，请检查配置", e);
        }
    }

    private Long validateLogin(User sysUser, UserLoginDTO dto) {
        //校验 激活状态
        this.validateUserActiveStatus(sysUser, false);

        this.validateUserState(sysUser);

        // 密码校验
        String password = RsaUtils.decryptPwd(dto.getPassword());
        if (!des.decryptStr(sysUser.getPassword()).equals(password)) {
            // 管理员不更新密码错误次数(其他用户使用)
            if (!AdminUtil.isAdminUser(sysUser.getUserId())) {
                userMapper.updateById(userPasswordErrorCountValidate(sysUser));
            }
            // 最后一次输入错密码
            if (UserActiveEnums.PASSWORD_LOCK.getValue().equals(sysUser.getActiveStatus())) {
                // 代表最后一次输错密码
                this.validateUserActiveStatus(sysUser, true);
            } else {
                loginLogService.insert(getLoginLogModel(sysUser, false, LoginActionEnum.LOG_IN, PlatformResponseCode.LOGIN_ERROR, null));
                throw new BmosException(PlatformResponseCode.LOGIN_ERROR);
            }
        }
        //校验密码有效期
        if (UserActiveEnums.ACTIVATE.getValue().equals(sysUser.getActiveStatus()) &&
                sysUser.getValidTime() < System.currentTimeMillis()) {
            sysUser.setActiveStatus(UserActiveEnums.PASSWORD_EXPIRED.getValue());
        }
        // 根据时间戳转换为LocalDateTime
        LocalDateTime localDateTime = TimeUtils.convertTimeStampToLocalDateTime(sysUser.getValidTime());
        return Math.abs(LocalDateTimeUtil.between(localDateTime, LocalDateTime.now(), ChronoUnit.DAYS));
    }

    private void recordLoginLog(User sysUser, BmosException bmosException) {
        if (Objects.nonNull(bmosException)) {
            // 记录登录失败日志
            loginLogService.insert(getLoginLogModel(sysUser, false, LoginActionEnum.LOG_IN, bmosException.getResponseItem(), null));
            throw bmosException;
        }
    }

    private void doUnLockUser(User user) {
        // 解锁过后回到之前的状态
        if (Objects.nonNull(user.getLockPreviewStatus())) {
            user.setActiveStatus(user.getLockPreviewStatus());
        } else {
            user.setActiveStatus(ActiveEnum.TO_BE_ACTIVATE.getCode());
        }
        user.setPwdErrorCount(0);
        user.setUnlockTime(null);
        user.setLockPreviewStatus(null);
    }

}
