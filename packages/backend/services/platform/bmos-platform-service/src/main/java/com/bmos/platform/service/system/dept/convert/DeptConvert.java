package com.bmos.platform.service.system.dept.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.platform.facade.system.dept.vo.DeptIntervalTreeVO;
import com.bmos.platform.facade.system.dept.vo.DeptTreeVO;
import com.bmos.platform.service.system.dept.dto.DeptSaveDTO;
import com.bmos.platform.service.system.dept.dto.DeptUpdateDTO;
import com.bmos.platform.service.system.dept.model.Dept;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import com.bmos.platform.service.system.dept.model.DeptRole;
import com.bmos.platform.service.system.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface DeptConvert {
    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    Dept convert(DeptSaveDTO dto);

    Dept convert(DeptUpdateDTO dto);

    default List<DeptUserTreeVO> convertTree(Map<Long, Dept> deptMap, List<Dept> deptList) {
        return deptList.stream()
                .map(dept -> {
                    DeptUserTreeVO node = new DeptUserTreeVO();
                    node.setName(dept.getDeptName());
                    node.setCode(dept.getCode());
                    node.setId(String.valueOf(dept.getId()));
                    node.setParentId(String.valueOf(dept.getParentId()));
                    node.setDeptFlag(true);
                    node.setCreateTime(dept.getCreateTime());
                    node.setRemark(dept.getRemark());
                    Dept parentDept = deptMap.get(dept.getParentId());
                    if (ObjectUtil.isNotNull(parentDept)) {
                        node.setParentName(parentDept.getDeptName());
                    }
                    return node;
                })
                .collect(Collectors.toList());
    }

    DeptIntervalTreeVO convert2DeptIntervalTreeVO(DeptTreeVO deptTreeNodeVO);

    List<DeptIntervalTreeVO> convert2DeptIntervalTreeVOList(List<DeptTreeVO> deptTreeNodeVOList);

    default DeptUserTreeVO convertDeptUser(DeptUserTreeVO parentNode, User user) {
        DeptUserTreeVO node = new DeptUserTreeVO();
        node.setName(user.getUserName());
        node.setCode(null);
        node.setId(user.getUserId());
        node.setDeptFlag(false);
        node.setCreateTime(user.getCreateTime());
        node.setParentId(parentNode.getId());
        node.setLoginName(user.getLoginName());
        node.setParentName(parentNode.getName());
        return node;
    }

    default List<DeptUserTreeVO> convertDeptUserList(List<User> users) {
        return users.stream().map(user -> {
            DeptUserTreeVO node = new DeptUserTreeVO();
            node.setName(user.getUserName());
            node.setCode(null);
            node.setId(user.getUserId());
            node.setDeptFlag(false);
            node.setCreateTime(user.getCreateTime());
            node.setLoginName(user.getLoginName());
            return node;
        }).collect(Collectors.toList());
    }

    default List<CommonTreeVO> convertTreeNode(List<Dept> deptParents){
        return deptParents.stream().map(e -> {
            CommonTreeVO treeVO = new CommonTreeVO();
            treeVO.setId(e.getId());
            treeVO.setName(e.getDeptName());
            treeVO.setParentId(e.getParentId());
            treeVO.setCreateTime(e.getCreateTime());
            return treeVO;
        }).collect(Collectors.toList());
    }

    default List<DeptRole> convert2DeptRoleList(List<Long> roleIds, Long deptId){
        List<DeptRole> deptRoles = new ArrayList<>();
        if (CollUtil.isEmpty(roleIds)){
            return deptRoles;
        }
        for (Long roleId : roleIds) {
            DeptRole deptRole = new DeptRole();
            deptRole.setDeptId(deptId);
            deptRole.setRoleId(roleId);
            deptRoles.add(deptRole);
        }
        return deptRoles;
    }

    default List<DeptRole> convert2RoleDeptList(List<Long> deptIdList, Long roleId){
        List<DeptRole> deptRoles = new ArrayList<>();
        if (CollUtil.isEmpty(deptIdList)){
            return deptRoles;
        }
        for (Long deptId : deptIdList) {
            DeptRole deptRole = new DeptRole();
            deptRole.setDeptId(deptId);
            deptRole.setRoleId(roleId);
            deptRoles.add(deptRole);
        }
        return deptRoles;
    }
}
