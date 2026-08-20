package com.bmos.mes.service.process.service;

import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.vo.ComponentConfigDetailVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProcedureStepConfigService {
    void saveBatch(Collection<ProcedureStepConfig> configs);

    List<ProcedureStepConfig> getListByProcedureStepModel(ProcedureStepModel procedureStepModel);

    //void deleteByProcessStepId(Long procedureStepId);

    List<ProcedureStepConfig> getListByProcedureStepModelIds(Long processId, String version, Set<Long> stepIds);

    List<ComponentConfigDetailVO> getComponentsByProcedureStepModel(ProcedureStepModel procedureStepModel);

    void deleteReuse(ProcedureStepConfigSaveDTO dto);

    void refreshBatch(Long processId, String modifyBeforeVersion, String version);


    void deleteByProcedureStepModelId(Long procedureStepModelId);

    List<BusinessComponentConfigDetailVO> getComponentConfigByProcedureStepModel(ProcedureStepModel procedureStepModel);

    String getComponentConfigJson(Long stepModelId, Long componentId,Boolean reusable, Long processId, String processVersion);

    String getStepComponentConfigJson(Long procedureStepModelId, Long componentId);

    List<ProcedureStepConfig> getListByProcessVersion(Long processId, String version);

    void deleteByProcedureStepModel(ProcedureStepModel procedureStepModel);

    void updateBatch(List<ProcedureStepConfig> needUpdateConfig);

    /**
     * 根据工艺版本和fieldId列表查询配置
     * @param processId 工艺id
     * @param processVersion 工艺版本
     * @param fieldIds fieldId列表
     * @return
     */
    List<ProcedureStepConfig> getListByProcessVersionAndFields(Long processId, String processVersion, List<Long> fieldIds);

}
