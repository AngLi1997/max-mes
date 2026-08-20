package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.mapper.ProcessRelationMapper;
import com.bmos.mes.service.process.model.ProcessRelation;
import com.bmos.mes.service.process.service.ProcessRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessRelationServiceImpl implements ProcessRelationService {
    @Autowired
    private ProcessRelationMapper processRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcessRelation> relations) {
        if (CollUtil.isEmpty(relations)){
            return;
        }
        processRelationMapper.insertBatch(relations);
    }

    @Override
    public List<ProcessRelation> getListByProcessId(Long processId) {
        return processRelationMapper.selectListByProcessId(processId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcessId(Long processId) {
        processRelationMapper.deleteByProcessId(processId);
    }

    @Override
    public List<ProcessRelation> getList() {
        return processRelationMapper.selectList();
    }

}
