package com.bmos.platform.service.system.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.system.role.vo.FeignRoleVO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.dept.model.DeptRole;
import com.bmos.platform.service.system.dept.service.DeptService;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import com.bmos.platform.service.system.menu.constant.MenuConstant;
import com.bmos.platform.service.system.menu.mapper.MenuMapper;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.menu.service.MenuService;
import com.bmos.platform.service.system.role.convert.RoleConvert;
import com.bmos.platform.service.system.role.dto.*;
import com.bmos.platform.service.system.role.mapper.RoleMapper;
import com.bmos.platform.service.system.role.mapper.RoleRelateMenuMapper;
import com.bmos.platform.service.system.role.mapper.RoleTypeMapper;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import com.bmos.platform.service.system.role.model.RoleType;
import com.bmos.platform.service.system.role.service.AuthRoleMenuService;
import com.bmos.platform.service.system.role.service.RoleMenuRelationService;
import com.bmos.platform.service.system.role.service.RoleService;
import com.bmos.platform.service.system.role.vo.*;
import com.bmos.platform.service.system.user.convert.UserRelateRoleConvert;
import com.bmos.platform.service.system.user.dto.UserRelateRoleSaveDTO;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.mapper.UserRelateRoleMapper;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.model.UserRelateRole;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleTypeMapper roleTypeMapper;

    @Autowired
    private UserRelateRoleMapper userRelateRoleMapper;

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    @Autowired
    private RoleRelateMenuMapper roleRelateMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<RoleTypeTreeVO> treeAll(String name) {
        List<RoleTypeTreeVO> allList = roleMapper.selectAllRoleType(name);
        if (CollUtil.isEmpty(allList)) {
            return Collections.emptyList();
        }
        Map<Long, String> map = CollectionUtils.convertMap(allList, RoleTypeTreeVO::getId, RoleTypeTreeVO::getRoleTypeName);
        allList.forEach(item -> item.setParentName(map.get(item.getParentId())));
        return TreeUtil.buildTree(allList,true);
    }


    @Override
    public List<RoleTypeTreeVO> getRoleTree(Long menuId) {
        if (ObjectUtil.isNull(menuId)){
            // 查询角色类型树
            List<RoleTypeTreeVO> roleTypes = roleMapper.selectTypeTreeList();
            List<Role> roles = roleMapper.selectAll();
            Map<Long, List<Role>> roleMap = roles.stream().collect(Collectors.groupingBy(Role::getRoleTypeId));
            Map<Long, RoleTypeTreeVO> roleTypeMap = CollectionUtils.convertMap(roleTypes, RoleTypeTreeVO::getId);
            for (Long roleTypeId : roleMap.keySet()) {
                if (!roleTypeMap.containsKey(roleTypeId)){
                    continue;
                }
                RoleTypeTreeVO roleTypeTreeVO = roleTypeMap.get(roleTypeId);
                roleTypeTreeVO.setRoleList(RoleConvert.INSTANCE.convert2RoleTypeVOList(roleMap.get(roleTypeId)));
            }
            return TreeUtil.buildTree(roleTypes, true);
        } else {
            return this.getMenuRoleTree(menuId);
        }
    }


    @Override
    public CommonPage<RoleVO> getRole(RoleQueryDTO dto) {
        if (Objects.nonNull(dto.getRoleTypeId())){
            List<RoleTypeTreeVO> roleTypeTreeList = this.treeAll(null);
            List<Long> targetChild = this.findTargetChild(dto.getRoleTypeId(), roleTypeTreeList);
            dto.setRoleTypeIdList(targetChild);
        }
        return CommonPage.convertPage(roleMapper.selectRole(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleType(RoleTypeSaveDTO dto) {
        roleTypeMapper.insert(RoleConvert.INSTANCE.convert(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(RoleSaveDTO dto) {
        roleMapper.insert(RoleConvert.INSTANCE.convert(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleType(RoleTypeUpdateDTO dto) {
        RoleType roleType = roleTypeMapper.selectById(dto.getId());
        roleType.setRoleTypeName(dto.getRoleTypeName());
        roleTypeMapper.updateById(roleType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleType(Long id) {
        if (AdminUtil.isAdminRole(id)) {
            throw new BmosException(PlatformResponseCode.ADMIN_ROLE_CAN_NOT_BE_MODIFY);
        }
        if (roleTypeMapper.existsSubType(id)){
            throw new BmosException(PlatformResponseCode.ROLE_EXIST_SUB_INFO);
        }
        if (roleMapper.existsByType(id)){
            throw new BmosException(PlatformResponseCode.ROLE_EXIST_SUB_INFO);
        }
        roleTypeMapper.deleteById(id);
    }

    @Override
    public Boolean validateRole(String roleName, Long id) {
        return roleMapper.validateRole(roleName, id);
    }

    @Override
    public Boolean validateRoleType(RoleTypeValidateDTO dto) {
        return roleTypeMapper.validateRoleType(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateDTO dto) {
        if (AdminUtil.isAdminRole(dto.getId())) {
            throw new BmosException(PlatformResponseCode.ADMIN_ROLE_CAN_NOT_BE_MODIFY);
        }
        Role role = roleMapper.selectById(dto.getId());
        role.setRoleName(dto.getRoleName());
        role.setRoleTypeId(dto.getRoleTypeId());
        role.setDescription(dto.getDescription());
        roleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        if (userRelateRoleMapper.existUser(id)) {
            throw new BmosException(PlatformResponseCode.ROLE_EXIST_USER);
        }
        roleMapper.deleteById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relateUserSave(UserRelateRoleSaveDTO dto) {
        //先删除过去的数据
        List<UserRelateRole> deleteList = userRelateRoleMapper.getListByRoleId(dto.getRoleId());
        if (CollUtil.isNotEmpty(deleteList)) {
            userRelateRoleMapper.deleteBatchIds(deleteList);
        }
        //再新增要添加的数据
        if (CollUtil.isNotEmpty(dto.getItems())) {
            userRelateRoleMapper.saveOrUpdateBatch(UserRelateRoleConvert.INSTANCE.convert(dto.getItems()));
        }
    }

    @Override
    public List<RoleMenuIdVO> getMenuIds(Long roleId) {
        // 获取当前登陆人拥有的授权菜单
        Set<Long> curRoleMenuIds = getLoginRoleMenuIds(roleId, true);
        Map<Long, RoleMenuIdVO> roleMenuIdVOMap = new HashMap<>();
        if (CollUtil.isEmpty(curRoleMenuIds)){
            return Collections.emptyList();
        }
        List<Menu> menuList = menuMapper.selectBatchIds(curRoleMenuIds);
        Map<Long, Menu> menuMap = menuList.stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
        for (Long curRoleMenuId : curRoleMenuIds) {
            Long rootMenuId = Long.valueOf(StrUtil.sub(String.valueOf(curRoleMenuId), 0, 3));
            if (roleMenuIdVOMap.containsKey(rootMenuId)){
                if (MenuConstant.IS_MENU.equals(menuMap.get(curRoleMenuId).getIsMenu())){
                    roleMenuIdVOMap.get(rootMenuId).getMenuIds().add(curRoleMenuId);
                } else {
                    roleMenuIdVOMap.get(rootMenuId).getFuncIds().add(curRoleMenuId);
                }
            } else {
                RoleMenuIdVO roleMenuIdVO = new RoleMenuIdVO();
                List<Long> menuIdList =  Lists.newArrayList();
                List<Long> funcIdList = Lists.newArrayList();
                if (MenuConstant.IS_MENU.equals(menuMap.get(curRoleMenuId).getIsMenu())){
                    menuIdList.add(curRoleMenuId);
                } else {
                    funcIdList.add(curRoleMenuId);
                }
                roleMenuIdVO.setMenuIds(menuIdList);
                roleMenuIdVO.setFuncIds(funcIdList);
                roleMenuIdVO.setRootMenuId(rootMenuId);
                roleMenuIdVOMap.put(rootMenuId, roleMenuIdVO);
            }
        }
        return new ArrayList<>(roleMenuIdVOMap.values());
    }

    @Override
    public List<RoleAuthMenuVO> getAuthMenuIds(Long roleId) {
        Set<Long> roleAuthMenu = authRoleMenuService.selectByRoleIdList(Lists.newArrayList(roleId));
        if (CollUtil.isEmpty(roleAuthMenu)){
            return new ArrayList<>();
        }
        Map<Long, RoleAuthMenuVO> roleAuthMenuVOMap = new HashMap<>();
        List<Menu> menuList = menuMapper.selectBatchIds(roleAuthMenu);
        Map<Long, Menu> menuMap = menuList.stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
        for (Long curRoleMenuId : roleAuthMenu) {
            if (Objects.isNull(menuMap.get(curRoleMenuId))){
                continue;
            }
            Long rootMenuId = Long.valueOf(StrUtil.sub(String.valueOf(curRoleMenuId), 0, 3));
            if (roleAuthMenuVOMap.containsKey(rootMenuId)){
                if (MenuConstant.IS_MENU.equals(menuMap.get(curRoleMenuId).getIsMenu())){
                    roleAuthMenuVOMap.get(rootMenuId).getMenuIds().add(curRoleMenuId);
                } else {
                    roleAuthMenuVOMap.get(rootMenuId).getFuncIds().add(curRoleMenuId);
                }
            } else {
                RoleAuthMenuVO roleAuthMenuVO = new RoleAuthMenuVO();
                List<Long> menuIdList =  Lists.newArrayList();
                List<Long> funcIdList = Lists.newArrayList();
                if (MenuConstant.IS_MENU.equals(menuMap.get(curRoleMenuId).getIsMenu())){
                    menuIdList.add(curRoleMenuId);
                } else {
                    funcIdList.add(curRoleMenuId);
                }
                roleAuthMenuVO.setMenuIds(menuIdList);
                roleAuthMenuVO.setFuncIds(funcIdList);
                roleAuthMenuVO.setRootMenuId(rootMenuId);
                roleAuthMenuVOMap.put(rootMenuId, roleAuthMenuVO);
            }
        }
        return new ArrayList<>(roleAuthMenuVOMap.values());
    }

    @Override
    public List<DeptUserTreeVO> relateUserData(Long roleId) {
        List<User> users = roleMapper.selectUserByRoleId(roleId);
        return deptService.getDeptUserTreeByUsers(users);
    }

    @Override
    public List<Long> getRoleIdList() {
        return userMapper.relateRoleData((SysUserHolder.getUser().getUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    @DistributedLock(key = "saveRoleMenu")
    public void saveRoleMenu(RoleMenuSaveDTO dto) {
        if (CollUtil.isEmpty(dto.getItems())){
            // 代表用户没有做过任何操作
            return ;
        }
        // 当前角色id在当前登陆人下的有权限的菜单权限(不包含功能权限)
        List<RoleMenuIdVO> menuIds = this.getMenuIds(dto.getRoleId());
        // 当前登陆用户点击过的所有根节点
        Set<Long> chooseRootMenuIdSet = new HashSet<>();
        Set<Long> deleteRootMenuIdSet = new HashSet<>();
        // 当前登陆用户点击过的所有菜单
        Set<Long> chooseIdSet = new HashSet<>();
        // 需要将原有的菜单与角色进行删除
        List<Long> deleteIdList = new ArrayList<>();
        // 需要进行授权的菜单
        Set<Long> addIdList = new HashSet<>();
        for (RoleMenuSaveItemDTO item : dto.getItems()) {
            if (CollUtil.isNotEmpty(item.getDelFuncIds())){
                deleteIdList.addAll(item.getDelFuncIds());
            }
            if (CollUtil.isEmpty(item.getMenuIds())){
                deleteIdList.addAll(item.getMenuIds());
                deleteRootMenuIdSet.add(item.getRootMenuId());
                continue;
            }
            chooseRootMenuIdSet.add(item.getRootMenuId());
            chooseIdSet.addAll(item.getMenuIds());
        }
        Set<Long> preMenuIdSet = new HashSet<>();
        Set<Long> preFuncIdSet = new HashSet<>();
        Set<Long> preRootMenuIdSet = new HashSet<>();
        for (RoleMenuIdVO menuIdVO : menuIds) {
            if (deleteRootMenuIdSet.contains(menuIdVO.getRootMenuId())){
                deleteIdList.addAll(menuIdVO.getMenuIds().stream().filter(e->!ObjectUtil.equals(menuIdVO.getRootMenuId(), e)).collect(Collectors.toList()));
                continue;
            }
            if (!chooseRootMenuIdSet.contains(menuIdVO.getRootMenuId())){
                continue;
            }
            preRootMenuIdSet.add(menuIdVO.getRootMenuId());
            preMenuIdSet.addAll(menuIdVO.getMenuIds());
            preFuncIdSet.addAll(menuIdVO.getFuncIds());
        }
        // 筛选出需要删除的菜单
        for (Long preMenuId : preMenuIdSet) {
            // 若是根节点则其可能不需要删除
            if (preRootMenuIdSet.contains(preMenuId) || chooseIdSet.contains(preMenuId)){
                continue;
            }
            deleteIdList.add(preMenuId);
        }
        chooseIdSet.addAll(chooseRootMenuIdSet);
        for (Long menuId : chooseIdSet) {
            if (preMenuIdSet.contains(menuId) || preFuncIdSet.contains(menuId)){
                continue;
            }
            addIdList.add(menuId);
        }
        if (CollUtil.isNotEmpty(deleteIdList)){
            roleMenuRelationService.deleteByRoleIdAndRootMenuIds(dto.getRoleId(), deleteIdList);
        }
        // 校验是否需要删除根节点权限
        if (CollUtil.isNotEmpty(deleteRootMenuIdSet)){
            List<Long> deleteRootMenuIdList = weedoutNotDeleteRootList(dto.getRoleId(), deleteRootMenuIdSet);
            if (CollUtil.isNotEmpty(deleteRootMenuIdList)){
                roleMenuRelationService.deleteByRoleIdAndRootMenuIds(dto.getRoleId(), deleteRootMenuIdList);
            }
        }
        // 校验是否有根节点菜单权限
        if (CollUtil.isNotEmpty(chooseRootMenuIdSet)){
            List<RoleRelateMenu> roleRelateMenuList = roleRelateMenuMapper.selectByMenuIdList(chooseRootMenuIdSet);
            if (CollUtil.isEmpty(roleRelateMenuList)){
                addIdList.addAll(chooseRootMenuIdSet);
            } else {
                Set<Long> rootMenuIdSet = CollectionUtils.convertSet(roleRelateMenuList, RoleRelateMenu::getMenuId);
                addIdList.addAll(chooseRootMenuIdSet.stream().filter(x -> !rootMenuIdSet.contains(x)).collect(Collectors.toList()));
            }
        }
        List<RoleRelateMenu> roleRelateMenus = RoleConvert.INSTANCE.convert(dto.getRoleId(), addIdList);
        if (CollUtil.isNotEmpty(roleRelateMenus)){
            roleMenuRelationService.saveBatch(roleRelateMenus);
        }
    }

    @Override
    @OperationLog
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "saveAuthRoleMenu")
    public void saveAuthRoleMenu(RoleMenuSaveDTO dto) {
        // 重新绑定
        if (CollUtil.isEmpty(dto.getItems())){
            return ;
        }
        Set<Long> bindMenuIds = new HashSet<>();
        Set<Long> deleteMenuIds = new HashSet<>();
        // 查询当前角色拥有的所有的菜单那
        List<RoleAuthMenuVO> authMenuIds = this.getAuthMenuIds(dto.getRoleId());
        Map<Long, RoleAuthMenuVO> alreadyMenuMap = CollUtil.isEmpty(authMenuIds) ? new HashMap<>() : authMenuIds.stream().collect(Collectors.toMap(RoleAuthMenuVO::getRootMenuId, e -> e));
        for (RoleMenuSaveItemDTO item : dto.getItems()) {
            // 必定需要删除顶层菜单 后续在判定是否需要重新绑定
            deleteMenuIds.add(item.getRootMenuId());
            if (CollUtil.isNotEmpty(item.getDelFuncIds())){
                // 必定需要删除功能列表
                deleteMenuIds.addAll(item.getDelFuncIds());
            }

            // 需要删除的菜单
            RoleAuthMenuVO alreadyAuthMenuVO = alreadyMenuMap.get(item.getRootMenuId());
            if (Objects.nonNull(alreadyAuthMenuVO) && CollUtil.isNotEmpty(alreadyAuthMenuVO.getMenuIds())){
                // 删除当前选中的顶级菜单下的所有菜单列表 后续在判定进行哪些绑定
                deleteMenuIds.addAll(alreadyAuthMenuVO.getMenuIds());
            }
            if (CollUtil.isNotEmpty(item.getMenuIds())){
                // 先将已经选中的全部解绑
                deleteMenuIds.addAll(item.getMenuIds());
                bindMenuIds.add(item.getRootMenuId());
                bindMenuIds.addAll(item.getMenuIds());
            }
        }
        // 删除菜单
        authRoleMenuService.deleteByRoleIdAndMenuIds(dto.getRoleId(), deleteMenuIds);
        // 添加菜单
        authRoleMenuService.bindRoleAuthMenu(dto.getRoleId(), bindMenuIds);
    }

    private List<Long> weedoutNotDeleteRootList(Long roleId, Set<Long> deleteRootMenuIdSet) {
        List<Menu> menuList = menuMapper.selectByParentIdList(deleteRootMenuIdSet);
        if (CollUtil.isEmpty(menuList)){
            return new ArrayList<>(deleteRootMenuIdSet);
        }
        Map<Long, Menu> menuMap = CollectionUtils.convertMap(menuList, Menu::getId);
        List<Long> menuIdList = CollectionUtils.convertList(menuList, Menu::getId);
        List<RoleRelateMenu> roleRelateMenuList = roleMenuRelationService.selectByRoleIdAndMenuIds(roleId, menuIdList);
        Map<Long, List<Menu>> parentMenuMap = new HashMap<>();
        for (RoleRelateMenu roleRelateMenu : roleRelateMenuList) {
            Menu menu = menuMap.get(roleRelateMenu.getMenuId());
            if (parentMenuMap.containsKey(menu.getParentId())){
                parentMenuMap.get(menu.getParentId()).add(menu);
            } else {
                parentMenuMap.put(menu.getParentId(), Lists.newArrayList(menu));
            }
        }
        List<Long> weedOutDeleteRootMenuIdSet = new ArrayList<>();
        for (Long deleteRootMenuId : deleteRootMenuIdSet) {
            if (CollUtil.isNotEmpty(parentMenuMap.get(deleteRootMenuId))){
                continue;
            }
            weedOutDeleteRootMenuIdSet.add(deleteRootMenuId);
        }
        return weedOutDeleteRootMenuIdSet;
    }

    /**
     * 获取当前登录用户所拥有的授权菜单集合
     * @param containsFunc 是否包含功能
     * @return
     */
    private List<Menu> getLoginUserAuthMenu(Boolean containsFunc) {
        List<Menu> curLoginUserAuthMenu;
        SysUser user = SysUserHolder.getUser();
        if (AdminUtil.isAdminUser(user.getUserId())){
            // 查询菜单表中的所有菜单id
            curLoginUserAuthMenu = menuMapper.selectAdminMenuIdSet(containsFunc);
        }else {
            List<UserRelateRole> userRelateRoles = userRelateRoleMapper.selectByUserId(user.getUserId());
            if (CollUtil.isEmpty(userRelateRoles)){
                return Collections.emptyList();
            }
            Set<Long> curUserRoleIdSet = userRelateRoles.stream().map(UserRelateRole::getRoleId).collect(Collectors.toSet());
            Set<Long> curLoginUserAuthMenuIdSet = authRoleMenuService.selectByRoleIdList(new ArrayList<>(curUserRoleIdSet));
            curLoginUserAuthMenu = menuMapper.selectMenuIdList(curLoginUserAuthMenuIdSet, containsFunc);
        }
        return curLoginUserAuthMenu;
    }

    @Override
    public List<RoleTreeNodeVO> getAggregateTree() {
        List<Role> roles = roleMapper.selectAdminRole();
        if (CollUtil.isEmpty(roles)) {
            return Collections.emptyList();
        }
        List<RoleType> roleTypes = roleTypeMapper.selectList();
        List<RoleTreeNodeVO> nodes = RoleConvert.INSTANCE.convertTreeNode(roles, roleTypes);
        return TreeUtil.buildTree(nodes, true);
    }


    @Override
    public List<RoleTreeNodeVO> getAuthRoleTree(Long menuId) {
        List<Long> roleIds = authRoleMenuService.getAuthRoleIdsByMenuId(menuId);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return buildTree(roles);
    }

    @Override
    public List<Long> getAuthRoleList(Long menuId) {
        return authRoleMenuService.getRoleIdByMenuId(menuId);
    }

    @Override
    public List<RoleVO> getRoleList(RoleListQueryDTO dto) {
        return RoleConvert.INSTANCE.convertList(roleMapper.selectCustomList(dto));
    }

    @Override
    public RoleVO getDetail(Long id) {
        return RoleConvert.INSTANCE.convertVO(roleMapper.selectById(id));
    }

    @Override
    public List<RoleTypeTreeVO> getMenuRoleTree(Long menuId) {
        // 获取当前菜单的角色Id列表
        List<RoleRelateMenu> roleRelateMenuList = roleRelateMenuMapper.selectByMenuId(menuId);
        Set<Long> roleIds = roleRelateMenuList.stream().map(RoleRelateMenu::getRoleId).collect(Collectors.toSet());
        if (CollUtil.isEmpty(roleIds)){
            return Collections.emptyList();
        }
        List<Role> roleList = roleMapper.selectBatchIds(roleIds);
        if (CollUtil.isEmpty(roleList)) {
            return Collections.emptyList();
        }
        Map<Long, List<Role>> roleMap = roleList.stream().collect(Collectors.groupingBy(Role::getRoleTypeId));
        List<RoleTypeTreeVO> roleTypes = roleMapper.selectTypeTreeList();
        Map<Long, RoleTypeTreeVO> roleTypeMap = CollectionUtils.convertMap(roleTypes, RoleTypeTreeVO::getId);
        List<RoleTypeTreeVO> roleTypeContainsRoleList = new ArrayList<>();
        for (Long roleTypeId : roleMap.keySet()) {
            if (!roleTypeMap.containsKey(roleTypeId)){
                continue;
            }
            RoleTypeTreeVO roleTypeTreeVO = roleTypeMap.get(roleTypeId);
            roleTypeTreeVO.setRoleList(RoleConvert.INSTANCE.convert2RoleTypeVOList(roleMap.get(roleTypeId)));
            roleTypeContainsRoleList.add(roleTypeTreeVO);
        }
        if (CollectionUtil.isEmpty(roleTypeContainsRoleList)){
            return Collections.emptyList();
        }
        // 寻找含有子角色的角色分类的所有祖先分类
        List<RoleTypeTreeVO> haveRoleTypeList = findRoleTypeTree(roleTypeContainsRoleList, roleTypes, roleTypeMap);
        // 进行去重操作
        if (CollUtil.isEmpty(haveRoleTypeList)){
            return Collections.emptyList();
        }
        List<RoleTypeTreeVO> distinctRoleTypeList = new ArrayList<>();
        Set<Long> idSet = new HashSet<>();
        for (RoleTypeTreeVO roleTypeTreeVO : haveRoleTypeList) {
            if (idSet.contains(roleTypeTreeVO.getId())){
                continue;
            }
            idSet.add(roleTypeTreeVO.getId());
            distinctRoleTypeList.add(roleTypeTreeVO);
        }
        return TreeUtil.buildTree(distinctRoleTypeList, true);
    }

    @Override
    public List<Long> getRoleListByMenuId(Long curMenu) {
        List<RoleRelateMenu> roleRelateMenuList = roleRelateMenuMapper.selectByMenuId(curMenu);
        if (CollUtil.isEmpty(roleRelateMenuList)){
            return new ArrayList<>();
        }
        return CollectionUtils.convertList(roleRelateMenuList, RoleRelateMenu::getRoleId);
    }

    @Override
    public Set<Long> selectRoleByMenuIdList(List<Long> menuIdList) {
        if (CollUtil.isEmpty(menuIdList)){
            return new HashSet<>();
        }
        List<RoleRelateMenu> roleRelateMenuList = roleRelateMenuMapper.selectByMenuIdList(menuIdList);
        if (CollUtil.isEmpty(roleRelateMenuList)){
            return new HashSet<>();
        }
        return CollectionUtils.convertSet(roleRelateMenuList, RoleRelateMenu::getRoleId);
    }

    @Override
    public List<FeignUserVO> authUserList(String authCode) {
        // 根据menuCode查询菜单信息
        Menu menu = menuMapper.selectByCode(authCode);
        if (Objects.isNull(menu)){
            return Collections.emptyList();
        }
        // 根据权限code查询对应的角色
        List<RoleRelateMenu> roleRelateMenuList = roleRelateMenuMapper.selectByMenuId(menu.getId());
        if (CollUtil.isEmpty(roleRelateMenuList)){
            return Collections.emptyList();
        }
        // 根据角色id查询角色下的用户
        List<Long> roleIds = CollectionUtils.convertList(roleRelateMenuList, RoleRelateMenu::getRoleId);
        List<UserRelateRole> userRelateRoles = userRelateRoleMapper.selectByRoleIdList(roleIds);
        if (CollUtil.isEmpty(userRelateRoles)){
            return Collections.emptyList();
        }
        List<String> userIdList = CollectionUtils.convertList(userRelateRoles, UserRelateRole::getUserId);
        List<User> userList = userMapper.selectByUserIds(userIdList);
        if (CollUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        return RoleConvert.INSTANCE.convert2FeignUserVO(userList);
    }

    @Override
    public List<Role> getByIds(List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)){
            return new ArrayList<>();
        }
        return roleMapper.selectBatchIds(roleIdList);
    }

    @Override
    public List<RoleTreeNodeVO> getDeptRoleTree(Long deptId) {
        List<DeptRole> deptRoles = deptService.selectByDeptId(deptId);
        if (CollUtil.isEmpty(deptRoles)){
            return new ArrayList<>();
        }
        List<Long> roleIds = CollectionUtils.convertList(deptRoles, DeptRole::getRoleId);
        List<RoleTreeNodeVO> treeNodeVOS = this.getAggregateTree();
        return findTargetRoleTreeNode(roleIds, treeNodeVOS);
    }

    private List<RoleTreeNodeVO> findTargetRoleTreeNode(List<Long> roleIds, List<RoleTreeNodeVO> treeNodeVOS) {
        List<RoleTreeNodeVO> res = new ArrayList<>();
        Map<Long, Integer> targetRoleMap = new HashMap<>();
        Integer i = 0;
        for (Long roleId : roleIds) {
            // 需要进行深拷贝
            List<RoleTreeNodeVO> copyTreeNodeVOs = JSON.parseArray(JSON.toJSONString(treeNodeVOS), RoleTreeNodeVO.class);
            RoleTreeNodeVO roleTreeNodeVO = helpFind(roleId, copyTreeNodeVOs);
            if (Objects.isNull(roleTreeNodeVO)){
                continue;
            }
            if (targetRoleMap.containsKey(roleTreeNodeVO.getId())){
                // 代表有共同的祖先，进行合并
               RoleTreeNodeVO targetRoleTypeVO = res.get(targetRoleMap.get(roleTreeNodeVO.getId()));
               mergeRole(targetRoleTypeVO, roleTreeNodeVO);
               continue;
            }
            res.add(roleTreeNodeVO);
            targetRoleMap.put(roleTreeNodeVO.getId(), i);
            i++;
        }
        return TreeUtil.buildTree(res, true);
    }

    /**
     * roleId的直系节点与其他roleId的直系节点进行合并
     * @param targetRoleTypeVO
     * @param roleTreeNodeVO
     */
    private void mergeRole(RoleTreeNodeVO targetRoleTypeVO, RoleTreeNodeVO roleTreeNodeVO) {
        List<RoleTreeNodeVO> targetChildren = targetRoleTypeVO.getChildren();
        List<RoleTreeNodeVO> curRoleChildren = roleTreeNodeVO.getChildren();
        if (CollUtil.isEmpty(curRoleChildren)){
            return ;
        }
        if (CollUtil.isEmpty(targetChildren)){
            targetRoleTypeVO.setChildren(curRoleChildren);
            return ;
        }
        for (RoleTreeNodeVO targetChild : targetChildren) {
            if (targetChild.getId().equals(curRoleChildren.get(0).getId())){
                mergeRole(targetChild, curRoleChildren.get(0));
                return ;
            }
        }
        targetRoleTypeVO.getChildren().addAll(curRoleChildren);
        targetRoleTypeVO.getChildren().sort(Comparator.comparing(RoleTreeNodeVO::sort).reversed());
    }

    private RoleTreeNodeVO helpFind(Long roleId, List<RoleTreeNodeVO> treeNodeVOS) {
        if (CollUtil.isEmpty(treeNodeVOS)){
            return null;
        }
        for (RoleTreeNodeVO treeNodeVO : treeNodeVOS) {
            if (treeNodeVO.isRoleTypeFlag()){
                RoleTreeNodeVO roleTreeNodeVO = helpFind(roleId, treeNodeVO.getChildren());
                if (Objects.nonNull(roleTreeNodeVO)){
                    treeNodeVO.setChildren(Lists.newArrayList(roleTreeNodeVO));
                    return treeNodeVO;
                }
            }
            if (roleId.equals(treeNodeVO.getId())){
                return treeNodeVO;
            }
        }
        return null;
    }

    @Override
    public List<FeignRoleVO> getListByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)){
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(roleMapper.getListByIds(ids),FeignRoleVO.class);
    }

    private List<RoleTreeNodeVO> buildTree(List<Role> roles) {
        Set<Long> roleTypeIds = CollectionUtils.convertSet(roles, Role::getRoleTypeId);
        if (CollUtil.isEmpty(roleTypeIds)) {
            return Collections.emptyList();
        }
        List<RoleType> roleTypes = roleTypeMapper.selectList();
        Map<Long, RoleType> roleTypeMap = CollectionUtils.convertMap(roleTypes, RoleType::getId);
        Set<Long> keys = new HashSet<>();
        roleTypeIds.forEach(key -> {
            keys.add(key);
            RoleType typeTreeItemVO = roleTypeMap.get(key);
            while (ObjectUtil.isNotNull(typeTreeItemVO)) {
                keys.add(typeTreeItemVO.getParentId());
                typeTreeItemVO = roleTypeMap.get(typeTreeItemVO.getParentId());
            }
        });
        List<RoleTreeNodeVO> nodes = RoleConvert.INSTANCE.convertTreeNode(roles, roleTypes);
        nodes = nodes.stream().filter(e -> keys.contains(e.getId()) || !e.isRoleTypeFlag()).collect(Collectors.toList());
        return TreeUtil.buildTree(nodes, true);
    }

    /**
     * 寻找含有子角色的角色分类的所有祖先分类
     * @param roleTypeContainsRoleList
     * @param roleTypes
     * @return
     */
    private List<RoleTypeTreeVO> findRoleTypeTree(List<RoleTypeTreeVO> roleTypeContainsRoleList, List<RoleTypeTreeVO> roleTypes,  Map<Long, RoleTypeTreeVO> roleTypeMap) {
        List<RoleTypeTreeVO> res = new ArrayList<>();
        Map<Long, RoleTypeTreeVO> parentMap = new HashMap<>();
        for (RoleTypeTreeVO roleType : roleTypes) {
            parentMap.put(roleType.getId(), roleTypeMap.get(roleType.getParentId()));
        }
        res.addAll(roleTypeContainsRoleList);
        for (RoleTypeTreeVO roleTypeTreeVO : roleTypeContainsRoleList) {
            dfs(roleTypeTreeVO, parentMap, res);
        }
        return res;
    }

    private void dfs(RoleTypeTreeVO roleTypeTreeVO, Map<Long, RoleTypeTreeVO> parentMap, List<RoleTypeTreeVO> res) {
        if (parentMap.get(roleTypeTreeVO.getId()) == null){
            return ;
        }
        res.add(parentMap.get(roleTypeTreeVO.getId()));
        dfs(parentMap.get(roleTypeTreeVO.getId()), parentMap, res);
    }

    /**
     * 获取roleId在当前登陆人的授权菜单下的菜单权限
     * @param roleId
     * @return
     */
    private Set<Long> getLoginRoleMenuIds(Long roleId, Boolean containsFunc) {
        List<Menu> loginMenuList = getLoginUserAuthMenu(containsFunc);
        if (CollUtil.isEmpty(loginMenuList)){
            return Collections.emptySet();
        }
        // 获取当前角色id是否拥有登陆人拥有的授权菜单
        Set<Long> loginMenuIds = CollectionUtils.convertSet(loginMenuList, Menu::getId);
        List<RoleRelateMenu> roleRelateMenuList  = roleMenuRelationService.selectByRoleIdAndMenuIds(roleId, loginMenuIds);
        if (CollUtil.isEmpty(roleRelateMenuList)){
            return Collections.emptySet();
        }
        return CollectionUtils.convertSet(roleRelateMenuList, RoleRelateMenu::getMenuId);
    }

    private List<Long> findTargetChild(Long roleTypeId, List<RoleTypeTreeVO> roleTypeTreeList) {
        List<Long> res = new ArrayList<>();
        res.add(roleTypeId);
        if (CollUtil.isEmpty(roleTypeTreeList)){
            return res;
        }
        for (RoleTypeTreeVO roleTypeTreeVO : roleTypeTreeList) {
            if (roleTypeId.equals(roleTypeTreeVO.getId())){
                helpFindAllChild(roleTypeTreeVO.getChildren(), res);
            }
        }
        return res;
    }

    private void helpFindAllChild(List<RoleTypeTreeVO> children, List<Long> res) {
        if (CollUtil.isEmpty(children)){
            return;
        }
        children.forEach(item -> {
            res.add(item.getId());
            helpFindAllChild(item.getChildren(), res);
        });
    }

}
