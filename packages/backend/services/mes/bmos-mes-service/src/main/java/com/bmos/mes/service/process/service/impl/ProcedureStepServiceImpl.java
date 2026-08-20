package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.service.process.convert.ProcessStepConverter;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.dto.ProcedureStepValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStep;
import com.bmos.mes.service.process.service.ProcedureStepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProcedureStepServiceImpl implements ProcedureStepService {

    @Autowired
    private ProcedureStepMapper procedureStepMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProcedureStep> saveBatch(ProcedureModel procedureModel, List<ProcedureStepDTO> procedureStepDTOS,
                                         StepTaskTypeEnum type) {
        if (CollUtil.isEmpty(procedureStepDTOS)){
            return Collections.emptyList();
        }
        List<ProcedureStep> procedureSteps = ProcessStepConverter.INSTANCE.convertList(procedureModel,procedureStepDTOS);
        procedureSteps.forEach(procedureStep -> procedureStep.setType(type));
        saveBatch(procedureSteps);
        return procedureSteps;
    }

    @Override
    public List<ProcedureStep> getHistoricList(ProcedureStepHistoricQueryDTO dto) {
        return procedureStepMapper.selectHistoricList(dto);
    }

    @Override
    public ProcedureStep getById(Long id) {
        return procedureStepMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcedureStep> steps) {
        if (CollUtil.isEmpty(steps)){
            return;
        }
        log.info("新增工步基础表信息: {}", steps);
        try {
            procedureStepMapper.insertBatch(steps);
        } catch (DuplicateKeyException e) {
            // todo
//            throw new BmosException();
        }
    }

    @Override
    public List<ProcedureStep> getByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return procedureStepMapper.selectBatchIds(ids);
    }

    @Override
    public Boolean validateProcedureStepName(ProcedureStepValidateDTO dto) {
        ProcedureStep step = procedureStepMapper.selectByProcedureIdAndName(dto.getProcedureId(), dto.getName());
        if (step != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
