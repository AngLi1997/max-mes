package com.bmos.mes.service.execute.service;

import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.vo.ChangeTeamRecordCopyChangeTeamVO;
import com.bmos.mes.service.execute.vo.CopyRecordItemVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ExecuteRecordCopyService {
    ExecuteRecordCopy copyRecordItem(RecordCopySaveDTO dto);

    List<CopyRecordItemVO> getCopyVersionList(RecordCopyQueryDTO dto);

    void save(ExecuteRecordCopy copy);

    Boolean existCopy(FormDataBatchSaveDTO dto);

    void discardRecordItem(FormDataDiscardDTO dto);

    List<ExecuteRecordCopy> getList(Long productPlanId, Set<Long> stepIds);

    List<ExecuteRecordCopy> getCurrentStepCopies(CopiesQueryDTO build);

    List<ExecuteRecordCopy> getListByRecordItemIds(Long productPlanId, Collection<Long> recordItems);

    List<ExecuteRecordCopy> getListByRecordVersion(Long productPlanId, Long recordVersionId);

    Long getVersionMaxValue(RecordCopyQueryDTO dto);

    /**
     * 查询工步换班信息
     * @param dto
     * @return
     */
    List<ChangeTeamRecordCopyChangeTeamVO> queryStepChangeTeamList(ProcedureStepChangeNumberQueryDTO dto);

    /**
     * 查询复制记录页
     * @param planId 生产计划id
     * @param procedureStepModelId 工步模型id
     * @param copyVersion 复制版本
     * @return
     */
    ExecuteRecordCopy getCurrentChangeRecord(Long planId, Long procedureStepModelId, Long copyVersion);

    /**
     * 获取批次中所有的复制版本
     * @param planIdList
     * @return
     */
    List<ExecuteRecordCopy> getByPlanIdList(List<Long> planIdList);
}
