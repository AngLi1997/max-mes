package com.bmos.platform.service.permission.service.impl;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.platform.service.permission.convert.ResourcePermissionConverter;
import com.bmos.platform.service.permission.mapper.ResourcePermissionMapper;
import com.bmos.platform.service.permission.model.ResourcePermission;
import com.bmos.platform.service.permission.service.ResourcePermissionService;
import com.bmos.platform.service.permission.service.dto.ResourcePermissionSaveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ResourcePermissionServiceImpl implements ResourcePermissionService {

    @Autowired
    private ResourcePermissionMapper resourcePermissionMapper;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ResourcePermissionSaveDTO dto) {
        resourcePermissionMapper.deleteByResourceId(dto.getResourceId());
        resourcePermissionMapper.insertBatch(ResourcePermissionConverter.INSTANCE.convertList(dto));
    }

    @Override
    public List<Long> getDeptListByResourceId(Long resourceId) {
        return resourcePermissionMapper.selectDeptIdsByResourceId(resourceId);
    }

    @Override
    public void deleteByResourceId(Long resourceId) {
        resourcePermissionMapper.deleteByResourceId(resourceId);
    }

    @Override
    public List<ResourcePermission> getDeptListByResourceIdList(List<Long> resourceIdList) {
        return resourcePermissionMapper.selectListByResourceIdList(resourceIdList);
    }

}
