package com.bmos.mes.service.permission.service.impl;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.mes.service.permission.convert.ResourcePermissionConverter;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.mapper.ResourcePermissionMapper;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void deleteByResourceId(Long resourceId) {
        if (resourceId == null) {
            return;
        }
        resourcePermissionMapper.deleteByResourceId(resourceId);
    }

    @Override
    public List<Long> getDeptListByResourceId(Long resourceId) {
        //todo cache
        return resourcePermissionMapper.selectDeptIdsByResourceId(resourceId);
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
