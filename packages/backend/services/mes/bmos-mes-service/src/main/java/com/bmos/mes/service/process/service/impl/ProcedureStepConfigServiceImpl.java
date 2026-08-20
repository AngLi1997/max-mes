package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.vo.ComponentConfigDetailVO;
import com.bmos.mes.service.record.mapper.BatchRecordComponentDetailMapper;
import com.bmos.mes.service.record.model.BatchRecordComponentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProcedureStepConfigServiceImpl implements ProcedureStepConfigService {

    @Autowired
    private ProcedureStepConfigMapper procedureStepConfigMapper;
    @Resource
    private BatchRecordComponentDetailMapper batchRecordComponentDetailMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Collection<ProcedureStepConfig> configs) {
        procedureStepConfigMapper.insertBatch(configs);
    }

    @Override
    public List<ProcedureStepConfig> getListByProcedureStepModel(ProcedureStepModel procedureStepModel) {
        return procedureStepConfigMapper.selectListByProcedureStepModel(procedureStepModel);
    }

   /* @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcessStepId(Long procedureStepId) {
        procedureStepConfigMapper.deleteByProcessStepId(procedureStepId);
    }*/

    @Override
    public List<ProcedureStepConfig> getListByProcedureStepModelIds(Long processId, String version, Set<Long> stepIds) {
        if (CollUtil.isEmpty(stepIds)) {
            return new ArrayList<>();
        }
        return procedureStepConfigMapper.selectListByProcedureStepModelIds(processId, version, stepIds);
    }

    @Override
    public List<ComponentConfigDetailVO> getComponentsByProcedureStepModel(ProcedureStepModel procedureStepModel) {
        List<ComponentConfigDetailVO> vos = procedureStepConfigMapper.selectComponentsByProcedureStepId(procedureStepModel);
        if (CollUtil.isEmpty(vos)) {
            return new ArrayList<>();
        }
        List<BatchRecordComponentDetail> details =
                batchRecordComponentDetailMapper.selectBatchIds(CollectionUtils.convertList(vos,
                        ComponentConfigDetailVO::getId));
        Map<Long, BatchRecordComponentDetail> map = CollectionUtils.convertMap(details, BatchRecordComponentDetail::getId);
        vos.forEach(e->{
            BatchRecordComponentDetail detail = map.getOrDefault(e.getId(), new BatchRecordComponentDetail());
            e.setFormulaField(detail.getFormulaField());
            e.setComponentDetail(detail.getComponentDetail());
        });
        return TreeUtil.buildTree(vos, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReuse(ProcedureStepConfigSaveDTO dto) {
        procedureStepConfigMapper.deleteReuse(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshBatch(Long processId, String modifyBeforeVersion, String version) {
        procedureStepConfigMapper.refreshBatch(processId, modifyBeforeVersion, version);
    }


    @Override
    public void deleteByProcedureStepModelId(Long procedureStepModelId) {
        procedureStepConfigMapper.deleteByProcedureStepModelId(procedureStepModelId);
    }

    @Override
    public List<BusinessComponentConfigDetailVO> getComponentConfigByProcedureStepModel(ProcedureStepModel procedureStepModel) {
        return procedureStepConfigMapper.selectComponentConfigByProcedureStepModel(procedureStepModel);
    }

    @Override
    public String getComponentConfigJson(Long stepModelId, Long componentId, Boolean reusable, Long processId,
                                         String processVersion) {
        ProcedureStepConfig procedureStepConfig = procedureStepConfigMapper.selectComponentConfig(stepModelId,
                componentId, reusable, processId, processVersion);
        return procedureStepConfig == null ? null : procedureStepConfig.getConfigInfo();
    }

    @Override
    public String getStepComponentConfigJson(Long procedureStepModelId, Long componentId) {
        ProcedureStepModel stepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        if (stepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        ProcedureStepConfig procedureStepConfig = procedureStepConfigMapper.selectComponentConfig(procedureStepModelId,
                componentId, stepModel.getReusable(), stepModel.getProcessId(), stepModel.getProcessVersion());
        return procedureStepConfig == null ? null : procedureStepConfig.getConfigInfo();
    }

    @Override
    public List<ProcedureStepConfig> getListByProcessVersion(Long processId, String version) {
        return procedureStepConfigMapper.selectByProcessVersion(processId, version);
    }

    @Override
    public void deleteByProcedureStepModel(ProcedureStepModel procedureStepModel) {
        procedureStepConfigMapper.deleteByProcedureStepModel(procedureStepModel);
    }

    @Override
    public void updateBatch(List<ProcedureStepConfig> needUpdateConfig) {
        if (CollUtil.isNotEmpty(needUpdateConfig)) {
            procedureStepConfigMapper.updateBatch(needUpdateConfig);
        }
    }

    @Override
    public List<ProcedureStepConfig> getListByProcessVersionAndFields(Long processId, String processVersion, List<Long> fieldIds) {
        if (CollUtil.isEmpty(fieldIds)) {
            return new ArrayList<>();
        }
        return procedureStepConfigMapper.selectByProcessVersionAndFields(processId, processVersion, fieldIds);
    }
}
