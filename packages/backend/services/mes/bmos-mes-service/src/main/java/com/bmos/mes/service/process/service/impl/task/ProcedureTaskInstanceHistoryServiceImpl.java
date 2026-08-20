package com.bmos.mes.service.process.service.impl.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.common.enums.execute.DateTypeEnum;
import com.bmos.mes.service.audit.vo.AuditHistoryVO;
import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.plan.info.vo.PlanRetraceExecutePageVO;
import com.bmos.mes.service.process.convert.Task.ProcessTaskConverter;
import com.bmos.mes.service.process.mapper.task.ProcedureTaskInstanceHistoryMapper;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceHistoryService;
import com.bmos.mes.service.workflow.dto.query.PlanSubRecordQueryDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowProcedureStepDTO;
import com.bmos.mes.service.workflow.vo.PlanSubRecordVO;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.orchestrator.engine.core.query.resp.ExecutionUserTaskResp;
import com.bmos.orchestrator.engine.core.query.service.ExecutionQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcedureTaskInstanceHistoryServiceImpl implements ProcedureTaskInstanceHistoryService {

    @Autowired
    private ProcedureTaskInstanceHistoryMapper historyMapper;

    @Resource
    private ExecutionQueryService executionQueryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<ProcedureTaskInstance> taskInstancesHistory) {
        historyMapper.saveOrUpdateBatch(ProcessTaskConverter.INSTANCE.convertToTaskHistory(taskInstancesHistory));
    }

    @Override
    public List<ProcedureTaskInstanceHistory> selectHistoryTask(WorkFlowProcedureStepDTO stepDTO) {
        return historyMapper.selectHistoryTask(stepDTO);
    }

    @Override
    public List<PlanSubRecordVO> queryPlanSubRecord(PlanSubRecordQueryDTO dto) {
        return historyMapper.selectPlanSubRecord(dto);
    }

    @Override
    public List<ProcedureTaskInstanceHistory> queryListPlanIdAndCompleteState(Long planId,String state) {
        return historyMapper.queryListPlanIdAndCompleteState(planId,state);
    }

    @Override
    public CommonPage<PlanRetraceExecutePageVO> executeTracePage(PlanRetraceInfoPageDTO dto) {
        BasePage page = new BasePage();
        page.setPageNum(dto.getPageNum());
        page.setPageSize(dto.getPageSize());
        List<ExecutionUserTaskResp> executionUserTask = executionQueryService.findHistoryUserTaskByBusinessKey(String.valueOf(dto.getPlanId()));
        List<PlanRetraceExecutePageVO> pageVoList = executionUserTask.stream().map(item -> {
            PlanRetraceExecutePageVO vo = new PlanRetraceExecutePageVO();
            vo.setProcedureName(item.getProcedureName());
            vo.setProcedureStepName(item.getElementName());
            vo.setProcessChangeNum(String.valueOf(item.getProcessChangeNumber()));
            vo.setProcedureStepNum(String.valueOf(item.getProcedureChangeNumber()));
            vo.setProcedureStepStartTime(LocalDateTimeUtil.format(item.getActiveTime(), DatePattern.NORM_DATETIME_PATTERN));
            vo.setProcedureStepEndTime(LocalDateTimeUtil.format(item.getEndTime(), DatePattern.NORM_DATETIME_PATTERN));
            vo.setCompleter(item.getCompletedBy());
            return vo;
        }).collect(Collectors.toList());
        pageVoList.addAll(historyMapper.queryListByPlanId(dto.getPlanId()));
        List<PlanRetraceExecutePageVO> pageVOSortedList = pageVoList
                .stream()
                .filter(item->ObjectUtil.isNotNull(item.getProcedureStepStartTime()))
                .sorted(Comparator.comparing(PlanRetraceExecutePageVO::getProcedureStepStartTime))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(pageVOSortedList)){
            return CommonPage.CommonPage(Collections.emptyList(),0L,page);
        }
        int limitStart = (dto.getPageNum() - 1) * dto.getPageSize();
        int pageSize = dto.getPageSize();
        if (limitStart > 0) {
            pageSize = limitStart + page.getPageSize();
        }
        if (limitStart < pageVOSortedList.size()) {
            List<PlanRetraceExecutePageVO> manageVO = pageVOSortedList.subList(limitStart, Math.min(pageSize, pageVOSortedList.size()));
            return CommonPage.CommonPage(manageVO, (long) pageVOSortedList.size(), page);
        }
        return CommonPage.CommonPage(Collections.emptyList(),0L,page);
    }

    @Override
    public ProcedureTaskInstanceHistory selectHistoryTaskById(String taskInstanceId) {
        return historyMapper.selectById(taskInstanceId);
    }
}
