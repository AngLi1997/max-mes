package com.bmos.lims2.server.eln.conclusion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.BusinessComponentTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.conclusion.dto.ConclusionComponentSaveDTO;
import com.bmos.lims2.server.eln.conclusion.service.ConclusionService;
import com.bmos.lims2.server.eln.entry.dto.BusinessComponentBatchSaveDTO;
import com.bmos.lims2.server.eln.entry.dto.ElnEntryContext;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.lims2.server.eln.entry.mapper.ExecuteFormDataMapper;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataService;
import com.bmos.lims2.server.eln.record.component.BusinessComponentStrategy;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Objects;
import java.time.LocalDateTime;

/**
 * @Description: 结论组件服务实现
 * @Author: yigaohui
 * @Date: 2025/11/19 10:26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConclusionServiceImpl implements ConclusionService {

    private static final String ERR_CONCLUSION_JUDGMENT_CONFIGURED = "该分析项已配置结论判定，不允许录入结论组件，parameterConfigId=%s";

    @Autowired
    private Map<String, BusinessComponentStrategy> componentStrategyMap;

    @Autowired
    private BatchRecordComponentService recordComponentService;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Autowired
    private InspectionSchemeJudgmentMapper inspectionSchemeJudgmentMapper;

    @Autowired
    private StabilitySchemeJudgmentMapper stabilitySchemeJudgmentMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private StabilitySchemeParameterMapper stabilitySchemeParameterMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    private ExecuteFormDataMapper executeFormDataMapper;

    @Autowired
    private com.bmos.lims2.server.inspect.entry.service.InspectionEntryService inspectionEntryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateConclusionComponent(ConclusionComponentSaveDTO dto) {
        // 结论判定校验：若分析项已配置结论判定则禁止录入
        validateNoConclusionJudgmentConfigured(dto);

        ComponentListVO componentListVO = findComponentList(dto.getRecordVersionId(), dto.getRecordItemId(), dto.getComponentId());
        if (ObjectUtil.isNull(componentListVO)){
            throw new BmosException(LimsResponseCode.COMPONENT_NOT_EXIST);
        }
        List<ExecuteFormData> formDataList = this.generateFormData(componentListVO, dto);
        for (ExecuteFormData executeFormData : formDataList) {
            executeFormData.setRemark(dto.getRemark());
            executeFormData.setParameterId(dto.getParameterId());
            executeFormData.setParameterConfigId(dto.getParameterConfigId());
            executeFormData.setRecordItemId(dto.getRecordItemId());
        }
        executeFormDataService.saveResultsAndHandleRelationComponentData(formDataList, dto);

        // 同步更新 LIMS 侧判定结论（与 LIMS 录入一致逻辑）
        if (dto.getTaskId() != null && dto.getCompliant() != null) {
            updateLimsJudgment(dto.getTaskId(), dto.getCompliant());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdateConclusionComponent(List<ConclusionComponentSaveDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return;
        }
        for (ConclusionComponentSaveDTO dto : dtoList) {
            this.saveOrUpdateConclusionComponent(dto);
        }
    }

    /**
     * 按 LIMS 录入的规则更新任务的判定结果（状态/权限校验与 LIMS 一致）
     */
    private void updateLimsJudgment(Long taskId, Boolean judgedResult) {
        if (taskId == null || judgedResult == null) {
            return;
        }
        com.bmos.lims2.server.inspect.entry.dto.BatchJudgmentDTO dto = new com.bmos.lims2.server.inspect.entry.dto.BatchJudgmentDTO();
        dto.setId(taskId);
        dto.setJudgedResult(judgedResult);
        // 委托给 LIMS 侧接口，复用其状态/权限校验与历史记录逻辑
        inspectionEntryService.batchUpdateJudgment(java.util.Collections.singletonList(dto));
    }

    private List<ExecuteFormData> generateFormData(ComponentListVO componentListVO, ConclusionComponentSaveDTO dto) {
        List<ExecuteFormData> executeFormDataList = Lists.newArrayList();
        ElnEntryContext context = new ElnEntryContext();
        BusinessComponentBatchSaveDTO base = BeanUtil.toBean(dto, BusinessComponentBatchSaveDTO.class);
        context.setDto(base);
        context.setConclusionResult(dto.getCompliant());
        componentStrategyMap.get(componentListVO.getComponentType())
                .handleBusinessComponent(executeFormDataList, componentListVO, context);
        return executeFormDataList;
    }

    private ComponentListVO findComponentList(Long recordVersionId, Long recordItemId, Long componentId) {
        return recordComponentService.selectUsedComponentDetail(recordVersionId, recordItemId, componentId);
    }

    /**
     * 若当前分析项配置了结论判定（finalExpression 非空），则不允许录入结论组件
     */
    private void validateNoConclusionJudgmentConfigured(ConclusionComponentSaveDTO dto) {
        if (dto.getParameterConfigId() == null) {
            return;
        }
        boolean isStability = dto.getSchemeVersionId() != null
                && stabilitySchemeVersionMapper.selectById(dto.getSchemeVersionId()) != null;
        if (isStability) {
            List<com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO> stabilityJudgments =
                    stabilitySchemeJudgmentMapper.listByParamConfigId(dto.getParameterConfigId());
            if (stabilityJudgments != null && !stabilityJudgments.isEmpty()) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM,
                        String.format(ERR_CONCLUSION_JUDGMENT_CONFIGURED, String.valueOf(dto.getParameterConfigId())));
            }
        } else {
            List<InspectionSchemeJudgmentDTO> judgments =
                    inspectionSchemeJudgmentMapper.listByParameterConfigId(dto.getParameterConfigId());
            if (judgments != null && !judgments.isEmpty()) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM,
                        String.format(ERR_CONCLUSION_JUDGMENT_CONFIGURED, String.valueOf(dto.getParameterConfigId())));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAutoConclusionForEln(Long taskId, Boolean judgedResult) {
        if (taskId == null || judgedResult == null) {
            return;
        }
        Task task = taskMapper.selectById(taskId);
        if (task == null || task.getRecordVersionId() == null) {
            return;
        }
        // 先判断来源，再查对应的分析项配置表
        boolean isStability = task.getSchemeVersionId() != null
                && stabilitySchemeVersionMapper.selectById(task.getSchemeVersionId()) != null;
        // 查询记录项id
        Long recordItemId = null;
        if (task.getParameterConfigId() != null) {
            if (isStability) {
                com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter sp =
                        stabilitySchemeParameterMapper.selectById(task.getParameterConfigId());
                if (sp != null) {
                    recordItemId = sp.getRecordItemId();
                }
            } else {
                InspectionSchemeParameter p = inspectionSchemeParameterMapper.selectById(task.getParameterConfigId());
                if (p != null) {
                    recordItemId = p.getRecordItemId();
                }
            }
        }
        if (recordItemId == null) {
            return;
        }
        // 定位结论组件
        java.util.List<com.bmos.lims2.server.eln.record.entity.BatchRecordComponent> components =
                recordComponentService.selectByVersionAndItem(task.getRecordVersionId(), recordItemId);
        if (components == null || components.isEmpty()) {
            return;
        }
        com.bmos.lims2.server.eln.record.entity.BatchRecordComponent target = null;
        for (com.bmos.lims2.server.eln.record.entity.BatchRecordComponent c : components) {
            if (BusinessComponentTypeEnum.CONCLUSION.getValue().equals(c.getComponentType())) {
                target = c;
                break;
            }
        }
        if (target == null) {
            return;
        }
        // 构造上下文 & 元数据
        BusinessComponentBatchSaveDTO base = new BusinessComponentBatchSaveDTO();
        base.setInspectionOrderId(task.getInspectionOrderId());
        base.setTaskId(taskId);
        base.setRecordId(task.getRecordId());
        base.setItemId(task.getInspectItemId());
        base.setItemConfigId(task.getItemConfigId());
        InspectionOrder order = inspectionOrderMapper.selectById(task.getInspectionOrderId());
        if (order != null) {
            base.setBatchNo(order.getBatchNo());
        }
        base.setSchemeVersionId(task.getSchemeVersionId());
        if (task.getSchemeVersionId() != null) {
            if (isStability) {
                com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion sv =
                        stabilitySchemeVersionMapper.selectById(task.getSchemeVersionId());
                if (sv != null) {
                    base.setSchemeId(sv.getSchemeId());
                }
            } else {
                InspectionSchemeVersion v = inspectionSchemeVersionMapper.selectById(task.getSchemeVersionId());
                if (v != null) {
                    base.setSchemeId(v.getSchemeId());
                }
            }
        }
        base.setRecordItemId(recordItemId);
        base.setRecordVersionId(task.getRecordVersionId());
        base.setParameterId(task.getParameterId());
        base.setParameterConfigId(task.getParameterConfigId());

        ElnEntryContext ctx = new ElnEntryContext();
        ctx.setDto(base);
        ctx.setConclusionResult(judgedResult);

        // 通过策略生成并写入
        List<ExecuteFormData> list = new java.util.ArrayList<>();
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(task.getRecordVersionId(),
                        recordItemId, target.getId());
        componentStrategyMap.get(target.getComponentType()).handleBusinessComponent(list, componentListVO, ctx);
        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 判定：新增/修订/跳过
        ExecuteFormData latest = executeFormDataMapper.selectLatestByTaskIdAndFieldId(taskId, target.getFieldId());
        Long nextRev = executeFormDataMapper.getNextRev(task.getInspectionOrderId(), Collections.singleton(target.getFieldId()));
        List<ExecuteFormData> toInsert = new java.util.ArrayList<>();
        for (ExecuteFormData data : list) {
            data.setOperationTime(LocalDateTime.now());
            data.setOperationUser(StrUtil.emptyToDefault(data.getOperationUser(), SysUserHolder.getUser().getUserId()));
            data.setSystemCreate(Boolean.TRUE);
            data.setRev(nextRev);
            // 补齐关键关联字段
            data.setRecordId(task.getRecordId());
            data.setTaskId(task.getId());
            data.setItemId(task.getInspectItemId());
            data.setItemConfigId(task.getItemConfigId());
            if (latest == null || StrUtil.isBlank(latest.getValue())) {
                data.setOperationType(ExecuteFormDataType.SAVE.getValue());
                toInsert.add(data);
            } else if (!Objects.equals(StrUtil.emptyToDefault(latest.getValue(), ""), StrUtil.emptyToDefault(data.getValue(), ""))) {
                data.setOperationType(ExecuteFormDataType.MODIFY.getValue());
                data.setRemark("系统判定");
                toInsert.add(data);
            }  // 值未变化，跳过

        }
        if (!toInsert.isEmpty()) {
            executeFormDataService.insertBatch(toInsert);
        }
    }
}

