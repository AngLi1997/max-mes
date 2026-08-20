package com.bmos.lims2.server.permission.service.impl;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.server.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.lims2.server.permission.mapper.ResourcePermissionMapper;
import com.bmos.lims2.server.permission.model.ResourcePermission;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourcePermissionServiceImpl implements ResourcePermissionService {

    @Resource
    private ResourcePermissionMapper resourcePermissionMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ResourcePermissionSaveDTO dto) {
        resourcePermissionMapper.deleteByResourceId(dto.getResourceId());
        List<ResourcePermission> collect = dto.getDeptIds()
                .stream()
                .map(deptId -> {
                    ResourcePermission resourcePermission = new ResourcePermission();
                    resourcePermission.setResourceId(dto.getResourceId());
                    resourcePermission.setDeptId(deptId);
                    resourcePermission.setModule(dto.getModule());
                    return resourcePermission;
                }).collect(Collectors.toList());
        resourcePermissionMapper.insertBatch(collect);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByResourceId(Long resourceId) {
        if (resourceId == null) {
            return;
        }
        resourcePermissionMapper.deleteByResourceId(resourceId);
    }

    @Override
    public List<Long> getDeptListByResourceId(Long resourceId) {
        return CollectionUtils.convertList(resourcePermissionMapper.selectDeptIdsByResourceId(resourceId), ResourcePermission::getDeptId);
    }

    @Override
    public List<CommonTreeVO> getDeptTree() {
        return platformApiAdaptor.deptTree();
    }

    @Override
    public List<CommonTreeVO> getDeptPartitionTree() {
        return platformApiAdaptor.deptPartitionTree();
    }

}
