package com.bmos.platform.service.system.menu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.platform.facade.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.menu.convert.MenuConvert;
import com.bmos.platform.service.system.menu.dto.CurrentMenuTreeQueryDTO;
import com.bmos.platform.service.system.menu.dto.MenuSaveDTO;
import com.bmos.platform.service.system.menu.dto.MenuUpdateDTO;
import com.bmos.platform.service.system.menu.mapper.MenuMapper;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.menu.service.MenuService;
import com.bmos.platform.service.system.menu.vo.FunctionVO;
import com.bmos.platform.service.system.menu.vo.MenuListVO;
import com.bmos.platform.service.system.menu.vo.MenuTreeVO;
import com.bmos.platform.service.system.role.convert.RoleConvert;
import com.bmos.platform.service.system.role.dto.RoleRelateMenuSaveItemDTO;
import com.bmos.platform.service.system.role.mapper.RoleMapper;
import com.bmos.platform.service.system.role.mapper.RoleTypeMapper;
import com.bmos.platform.service.system.role.model.AuthRoleMenu;
import com.bmos.platform.service.system.role.model.Role;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import com.bmos.platform.service.system.role.model.RoleType;
import com.bmos.platform.service.system.role.service.AuthRoleMenuService;
import com.bmos.platform.service.system.role.service.RoleMenuRelationService;
import com.bmos.platform.service.system.role.service.RoleService;
import com.bmos.platform.service.system.role.vo.RoleTreeNodeVO;
import com.bmos.platform.service.system.user.mapper.UserRelateRoleMapper;
import com.bmos.platform.service.system.user.model.UserRelateRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.bmos.platform.service.system.menu.constant.MenuConstant.MAX_ONE_LEVEL_MENU_ID;
import static com.bmos.platform.service.system.menu.constant.MenuConstant.MAX_ROOT_ID;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleTypeMapper roleTypeMapper;

    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    @Autowired
    UserRelateRoleMapper userRelateRoleMapper;

    @Autowired
    RoleService roleService;

    @Override
    public List<MenuTreeVO> treeAll(String rootMenuCode) {
        //todo 校验是否是 admin
        List<MenuTreeVO> menuList = menuMapper.selectAllMenu(rootMenuCode);
        // 进行国际化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        menuList = menuList.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        Set<Long> ids = CollectionUtils.convertSet(menuList, MenuTreeVO::getId);
        return TreeUtil.buildTree(menuList, e -> !ids.contains(e.getParentId()), false);
    }

    @Override
    public List<FunctionVO> getFunction(Long menuId, Long roleId) {
        // 若所选菜单为一级菜单 则返回空列表
        if (menuId > MAX_ROOT_ID && menuId < MAX_ONE_LEVEL_MENU_ID){
            return new ArrayList<>();
        }
        // 查看当前登陆人的授权菜单
        List<FunctionVO> functionVOS = menuMapper.selectFunction(menuId, roleId, 0);
        if (CollUtil.isEmpty(functionVOS)){
            return new ArrayList<>();
        }
        // 查询当前菜单所属的功能
        SysUser user = SysUserHolder.getUser();
        // 超级管理员可以查看所有功能
        if (AdminUtil.isAdminUser(user.getUserId())){
            // 查询roleId在menuId下的所有菜单权限
            Set<Long> parentIds = functionVOS.stream().map(FunctionVO::getId).collect(Collectors.toSet());
            return TreeUtil.buildTree(functionVOS, e -> !parentIds.contains(e.getParentId()), false);
        }
        return getAuthFunction(user, functionVOS);
    }

    private List<FunctionVO> getAuthFunction(SysUser user, List<FunctionVO> functionVOS) {
        // 查询当前用户所属的角色
        List<UserRelateRole> userRelateRoles = userRelateRoleMapper.selectByUserId(user.getUserId());
        if (CollUtil.isEmpty(userRelateRoles)){
            // 当前登录用户不属于任何角色 则没有菜单授权功能
            return new ArrayList<>();
        }
        List<Long> roleIdList = userRelateRoles.stream().map(UserRelateRole::getRoleId).collect(Collectors.toList());
        List<Long> functionMenuIdList = functionVOS.stream().map(FunctionVO::getId).collect(Collectors.toList());
        List<AuthRoleMenu> authRoleMenus = authRoleMenuService.selectByRoleMenuIdList(functionMenuIdList, roleIdList);
        if (CollUtil.isEmpty(authRoleMenus)){
            // 当前登陆人所属角色没有菜单权限
            return new ArrayList<>();
        }
        Set<Long> authMenuIds = authRoleMenus.stream().map(AuthRoleMenu::getMenuId).collect(Collectors.toSet());
        // 剔除没有当前权限菜单的功能菜单
        List<FunctionVO> collect = functionVOS.stream().filter(e -> authMenuIds.contains(e.getId())).collect(Collectors.toList());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        collect = collect.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        Set<Long> parentIds = collect.stream().map(FunctionVO::getId).collect(Collectors.toSet());
        return TreeUtil.buildTree(collect, e -> !parentIds.contains(e.getParentId()), false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MenuSaveDTO dto) {
        menuMapper.insert(MenuConvert.INSTANCE.convert(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuUpdateDTO dto) {
        menuMapper.updateById(MenuConvert.INSTANCE.convert(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        menuMapper.deleteById(id);
    }

    @Override
    public List<Long> relateRoleData(Long menuId) {
        return roleMapper.selectRoleByMenuId(menuId);
    }

    @Override
    public List<MenuListVO> getAdminMenuRootList() {
        //todo 判断是否为系统 管理员
        List<MenuListVO> convert = MenuConvert.INSTANCE.convert(menuMapper.selectRootMenuList());
        // 国际化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        convert = convert.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        return convert;
    }

    @Override
    public List<MenuListVO> getMenuRootList() {
        return MenuConvert.INSTANCE.convertList(authRoleMenuService.getRootMenuListByUserId(SysUserHolder.getUser().getUserId(), MAX_ROOT_ID));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "saveMenuRole")
    public void saveMenuRole(RoleRelateMenuSaveItemDTO dto) {
        Long curMenu = CollUtil.getFirst(dto.getMenuIds());
        // 一级菜单
        if (CollUtil.isNotEmpty(dto.getDeletedRoleIds())){
            roleMenuRelationService.deleteByMenuIdAndRoleIds(curMenu, dto.getDeletedRoleIds());
        }
        List<RoleRelateMenu> roleRelateMenuList = new ArrayList<>();
        if (dto.getIsMenu()){
            Long parentMenuId = CollUtil.getLast(dto.getMenuIds());
            // 级联删除菜单
            cascadeDeleteMenuRole(parentMenuId, dto.getDeletedRoleIds());
            // 级联添加菜单权限
            cascadeAddMenuRole(roleRelateMenuList, parentMenuId, dto.getRoleIds());
        }
        // 当前菜单具有的角色权限
        List<Long> menuRoleList = roleService.getRoleListByMenuId(curMenu);
        // 校验哪些角色是新增的 剔除原就具有的权限的角色
        Set<Long> menuRoleIdSet = new HashSet<>(menuRoleList);
        List<Long> needAddMenuRoleList = dto.getRoleIds().stream().filter(e -> !menuRoleIdSet.contains(e)).collect(Collectors.toList());
        // 进行权限新增
        roleRelateMenuList.addAll(MenuConvert.INSTANCE.convert(curMenu, needAddMenuRoleList));
        List<RoleRelateMenu> insert = roleRelateMenuList.stream().distinct().collect(Collectors.toList());
        roleMenuRelationService.saveBatch(insert);
    }

    @Override
    public List<MenuTreeVO> getCurrentMenuTree(CurrentMenuTreeQueryDTO dto) {
        String userId = SysUserHolder.getUser().getUserId();
        List<Menu> list;
        if (AdminUtil.isAdminUser(userId)) {
            list = menuMapper.selectMenuAdminList(dto, MAX_ROOT_ID);
        } else {
            list = menuMapper.selectMenuList(userId, dto, MAX_ROOT_ID);
        }
        return buildTree(dto.getRootMenuCode(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "saveAuthMenuRole")
    public void saveAuthMenuRole(RoleRelateMenuSaveItemDTO dto) {
        // 需要修改的菜单/功能
        Long curMenu = CollUtil.getFirst(dto.getMenuIds());
        // 一级菜单
        if (CollUtil.isNotEmpty(dto.getDeletedRoleIds())){
            authRoleMenuService.deleteByMenuIdAndRoleIds(curMenu, dto.getDeletedRoleIds());
        }
        List<AuthRoleMenu> authRoleMenus = new ArrayList<>();
        if (dto.getIsMenu()){
            Long parentMenuId = CollUtil.getLast(dto.getMenuIds());
            // 级联删除菜单
            cascadeDeleteAuthRoleMenu(parentMenuId, dto.getDeletedRoleIds());
            // 级联添加菜单授权
            cascadeAddAuthRoleMenu(authRoleMenus, parentMenuId, dto.getRoleIds());
        }
        // 当前菜单具有的角色权限
        List<Long> authRoleList = roleService.getAuthRoleList(curMenu);
        // 校验哪些角色是新增的 剔除原就具有的权限的角色
        Set<Long> authRoleSet = new HashSet<>(authRoleList);
        List<Long> needAddAuthRoleList = dto.getRoleIds().stream().filter(e -> !authRoleSet.contains(e)).collect(Collectors.toList());
        // 进行权限新增
        authRoleMenus.addAll(MenuConvert.INSTANCE.convertAuthRoleMenu(curMenu, needAddAuthRoleList));
        List<AuthRoleMenu> insert = authRoleMenus.stream().distinct().collect(Collectors.toList());
        authRoleMenuService.saveBatch(insert);
    }

    @Override
    public List<MenuTreeVO> getAuthMenuTree(String rootMenuCode, Boolean containsFunc) {
        String userId = SysUserHolder.getUser().getUserId();
        List<Menu> list;
        if (AdminUtil.isAdminUser(userId)) {
            CurrentMenuTreeQueryDTO dto = new CurrentMenuTreeQueryDTO();
            dto.setRootMenuCode(rootMenuCode);
            dto.setContainsFunc(ObjectUtil.isNotNull(containsFunc) ? containsFunc : true);
            list = menuMapper.selectMenuAdminList(dto, MAX_ROOT_ID);
        } else {
            // 查询当前用户能够授权的所有菜单
            list = authRoleMenuService.getMenuListByUserId(userId, rootMenuCode, containsFunc);
        }
        List<MenuTreeVO> menuTreeVOS = buildTree(rootMenuCode, list);
        // 进行国际化处理
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        menuTreeVOS = menuTreeVOS.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        // 剔除非rootMenuCode的菜单
        return StrUtil.isEmpty(rootMenuCode) ? menuTreeVOS : menuTreeVOS.stream().filter(e -> e.getId().equals(Long.valueOf(rootMenuCode))).collect(Collectors.toList());
    }

    @Override
    public List<MenuTreeVO> getOperationMenuAll() {
        List<MenuTreeVO> menuList = menuMapper.selectAllMenuFilterPermission();
        // 操作日志菜单国际化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        menuList = menuList.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        Set<Long> ids = CollectionUtils.convertSet(menuList, MenuTreeVO::getId);
        return TreeUtil.buildTree(menuList, e -> !ids.contains(e.getParentId()), false);
    }

    @Override
    public List<RoleTreeNodeVO> getMenuRoleTree(Long menuId) {
        List<RoleRelateMenu> roleRelateMenus = roleMenuRelationService.getByMenuId(menuId);
        if(CollUtil.isEmpty(roleRelateMenus)){
            return Collections.emptyList();
        }
        List<Role> roles = roleMapper.selectBatchIds(CollectionUtils.convertList(roleRelateMenus, RoleRelateMenu::getRoleId));
        return buildRoleTree(roles);
    }

    @Override
    public List<Menu> getByCodes(Collection<String> menuCodeSet) {
        return menuMapper.getByCodes(menuCodeSet);
    }

    @Override
    public List<MenuVO> getAllChildMenuIdList(Long menuId) {
        List<Menu> menus = menuMapper.selectList();
        List<MenuTreeVO> menuTreeVOS = MenuConvert.INSTANCE.convert2MenuTreeVO(menus);
        List<MenuTreeVO> treeVOS = TreeUtil.buildTree(menuTreeVOS, true);
        // 寻找目标menuId
        MenuTreeVO menuTreeVO = findTargetMenuId(menuId, treeVOS);
        if (Objects.isNull(menuTreeVO)){
            return new ArrayList<>();
        }
        return MenuConvert.INSTANCE.convert2MenuVO(menuTreeVO.getChildren());
    }

    @Override
    public List<FunctionVO> getRoleAuthFunction(Long menuId, Long roleId) {
        // 获取当前菜单下的所有的功能列表
        if (menuId > MAX_ROOT_ID && menuId < MAX_ONE_LEVEL_MENU_ID){
            return new ArrayList<>();
        }
        // 菜单id下所有的功能列表
        List<FunctionVO> functionVOS = menuMapper.selectAuthFunction(menuId, roleId, 0);
        // 进行国际化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        functionVOS = functionVOS.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        if (CollUtil.isEmpty(functionVOS)){
            return new ArrayList<>();
        }
        Set<Long> parentIds = functionVOS.stream().map(FunctionVO::getId).collect(Collectors.toSet());
        return TreeUtil.buildTree(functionVOS, e -> !parentIds.contains(e.getParentId()), false);
    }

    @Override
    public List<MenuTreeVO> getMenuTree() {
        List<Menu> menuList = menuMapper.selectAllMenuList(Boolean.FALSE);
        List<MenuTreeVO> menuTreeVOS = MenuConvert.INSTANCE.convert2MenuTreeVO(menuList);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        menuTreeVOS = menuTreeVOS.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        return TreeUtil.buildTree(menuTreeVOS, false);
    }

    @Override
    public List<MenuTreeVO> getCurrentAllMenu(CurrentMenuTreeQueryDTO dto) {
        String userId = SysUserHolder.getUser().getUserId();
        List<Menu> list;
        if (AdminUtil.isAdminUser(userId)) {
            list = menuMapper.selectMenuAdminList(dto, MAX_ROOT_ID);
        } else {
            list = menuMapper.selectMenuList(userId, dto, MAX_ROOT_ID);
        }
        List<MenuTreeVO> menuTreeVOS = MenuConvert.INSTANCE.convert2VO(list);
        // 所有菜单名称进行国际化
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        menuTreeVOS = menuTreeVOS.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        return menuTreeVOS;
    }

    private MenuTreeVO findTargetMenuId(Long menuId, List<MenuTreeVO> treeVOS) {
        MenuTreeVO res = null;
        for (MenuTreeVO treeVO : treeVOS) {
            if (treeVO.getId().equals(menuId)) {
                return treeVO;
            }
            if (CollUtil.isEmpty(treeVO.getChildren())){
                continue;
            }
            if (Objects.nonNull(res = findTargetMenuId(menuId, treeVO.getChildren()))){
                return res;
            }
        }
        return res;
    }

    private List<RoleTreeNodeVO> buildRoleTree(List<Role> roles) {
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

    private List<MenuTreeVO> buildTree(String rootMenuCode, List<Menu> list) {
        List<MenuTreeVO> treeNodeList = MenuConvert.INSTANCE.convertTreeNode(list);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        treeNodeList = treeNodeList.stream().peek(e -> e.setName(I18nUtils.getMenuMessage(e.getId().toString(), e.getName(), null, request))).collect(Collectors.toList());
        if (StrUtil.isEmpty(rootMenuCode)) {
            Set<Long> parentIds = CollectionUtils.convertSet(list, Menu::getId);
            return TreeUtil.buildTree(treeNodeList, e -> !parentIds.contains(e.getParentId()), false);
        }
        return TreeUtil.buildTree(treeNodeList, e -> e.getId().equals(Long.valueOf(rootMenuCode)), false);
    }

    /**
     * 级联添加菜单权限
     * @param roleRelateMenuList
     * @param menuId
     * @param roleIds
     */
    private void cascadeAddMenuRole( List<RoleRelateMenu> roleRelateMenuList, Long menuId, List<Long> roleIds) {
        Set<Long> menuRoleIdSet = roleService.selectRoleByMenuIdList(Collections.singletonList(menuId));
        List<Long> needAddMenuRoleList = roleIds.stream().filter(e -> !menuRoleIdSet.contains(e)).collect(Collectors.toList());
        if (CollUtil.isEmpty(needAddMenuRoleList)){
            return ;
        }
        roleRelateMenuList.addAll(MenuConvert.INSTANCE.convert(menuId, needAddMenuRoleList));
        if (menuId < MAX_ROOT_ID){
            return ;
        }
        String menuIdStr = String.valueOf(menuId);
        Long parentMenuId = Long.valueOf(StrUtil.sub(menuIdStr, 0, menuIdStr.length() - 3));
        cascadeAddMenuRole(roleRelateMenuList, parentMenuId, needAddMenuRoleList);
    }

    /**
     * 级联删除角色的菜单权限
     * @param menuId
     * @param deletedRoleIds
     */
    private void cascadeDeleteMenuRole(Long menuId, List<Long> deletedRoleIds) {
        // 查询parentMenu下的所有子菜单包含的角色
        List<Menu> childMenuList = this.selectByParentId(menuId);
        List<Long> chileMenuIdList = childMenuList.stream().map(Menu::getId).collect(Collectors.toList());
        Set<Long> childMenuRoleIdSet = roleService.selectRoleByMenuIdList(chileMenuIdList);
        List<Long> needDeleteParentMenuIdRoleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(deletedRoleIds)){
            // 若是修改的为菜单则判断当前parentMenu下的菜单是否还有deleteRoleIds角色的权限
            needDeleteParentMenuIdRoleIdList = deletedRoleIds.stream().filter(e -> !childMenuRoleIdSet.contains(e)).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(needDeleteParentMenuIdRoleIdList)){
            roleMenuRelationService.deleteByMenuIdAndRoleIds(menuId, needDeleteParentMenuIdRoleIdList);
            if (menuId < MAX_ROOT_ID){
                // 代表当前菜单为根节点菜单
                return ;
            }
            String menuStr = String.valueOf(menuId);
            Long parentMenuId = Long.valueOf(StrUtil.sub(menuStr, 0, menuStr.length() - 3));
            cascadeDeleteMenuRole(parentMenuId, needDeleteParentMenuIdRoleIdList);
        }
    }

    /**
     * 级联为roleIds添加菜单授权
     * @param authRoleMenus
     * @param menuId
     * @param roleIds
     */
    private void cascadeAddAuthRoleMenu(List<AuthRoleMenu> authRoleMenus, Long menuId, List<Long> roleIds) {
        List<Long> authMenuRoleIdList = authRoleMenuService.getRoleIdByMenuId(menuId);
        List<Long> needAddAuthMenuRoleList = roleIds.stream().filter(e -> !new HashSet<>(authMenuRoleIdList).contains(e)).collect(Collectors.toList());
        if (CollUtil.isEmpty(needAddAuthMenuRoleList)){
            return ;
        }
        authRoleMenus.addAll(MenuConvert.INSTANCE.convertAuthRoleMenu(menuId, needAddAuthMenuRoleList));
        if (menuId <= MAX_ROOT_ID){
            return ;
        }
        String menuIdStr = String.valueOf(menuId);
        Long parentMenuId = Long.valueOf(StrUtil.sub(menuIdStr, 0, menuIdStr.length() - 3));
        cascadeAddAuthRoleMenu(authRoleMenus, parentMenuId, needAddAuthMenuRoleList);
    }

    /**
     * 级联删除角色的菜单授权
     * @param menuId
     * @param deletedRoleIds
     */
    private void cascadeDeleteAuthRoleMenu(Long menuId, List<Long> deletedRoleIds) {
        // 查询parentMenu下的所有子菜单包含的角色
        List<Menu> childMenuList = this.selectByParentId(menuId);
        List<Long> chileMenuIdList = childMenuList.stream().map(Menu::getId).collect(Collectors.toList());
        Set<Long> childMenuRoleIdSet = authRoleMenuService.selectRoleByMenuIdList(chileMenuIdList);
        List<Long> needDeleteParentMenuIdRoleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(deletedRoleIds)){
            // 若是修改的为菜单则判断当前parentMenu下的菜单是否还有deleteRoleIds角色的权限
            needDeleteParentMenuIdRoleIdList = deletedRoleIds.stream().filter(e -> !childMenuRoleIdSet.contains(e)).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(needDeleteParentMenuIdRoleIdList)){
            authRoleMenuService.deleteByMenuIdAndRoleIds(menuId, needDeleteParentMenuIdRoleIdList);
            if (menuId < MAX_ROOT_ID){
                // 代表当前菜单为根节点菜单
                return ;
            }
            String menuStr = String.valueOf(menuId);
            Long parentMenuId = Long.valueOf(StrUtil.sub(menuStr, 0, menuStr.length() - 3));
            cascadeDeleteAuthRoleMenu(parentMenuId, needDeleteParentMenuIdRoleIdList);
        }
    }

    private List<Menu> selectByParentId(Long menuId) {
        List<Menu> menus = menuMapper.selectByParentId(menuId);
        if (CollUtil.isEmpty(menus)){
            return new ArrayList<>();
        }
        return menus;
    }

}
