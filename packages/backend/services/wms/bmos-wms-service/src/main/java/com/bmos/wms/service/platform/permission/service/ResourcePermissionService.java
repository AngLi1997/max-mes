package com.bmos.wms.service.platform.permission.service;

import com.bmos.common.tree.CommonTreeVO;
import com.bmos.wms.service.platform.permission.dto.ResourcePermissionSaveDTO;

import java.util.List;

public interface ResourcePermissionService {

    /**
     * 保存资源权限
     *
     * @param dto
     */
    void save(ResourcePermissionSaveDTO dto);

    /**
     * 删除资源权限
     *
     * @param resourceId
     */
    void deleteByResourceId(Long resourceId);

    List<Long> getDeptListByResourceId(Long resourceId);

    List<CommonTreeVO> getDeptTree();

    List<CommonTreeVO> getDeptPartitionTree();

}
