package com.bmos.platform.service.system.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.service.system.role.convert.RoleRelateMenuConvert;
import com.bmos.platform.service.system.role.mapper.RoleRelateMenuMapper;
import com.bmos.platform.service.system.role.model.RoleRelateMenu;
import com.bmos.platform.service.system.role.service.RoleMenuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class RoleMenuRelationServiceImpl implements RoleMenuRelationService {

    @Autowired
    private RoleRelateMenuMapper roleRelateMenuMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuId(Long menuId) {
        roleRelateMenuMapper.deleteByMenuId(menuId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<RoleRelateMenu> list) {
        if (CollUtil.isEmpty(list)){
            return;
        }
        roleRelateMenuMapper.insertBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleId(Long roleId) {
        roleRelateMenuMapper.deleteByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        roleRelateMenuMapper.deleteBatchIds(ids);
    }

    @Override
    public List<RoleRelateMenu> getByRoleId(Long roleId) {
        return roleRelateMenuMapper.getByRoleId(roleId);
    }

    @Override
    public Boolean existUser(Long roleId) {
        return roleRelateMenuMapper.existsUser(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIdAndRootMenuIds(Long roleId, Collection<Long> rootMenuIds) {
        if (CollUtil.isEmpty(rootMenuIds)){
            return ;
        }
        roleRelateMenuMapper.deleteByRoleIdAndRootMenuIds(roleId,rootMenuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuIdAndRoleIds(Long menuId, List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)){
            return;
        }
        roleRelateMenuMapper.deleteByMenuIdAndRoleIds(menuId,roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByMenuIds(List<Long> menuIds) {
        roleRelateMenuMapper.deleteByMenuIds(menuIds);
    }

    @Override
    public List<RoleRelateMenu> selectByMenuIdLikeAndRoleIds(Long parentMenuId, List<Long> roleIds) {
        return roleRelateMenuMapper.selectByMenuIdLikeAndRoleIds(parentMenuId,roleIds);
    }

    @Override
    public List<RoleRelateMenu> getByMenuId(Long menuId) {
        return roleRelateMenuMapper.selectByMenuId(menuId);
    }

    @Override
    public List<RoleRelateMenu> selectByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds) {
        return roleRelateMenuMapper.getListByRoleId(roleId,menuIds);
    }

}
