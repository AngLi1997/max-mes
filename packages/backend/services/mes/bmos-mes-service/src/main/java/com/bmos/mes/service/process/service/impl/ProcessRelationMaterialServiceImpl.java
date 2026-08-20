package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.mapper.ProcessRelationMaterialMapper;
import com.bmos.mes.service.process.model.ProcessRelationMaterial;
import com.bmos.mes.service.process.service.ProcessRelationMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ProcessRelationMaterialServiceImpl implements ProcessRelationMaterialService {

    @Autowired
    private ProcessRelationMaterialMapper processRelationMaterialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcessRelationMaterial> relationMaterials) {
        if (CollUtil.isEmpty(relationMaterials)){
            return;
        }
        processRelationMaterialMapper.insertBatch(relationMaterials);
    }

    @Override
    public List<ProcessRelationMaterial> getListByProcessRelationIds(Set<Long> processRelationIds) {
        if (CollUtil.isEmpty(processRelationIds)){
            return Collections.emptyList();
        }
        return processRelationMaterialMapper.selectListByProcessRelationIds(processRelationIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcessId(Long processId) {
        processRelationMaterialMapper.deleteByProcessId(processId);
    }
}
