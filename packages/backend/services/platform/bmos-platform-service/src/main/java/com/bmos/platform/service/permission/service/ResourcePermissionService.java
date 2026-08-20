package com.bmos.platform.service.permission.service;

import com.bmos.common.tree.CommonTreeVO;
import com.bmos.platform.service.permission.model.ResourcePermission;
import com.bmos.platform.service.permission.service.dto.ResourcePermissionSaveDTO;

import java.util.List;

public interface ResourcePermissionService {

    /**
     * 保存资源权限
     *
     * @param dto
     */
    void save(ResourcePermissionSaveDTO dto);

    /**
     * 根据资源id获取部门id
     * @param resourceId
     * @return
     */
    List<Long> getDeptListByResourceId(Long resourceId);

    /**
     * 删除数据权限
     * @param resourceId
     */
    void deleteByResourceId(Long resourceId);

    /**
     * 资源id集合
     * @param resourceIdList
     * @return
     */
    List<ResourcePermission> getDeptListByResourceIdList(List<Long> resourceIdList);
}
