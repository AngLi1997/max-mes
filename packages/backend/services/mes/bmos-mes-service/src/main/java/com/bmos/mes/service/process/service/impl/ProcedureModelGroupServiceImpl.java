package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.process.mapper.ProcedureModelGroupMapper;
import com.bmos.mes.service.process.model.ProcedureModelGroup;
import com.bmos.mes.service.process.service.ProcedureModelGroupService;
import com.bmos.mes.service.process.vo.ProcessConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProcedureModelGroupServiceImpl implements ProcedureModelGroupService {

    @Autowired
    private ProcedureModelGroupMapper procedureModelGroupMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcedureModelGroup> groups) {
        if (CollUtil.isEmpty(groups)){
            return;
        }
        procedureModelGroupMapper.insertBatch(groups);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcedureModelIds(List<Long> procedureModelIds) {
        if (CollUtil.isEmpty(procedureModelIds)){
            return;
        }
        procedureModelGroupMapper.deleteByProcedureModelIds(procedureModelIds);
    }

    @Override
    public Map<Long, List<Long>> getByProcedureModelIds(Set<Long> procedureModelIds) {
        if (CollUtil.isEmpty(procedureModelIds)){
            return Collections.emptyMap();
        }
        List<ProcedureModelGroup> groups = procedureModelGroupMapper.selectByProcedureModelIds(procedureModelIds);
        return CollectionUtils.convertMultiMap(groups,ProcedureModelGroup::getProcedureModelId,ProcedureModelGroup::getGroupId);
    }

    @Override
    public List<Long> getByProcedureModelId(Long procedureModelId) {
        List<ProcedureModelGroup> groups = procedureModelGroupMapper.selectByProcedureModelId(procedureModelId);
        if (CollUtil.isEmpty(groups)){
            return Collections.emptyList();
        }
        return CollectionUtils.convertList(groups,ProcedureModelGroup::getGroupId);
    }

    @Override
    public List<ProcessConfigVO> getDeleteByProcedureModelId(Set<Long> procedureModelIds) {
        return procedureModelGroupMapper.selectDeleteTeamByModelIds(procedureModelIds);
    }
}
