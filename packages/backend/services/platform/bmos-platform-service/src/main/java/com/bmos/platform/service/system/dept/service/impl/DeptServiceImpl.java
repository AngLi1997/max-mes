package com.bmos.platform.service.system.dept.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.system.dept.vo.*;
import com.bmos.platform.service.system.dept.constant.DeptConstant;
import com.bmos.platform.service.system.dept.convert.DeptConvert;
import com.bmos.platform.service.system.dept.convert.DeptRelateUserConvert;
import com.bmos.platform.service.system.dept.dto.*;
import com.bmos.platform.service.system.dept.mapper.DeptMapper;
import com.bmos.platform.service.system.dept.mapper.DeptRelateUserMapper;
import com.bmos.platform.service.system.dept.mapper.DeptRoleMapper;
import com.bmos.platform.service.system.dept.model.Dept;
import com.bmos.platform.service.system.dept.model.DeptRelateUser;
import com.bmos.platform.service.system.dept.model.DeptRole;
import com.bmos.platform.service.system.dept.service.DeptService;
import com.bmos.platform.service.system.dept.vo.DeptAssignUserVO;
import com.bmos.platform.service.system.dept.vo.DeptUnAssignUserVO;
import com.bmos.platform.service.system.role.vo.RoleTreeNodeVO;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.model.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptRoleMapper deptRoleMapper;

    @Autowired
    private DeptRelateUserMapper deptRelateUserMapper;

    @Override
    public List<DeptTreeVO> treeAll() {
        List<DeptTreeVO> deptList = deptMapper.selectAllDept();
        if (CollUtil.isEmpty(deptList)) {
            return Collections.emptyList();
        }
        Map<Long, DeptTreeVO> map = CollectionUtils.convertMap(deptList, DeptTreeVO::getId);
        deptList.forEach(item -> {
            //给父级名称赋值
            DeptTreeVO parentDept = map.get(item.getParentId());
            if (ObjectUtil.isNotNull(parentDept)) {
                item.setParentName(parentDept.getName());
                item.setParentCode(parentDept.getCode());
            }
        });
        return TreeUtil.buildTree(deptList, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DeptSaveDTO dto) {
        Long maxDBId = deptMapper.selectMaxId();
        Long maxId = ObjectUtil.isNull(maxDBId) ? DeptConstant.MIN_ID : maxDBId;
        Dept dept = DeptConvert.INSTANCE.convert(dto);
        dept.setId(++maxId);
        dept.setCode(dto.getParentCode() + StrUtil.COMMA + maxId);
        deptMapper.insert(dept);
        // 保存角色与部门之间的绑定关系
        List<DeptRole> deptRoles = DeptConvert.INSTANCE.convert2DeptRoleList(dto.getRoleIds(),  dept.getId());
        if (CollUtil.isNotEmpty(deptRoles)){
            deptRoleMapper.insertBatch(deptRoles);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeptUpdateDTO dto) {
        Dept dept = deptMapper.selectById(dto.getId());
        if (ObjectUtil.isNull(dept)) {
            throw new BmosException(PlatformResponseCode.DEPT_NOT_EXIST);
        }
        dept.setDeptName(dto.getDeptName());
        dept.setRemark(dto.getRemark());
        deptMapper.updateById(dept);
        // 保存角色与部门之间的绑定关系
        deptRoleMapper.deleteByDeptId(dto.getId());
        List<DeptRole> deptRoles = DeptConvert.INSTANCE.convert2DeptRoleList(dto.getRoleIds(),  dept.getId());
        if (CollUtil.isNotEmpty(deptRoles)){
            deptRoleMapper.insertBatch(deptRoles);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (deptMapper.existsChildDept(id)) {
            throw new BmosException(PlatformResponseCode.DEPT_EXIST_CHILDREN);
        }
        if (deptRelateUserMapper.existsUser(id)) {
            throw new BmosException(PlatformResponseCode.DEPT_EXIST_USER);
        }
        // 删除角色与部门之间的绑定关系
        deptRoleMapper.deleteByDeptId(id);
        deptMapper.deleteById(id);
    }

    @Override
    public Boolean validateDept(String deptName, Long id) {
        return CollUtil.isNotEmpty(deptMapper.validateDept(deptName, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relateUserSave(List<DeptRelateUserSaveDTO> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            return;
        }
        deptRelateUserMapper.insertBatch(DeptRelateUserConvert.INSTANCE.convert(dtoList));
    }

    @Override
    public CommonPage<DeptAssignUserVO> relateUserData(DeptRelateUserQueryDTO dto) {
        return CommonPage.convertPage(deptMapper.selectAssignList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relateUserDel(Long id) {
        deptRelateUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relateUserDelAll(Long deptId) {
        deptRelateUserMapper.deleteBatchIds(deptRelateUserMapper.getListByDeptId(deptId));
    }

    @Override
    public List<DeptUnAssignUserVO> assignPerson(DeptAssignQueryDTO dto) {
        List<DeptUnAssignUserVO> list = deptMapper.selectUnAssignList(dto.getDeptId(), dto.getName());
        list.forEach(item -> item.setName(item.getUserName() + StrUtil.DASHED + item.getLoginName()));
        return list;
    }

    @Override
    public DeptTreeUserAllVO unassigned(String name) {
        List<DeptTreeUserVO> list = deptMapper.unAssigned(name);
        list.forEach(item -> item.setName(item.getUserName() + StrUtil.DASHED + item.getLoginName()));
        return DeptTreeUserAllVO.builder().list(list).size(deptMapper.getCount(DeptConstant.START_STATUS)).build();
    }

    @Override
    public List<DeptTreeUserVO> assigned(String name, Long deptId) {
        List<DeptTreeUserVO> list = deptMapper.assigned(name, deptId);
        list.forEach(item -> item.setName(item.getUserName() + StrUtil.DASHED + item.getLoginName()));
        return list;
    }

    @Override
    public List<Long> getDeptList() {
        String userId = SysUserHolder.getUser().getUserId();
        if (AdminUtil.isAdminUser(userId)){
            return CollectionUtils.convertList(deptMapper.selectList(),Dept::getId);
        }
        return deptMapper.getDeptList(userId);
    }

    @Override
    public List<DeptUserTreeVO> getDeptUserTree(String parentDeptCode) {
        List<Dept> deptList = deptMapper.selectListByDeptCode(parentDeptCode);
        if (CollUtil.isEmpty(deptList)) {
            return Collections.emptyList();
        }
        Map<Long, Dept> deptMap = CollectionUtils.convertMap(deptList, Dept::getId);
        Set<Long> deptIds = deptMap.keySet();
        List<DeptRelateUser> deptRelateUsers = deptRelateUserMapper.getByDeptIds(deptIds);
        Map<Long, Set<String>> deptUsers =
                CollectionUtils.convertMultiMap2(deptRelateUsers, DeptRelateUser::getDeptId, DeptRelateUser::getUserId);
        //查询关联的用户
        Map<String, User> userMap = findUserMap(deptRelateUsers);
        List<DeptUserTreeVO> treeNodes = DeptConvert.INSTANCE.convertTree(deptMap, deptList);
        //填充用户节点
        treeNodes.forEach(node -> fillChildrenUserNode(deptUsers, userMap, node));
        return TreeUtil.buildTree(treeNodes, e -> !deptIds.contains(Long.valueOf(e.getParentId())), false);
    }

    @Override
    public List<DeptUserTreeVO> getDeptUserTreeByUsers(List<User> users) {
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }
        Map<String, User> userMap = CollectionUtils.convertMap(users, User::getUserId);
        List<DeptRelateUser> deptRelateUsers = deptRelateUserMapper.getListByUserIds(userMap.keySet());
        Set<String> userWithDeptIds = CollectionUtils.convertSet(deptRelateUsers, DeptRelateUser::getUserId);
        //没有部门的用户
        List<User> userList = users.stream().filter(user -> !userWithDeptIds.contains(user.getUserId())).collect(Collectors.toList());
        Map<Long, Set<String>> deptUsers =
                CollectionUtils.convertMultiMap2(deptRelateUsers, DeptRelateUser::getDeptId, DeptRelateUser::getUserId);
        if (CollUtil.isEmpty(deptUsers)) {
            return DeptConvert.INSTANCE.convertDeptUserList(userList);
        }
        List<Dept> deptList = deptMapper.selectBatchIds(deptUsers.keySet());
        Map<Long, Dept> deptMap = CollectionUtils.convertMap(deptList, Dept::getId);
        List<DeptUserTreeVO> treeNodes = DeptConvert.INSTANCE.convertTree(deptMap, deptList);
        //填充用户节点
        Set<Long> ids = deptMap.keySet();
        treeNodes.forEach(node -> fillChildrenUserNode(deptUsers, userMap, node));
        List<DeptUserTreeVO> tree = TreeUtil.buildTree(treeNodes, e -> !ids.contains(Long.valueOf(e.getParentId())), false);
        //填充没有部门的用户的节点
        if (CollUtil.isNotEmpty(userList)) {
            tree.addAll(DeptConvert.INSTANCE.convertDeptUserList(userList));
        }
        return tree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUser(DeptUserRemoveDTO dto) {
        //todo 删除缓存
        deptRelateUserMapper.deleteByDeptAndUser(dto);
    }

    @Override
    public List<CommonTreeVO> getDeptTree() {
        List<CommonTreeVO> nodes = deptMapper.selectDeptCommonTree();
        return TreeUtil.buildTree(nodes, true);
    }

    @Override
    public List<CommonTreeVO> getPartitionTree() {
        String userId = SysUserHolder.getUser().getUserId();
        if (AdminUtil.isAdminUser(userId)) {
            return getDeptTree();
        }
        List<Dept> deptList = deptMapper.selectListByUserId(userId);
        if (CollUtil.isEmpty(deptList)) {
            return Collections.emptyList();
        }
        Set<String> deptCodes = deptList.stream().map(Dept::getCode).collect(Collectors.toSet());
        //所有上级
        Set<Long> ids = splitDeptCodes(deptList);
        List<Dept> allDeptList = deptMapper.selectList();
        List<Dept> deptNodes = allDeptList
                .stream()
                .filter(e -> ids.contains(e.getId()) || startsWith(e.getCode(), deptCodes))
                .collect(Collectors.toList());
        List<CommonTreeVO> nodes = DeptConvert.INSTANCE.convertTreeNode(deptNodes);
        return TreeUtil.buildTree(nodes, true);
    }

    @Override
    public List<Long> getMineDeptIds() {
        return deptRelateUserMapper.selectDeptIdsByUserId(SysUserHolder.getUser().getUserId());
    }

    @Override
    public List<Dept> getByIds(List<Long> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)){
            return new ArrayList<>();
        }
        return deptMapper.selectBatchIds(deptIdList);
    }

    @Override
    public List<DeptIntervalTreeVO> intervalTree() {
        // 获取当前登陆人所在部门
        SysUser user = SysUserHolder.getUser();
        if (AdminUtil.isAdminUser(user.getUserId())){
            return DeptConvert.INSTANCE.convert2DeptIntervalTreeVOList(this.treeAll());
        }
        List<DeptRelateUser> deptRelateUsers =  deptRelateUserMapper.getDeptIdByUserId(user.getUserId());
        if (CollUtil.isEmpty(deptRelateUsers)) {
            return new ArrayList<>();
        }
        List<Long> deptIdList = deptRelateUsers.stream().map(DeptRelateUser::getDeptId).collect(Collectors.toList());
        List<DeptTreeVO> deptTreeVOS = this.treeAll();
        Map<Long, List<DeptTreeVO>> deptChildMap = this.findDeptChildMap(deptTreeVOS);
        // 剔除包含关系
        // 将deptIdList中具有包含关系的数据全部剔除 此时deptIdList中的部门已经没有包含关系
        this.weedOutIncludeDeptId(deptIdList, deptChildMap);
        // 查询没有包含关系的部门的所有上级部门以及下级部门
        return this.findChainDept(deptIdList, deptTreeVOS);
    }

    @Override
    public List<DeptRole> selectByDeptId(Long deptId) {
        return deptRoleMapper.selectByDeptId(deptId);
    }

    @Override
    public List<Long> getDeptByUserId(String userId) {
        return deptMapper.getDeptList(userId);
    }

    @Override
    public List<Long> deptRole(Long id) {
        List<DeptRole> deptRoles = deptRoleMapper.selectByDeptId(id);
        if (CollUtil.isEmpty(deptRoles)){
            return new ArrayList<>();
        }
        return CollectionUtils.convertList(deptRoles, DeptRole::getRoleId);
    }

    @Override
    public void bindRole(DeptRoleBindDTO dto) {
        deptRoleMapper.deleteByDeptId(dto.getId());
        List<DeptRole> deptRoles = DeptConvert.INSTANCE.convert2DeptRoleList(dto.getRoleIdList(), dto.getId());
        if (CollUtil.isEmpty(deptRoles)){
            return ;
        }
        deptRoleMapper.insertBatch(deptRoles);
    }

    @Override
    public List<Long> getRoleDeptList(Long roleId) {
        List<DeptRole> deptRoles = deptRoleMapper.selectByRoleId(roleId);
        if (CollUtil.isEmpty(deptRoles)){
            return new ArrayList<>();
        }
        return CollectionUtils.convertList(deptRoles, DeptRole::getDeptId);
    }

    @Override
    public void roleBindDept(RoleDeptBindDTO dto) {
        deptRoleMapper.deleteByRoleId(dto.getId());
        List<DeptRole> deptRoles = DeptConvert.INSTANCE.convert2RoleDeptList(dto.getDeptIdList(), dto.getId());
        if (CollUtil.isEmpty(deptRoles)){
            return ;
        }
        deptRoleMapper.insertBatch(deptRoles);
    }

    private Boolean startsWith(String code, Set<String> set) {
        for (String ch : set) {
            if (code.startsWith(ch)) {
                return true;
            }
        }
        return false;
    }

    private Set<Long> splitDeptCodes(List<Dept> deptList) {
        return deptList.stream()
                .map(Dept::getCode)
                .map(e -> StrUtil.split(e, StrUtil.COMMA))
                .flatMap(Collection::stream)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    private Map<String, User> findUserMap(List<DeptRelateUser> deptRelateUsers) {
        Set<String> userIds = CollectionUtils.convertSet(deptRelateUsers, DeptRelateUser::getUserId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<User> users = userMapper.selectByUserIds(userIds);
        return CollectionUtils.convertMap(users, User::getUserId);
    }

    private void fillChildrenUserNode(Map<Long, Set<String>> deptUsers, Map<String, User> userMap, DeptUserTreeVO node) {
        Set<String> userIdList = deptUsers.get(Long.valueOf(node.getId()));
        if (CollUtil.isEmpty(userIdList)) {
            return;
        }
        node.setChildren(convertUserNodes(deptUsers, userMap, node));
    }

    private List<DeptUserTreeVO> convertUserNodes(Map<Long, Set<String>> deptUsers,
                                                  Map<String, User> userMap,
                                                  DeptUserTreeVO node) {
        return deptUsers.get(Long.valueOf(node.getId()))
                .stream()
                .map(userId -> {
                    User user = userMap.get(userId);
                    return DeptConvert.INSTANCE.convertDeptUser(node, user);
                }).distinct().collect(Collectors.toList());
    }

    private Map<Long, List<DeptTreeVO>> findDeptChildMap(List<DeptTreeVO> deptTreeVOS) {
        Map<Long, List<DeptTreeVO>> deptChildMap = new HashMap<>();
        for (DeptTreeVO deptTreeVO : deptTreeVOS) {
            if (CollUtil.isEmpty(deptTreeVO.getChildren())){
                deptChildMap.put(deptTreeVO.getId(), Lists.newArrayList());
                continue;
            }
            deptChildMap.put(deptTreeVO.getId(), Lists.newArrayList(deptTreeVO.getChildren()));
            Map<Long, List<DeptTreeVO>> curDeptChildMap = findDeptChildMap(deptTreeVO.getChildren());
            deptChildMap.putAll(curDeptChildMap);
            deptChildMap.get(deptTreeVO.getId()).addAll(curDeptChildMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
        }
        return deptChildMap;
    }

    private List<DeptIntervalTreeVO> findChainDept(List<Long> deptIdList, List<DeptTreeVO> deptTreeVOS) {
        if (CollUtil.isEmpty(deptIdList)){
            return new ArrayList<>();
        }
        Map<Long, Integer> targetDeptMap = new HashMap<>();
        Integer i = 0;
        // 根据目标id寻找跟其所有祖先节点
        List<DeptIntervalTreeVO> result = new ArrayList<>();
        for (Long deptId : deptIdList) {
            DeptIntervalTreeVO rootDept = findRootDept(deptId, deptTreeVOS);
            if (Objects.isNull(rootDept)){
                continue;
            }
            if (targetDeptMap.containsKey(rootDept.getId())){
                // 代表有相同祖先，需要进行合并
                mergeDept(result.get(targetDeptMap.get(rootDept.getId())), rootDept);
                continue ;
            }
            targetDeptMap.put(rootDept.getId(), i);
            result.add(rootDept);
            i++;
        }
        return TreeUtil.buildTree(result, false);
    }

    /**
     * 合并部门
     * @param deptIntervalTreeVO
     * @param rootDept
     */
    private void mergeDept(DeptIntervalTreeVO deptIntervalTreeVO, DeptIntervalTreeVO rootDept) {
        List<DeptIntervalTreeVO> targetChildren = deptIntervalTreeVO.getChildren();
        List<DeptIntervalTreeVO> curRoleChildren = rootDept.getChildren();
        if (CollUtil.isEmpty(curRoleChildren)){
            return ;
        }
        if (CollUtil.isEmpty(targetChildren)){
            deptIntervalTreeVO.setChildren(curRoleChildren);
            return ;
        }
        for (DeptIntervalTreeVO targetChild : targetChildren) {
            if (Objects.equals(targetChild.getId(), curRoleChildren.get(0).getId())){
                mergeDept(targetChild, curRoleChildren.get(0));
                return ;
            }
        }
        deptIntervalTreeVO.getChildren().addAll(curRoleChildren);
    }

    /**
     * 寻找目标祖先节点
     * @param deptId
     * @param deptTreeVOS
     * @return
     */
    private DeptIntervalTreeVO findRootDept(Long deptId, List<DeptTreeVO> deptTreeVOS) {
        DeptIntervalTreeVO root = new DeptIntervalTreeVO();
        for (DeptTreeVO deptTreeVO : deptTreeVOS) {
            DeptIntervalTreeVO deptIntervalTreeVO = DeptConvert.INSTANCE.convert2DeptIntervalTreeVO(deptTreeVO);
            if (deptTreeVO.getId().equals(deptId)){
                return deptIntervalTreeVO;
            }
            deptIntervalTreeVO.setClickFlag(false);
            if (CollUtil.isNotEmpty(deptTreeVO.getChildren())){
                // 假设deptId的祖先节点就是它
                root.setChildren(Lists.newArrayList(deptIntervalTreeVO));
                if (helpFindRootDept(deptId, deptIntervalTreeVO, deptTreeVO.getChildren())){
                    return root.getChildren().get(0);
                }
            }
        }
        if (CollUtil.isNotEmpty(root.getChildren())){
            return root.getChildren().get(0);
        }
        return null;
    }

    private boolean helpFindRootDept(Long deptId, DeptIntervalTreeVO deptIntervalTreeVO, List<DeptTreeVO> deptTreeVOS) {
        for (DeptTreeVO deptTreeVO : deptTreeVOS) {
            DeptIntervalTreeVO curDeptIntervalTreeVO = DeptConvert.INSTANCE.convert2DeptIntervalTreeVO(deptTreeVO);
            deptIntervalTreeVO.setChildren(Lists.newArrayList(curDeptIntervalTreeVO));
            if (deptTreeVO.getId().equals(deptId)){
                return true;
            }
            curDeptIntervalTreeVO.setClickFlag(false);
            if (CollUtil.isEmpty(deptTreeVO.getChildren())){
                continue;
            }
            if (helpFindRootDept(deptId, curDeptIntervalTreeVO, deptTreeVO.getChildren())){
                return true;
            }
        }
        return false;
    }

    /**
     * 将deptId中具有包含关系的部门全部清除
     * @param deptIdList
     * @param deptChildMap
     */
    private void weedOutIncludeDeptId(List<Long> deptIdList,  Map<Long, List<DeptTreeVO>> deptChildMap) {
        Map<Long, Set<Long>> deptChildIdListMap = deptChildMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(DeptTreeVO::getId).collect(Collectors.toSet())));
        if (CollUtil.isEmpty(deptIdList)){
            return ;
        }
        Deque<Long> q = new ArrayDeque<>();
        q.addAll(deptIdList);
        while (!q.isEmpty()){
            Long deptId = q.poll();
            Set<Long> childDeptIdList = deptChildIdListMap.get(deptId);
            if (CollUtil.isEmpty(childDeptIdList)){
                continue;
            }
            deptIdList.removeAll(childDeptIdList);
            if (deptIdList.size() == 1){
                break;
            }
        }
    }

}
