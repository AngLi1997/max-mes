package com.bmos.mes.service.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mes.service.execute.dto.IntactMergeListQueryDTO;
import com.bmos.mes.service.execute.vo.IntactFormDataVO;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.process.dto.*;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessRecordOrderQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcedureStepRole;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mes.service.process.vo.Task.ProcedureStepAndTaskVO;
import com.bmos.mes.service.workflow.vo.ProcedureStepDurationVO;

import java.util.List;

public interface ProcedureStepModelService extends IService<ProcedureStepModel> {
    void saveBatch(ProcedureModel procedureModel, List<ProcedureStepDTO> saveDTO);

    void saveBatch(List<ProcedureStepModel> procedureStepModels);

    ProcedureStepAndTaskVO getByProcedureModelId(String recordVersionIds, Long procedureId);

    void saveConfig(ProcedureStepConfigSaveDTO dto);

    List<ComponentConfigVO> getConfigList(ProcedureStepConfigListQueryDTO dto);

    void deleteByIdNotIn(Long procedureModelId,List<Long> ids);

    void updateBatchRoles(List<ProcedureStepDTO> items);

    void updateBatchById(ProcedureModel procedureModel, List<ProcedureStepDTO> items);

    List<HistoricVO> getHistoricStepList(ProcedureStepHistoricQueryDTO dto);

    List<ProcedureStepModel> getByProcedureModelIds(List<Long> procedureModelIds);

    List<ProcessRecordVO> getRecords(ProcessRecordOrderQueryDTO dto);

    ProcedureStepRecordItemVO getRecordItem(ProcedureStepRecordItemQueryDTO dto);

    List<ProcessStepVO> getListByProcess(ProcessStepQueryDTO dto);

    List<FieldConfigVO> getFieldsConfig(CalculateDataQueryDTO query, List<Long> fieldIds);

    List<PlatformUserVO> getGroupUserList(ProcedureStepGroupUserDTO dto);

    List<IntactFormDataVO> getRecordContents(IntactMergeListQueryDTO dto);

    void deleteByProcedureModelId(Long procedureModelId);

    ProcedureStepModel getById(Long procedureStepModelId);

    List<ProcessRecordItemVO> queryRecordVersionIdByProcessId(Long processId, String processVersion,List<Long> modelId);

    List<ProcedureStepDurationVO> getProcedureAndStepDurationByNodeIds(List<String> procedureStepNodeIdList);

    List<ProcedureStepModel> getStepModelByProcessIdAndVersion(Long processId, String modifyBeforeVersion);

    void updateBatch(List<ProcedureStepModel> stepModel);

    List<NodeVO> getNodeListByProcedureModeId(Long id,Boolean type,Long stepModelId);

    List<ProcedureStepModel> getStepModelByProcessIdAndVersionAndNodeIdList(Long processId, String processVersion);

    List<ProcedureStepModel> getByIdList(List<Long> idList);

    /**
     * 根据步骤模型id获取步骤过程时长
     * @param procedureStepId 步骤模型id
     * @return 查询结果
     */
    List<ProcedureStepDurationVO> getProcedureAndStepDurationByStepModeIds(List<Long> procedureStepId);

    /**
     * 根据工序模型id查询工步列表
     * 不区分步骤和任务
     * @param procedureModelId
     * @return
     */
    List<ProcedureStepModel> getByProcedureModelId(Long procedureModelId);

    /**
     * 根据条件查询工步模型列表
     * 工艺id、工艺版本、工序名称、工步名称
     * @param build
     * @return
     */
    List<ProcedureStepModelDetailVO> queryStepModelList(ProcedureStepModelQueryDTO build);

    /**
     * 根据工序模型查询工步列表
     * @param procedureModelId
     * @return
     */
    List<ProcedureStepModelListVO> getListByProcedureModelId(Long procedureModelId);

    List<ProcedureStepModelVO> selectByProcedureModelIdS(List<Long> procedureModelIdS);

    List<ProcessSortVO> selectStepModelSort(List<Long> modelIdList, Long processId, String version);

    List<ProcedureStepModel> queryListByProcessIdAndVersionAndModelId(Long processId, String processVersion, Long procedureModelId);


    /**
     * 根据节点功能获取记录页
     * @param queryDTO
     * @return
     */
    List<IntactFormDataVO> getRecordContentsByNodeFunction(IntactMergeListQueryDTO queryDTO);

    /**
     * 查询工艺id下哪些工艺版本使用了这个记录项
     * @param processId
     * @param recordItemId
     * @return
     */
    List<String> selectByProcessAndRecordItemId(Long processId, Long recordItemId);

    List<ProcedureStepRole> getStepTeamIdByProcessIdAndVersion(Long processId, String version);

    List<NodeVO> queryByIds(List<Long> deleteTaskId);

    /**
     * 根据工艺id、工艺版本、记录项id查询工步模型
     * @param queryDTOS
     * @return
     */
    List<ProcedureStepModel> getByProcessAndRecord(List<ProcessRecordQueryDTO> queryDTOS);
}
