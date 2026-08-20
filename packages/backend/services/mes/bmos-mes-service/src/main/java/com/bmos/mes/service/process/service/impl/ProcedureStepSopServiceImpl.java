package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepSopMapper;
import com.bmos.mes.service.process.model.ProcedureStepRole;
import com.bmos.mes.service.process.model.ProcedureStepSop;
import com.bmos.mes.service.process.service.ProcedureStepSopService;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Slf4j
@Service
public class ProcedureStepSopServiceImpl implements ProcedureStepSopService {

    @Resource
    private ProcedureStepSopMapper stepSopMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcedureStepSop> sopList) {
        if (CollUtil.isEmpty(sopList)){
            return;
        }
        stepSopMapper.insertBatch(sopList);
    }

    @Override
    public List<ProcedureStepSop> queryListByStepModelId(Set<Long> stepIds) {
        if (CollUtil.isEmpty(stepIds)){
            return new ArrayList<>();
        }
        return stepSopMapper.queryListByStepModelId(stepIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchSops(List<ProcedureStepDTO> items) {
        List<ProcedureStepSop> sopList = new ArrayList<>();
        List<Long> stepModelIdList = CollectionUtils.convertList(items, ProcedureStepDTO::getId);
        stepSopMapper.delete(new LambdaQueryWrapperX<ProcedureStepSop>()
                .in(ProcedureStepSop::getStepModelId,stepModelIdList));
        items.forEach(item -> {
            if (CollUtil.isNotEmpty(item.getOperationSopId())) {
                sopList.addAll(item.getOperationSopId().stream().map(e -> {
                  ProcedureStepSop sop = new ProcedureStepSop();
                  sop.setStepModelId(item.getId());
                  sop.setOperationSopId(e);
                  return sop;
                }).collect(Collectors.toList()));
            }
        });
        if (CollUtil.isNotEmpty(sopList)){
            stepSopMapper.saveOrUpdateBatch(sopList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchByStepModelIds(List<Long> modelIds) {
        if (CollUtil.isEmpty(modelIds)){
            return;
        }
        stepSopMapper.delete(new LambdaQueryWrapperX<ProcedureStepSop>()
                .in(ProcedureStepSop::getStepModelId,modelIds));
    }
}
