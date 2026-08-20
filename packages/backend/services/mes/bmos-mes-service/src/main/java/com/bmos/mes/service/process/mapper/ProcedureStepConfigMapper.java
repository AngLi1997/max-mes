package com.bmos.mes.service.process.mapper;

import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.vo.ComponentConfigDetailVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

import static com.bmos.mes.service.process.constant.ProcessConstant.REUSE_PROCEDURE_STEP_ID;

@Mapper
public interface ProcedureStepConfigMapper extends BaseMapperX<ProcedureStepConfig> {

    List<ProcedureStepConfig> selectListByProcedureStepModel(ProcedureStepModel stepModel);

    default List<ProcedureStepConfig> selectListByProcedureStepModelIds(Long processId, String version,
                                                                        Set<Long> stepIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcessId, processId)
                .eq(ProcedureStepConfig::getVersion, version)
                .in(ProcedureStepConfig::getProcedureStepModelId, stepIds));
    }

    List<ComponentConfigDetailVO> selectComponentsByProcedureStepId(ProcedureStepModel procedureStepModel);

    default void deleteReuse(ProcedureStepConfigSaveDTO dto) {
        delete(new LambdaQueryWrapperX<ProcedureStepConfig>().eq(ProcedureStepConfig::getProcessId, dto.getProcessId())
                .eq(ProcedureStepConfig::getVersion, dto.getVersion())
                .eq(ProcedureStepConfig::getProcedureStepModelId, REUSE_PROCEDURE_STEP_ID)
                .eq(ProcedureStepConfig::getRecordVersionId, dto.getRecordVersionId())
                .eq(ProcedureStepConfig::getRecordItemId, dto.getRecordItemId()));
    }

    default void refreshBatch(Long processId, String modifyBeforeVersion, String version) {
        ProcedureStepConfig config = new ProcedureStepConfig();
        config.setVersion(version);
        update(config,
                new LambdaQueryWrapperX<ProcedureStepConfig>()
                        .eq(ProcedureStepConfig::getProcessId, processId)
                        .eq(ProcedureStepConfig::getVersion, modifyBeforeVersion));
    }

    List<BusinessComponentConfigDetailVO> selectComponentConfigByProcedureStepModel(ProcedureStepModel procedureStepModel);

    default void deleteByProcedureStepModelId(Long procedureStepModelId) {
        delete(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcedureStepModelId, procedureStepModelId));
    }

    default ProcedureStepConfig selectComponentConfig(Long stepModelId, Long componentId, Boolean reusable,
                                                      Long processId, String processVersion) {
        return selectOne(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcessId, processId)
                .eq(ProcedureStepConfig::getVersion, processVersion)
                .eq(ProcedureStepConfig::getComponentId, componentId)
                .eq(ProcedureStepConfig::getProcedureStepModelId, reusable ? 0 : stepModelId));
    }

    default List<ProcedureStepConfig> selectByProcessVersion(Long processId, String version){
            return selectList(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcessId, processId)
                .eq(ProcedureStepConfig::getVersion, version));
    }

    default void deleteByProcedureStepModel(ProcedureStepModel procedureStepModel){
        delete(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcedureStepModelId, procedureStepModel.getId())
                .eq(ProcedureStepConfig::getRecordItemId, procedureStepModel.getRecordItemId())
                .eq(ProcedureStepConfig::getRecordVersionId, procedureStepModel.getRecordVersionId()));
    }

    default List<ProcedureStepConfig> selectByProcessVersionAndFields(Long processId, String processVersion,
                                                                      List<Long> fieldIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcessId, processId)
                .eq(ProcedureStepConfig::getVersion, processVersion)
                .in(ProcedureStepConfig::getFieldId, fieldIds));
    }
}
