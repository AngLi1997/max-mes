package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.mapper.ProcedureStepRoleRelationMapper;
import com.bmos.mes.service.process.model.ProcedureStepRole;
import com.bmos.mes.service.process.service.ProcedureStepRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ProcedureStepRoleRelationServiceImpl implements ProcedureStepRoleRelationService {

    @Autowired
    private ProcedureStepRoleRelationMapper procedureStepRoleRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcedureStepRole> roles) {
        if (CollUtil.isEmpty(roles)){
            return;
        }
        procedureStepRoleRelationMapper.insertBatch(roles);
    }

    @Override
    public List<ProcedureStepRole> getListByProcedureStepIds(Set<Long> procedureStepIds) {
        if (CollUtil.isEmpty(procedureStepIds)){
            return Collections.emptyList();
        }
        return procedureStepRoleRelationMapper.selectListByProcedureStepIds(procedureStepIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcedureStepIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)){
            return;
        }
        procedureStepRoleRelationMapper.deleteByProcedureStepIds(ids);
    }
}
