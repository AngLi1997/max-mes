package com.bmos.mes.service.process.service.impl.copy;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.service.process.constant.ProcessConstant;
import com.bmos.mes.service.process.dto.RelationBatchRecordItemDTO;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CopyProcedureStep extends CopyProcedure {

    /**
     * 旧工步id
     */
    private Long oldProcedureStepId;

    /**
     * 工步id
     */
    private Long procedureStepId;

    /**
     * 工步模型id
     */
    private Long stepModelId;

    /**
     * 旧工步模型id
     */
    private Long oldStepModelId;

    private String nodeId;

    /**
     * 工步名称
     */
    private String stepName;

    /**
     * 节点功能
     */
    private String nodeFunction;

    /**
     * 是否可复用
     */
    private Boolean reusable;

    /**
     * 记录项Id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 单位
     */
    private String timeUnit;

    /**
     * 工步班组
     */
    private List<ProcedureStepRole> stepGroupList = new ArrayList<>();

    /**
     * 操规
     */
    private List<ProcedureStepSop> stepSopList = new ArrayList<>();

    /**
     * 条件
     */
    private List<ProcedureExpression> expressionList = new ArrayList<>();

    /**
     * 表达式
     */
    private List<ProcedureCondition> procedureConditionList = new ArrayList<>();

    /**
     * 组件配置
     */
    private List<ProcedureStepConfig> procedureStepConfigs = new ArrayList<>();

    /**
     * 步骤
     */
    private StepTaskTypeEnum stepType;

    /**
     * 工步顺序
     */
    private Integer stepSort;


    /**
     * 构建工步关联配置
     *
     * @param copyContext
     */
    public void buildProcedureStepRelatedInfo(CopyContext copyContext) {
        // 工步班组
        handleStepGroup(copyContext);
        // 操规
        handleStepSop(copyContext);
        // 条件和表达式
        handleExpression(copyContext);
        // 组件配置
        handleStepConfig(copyContext);
        // 归档顺序
        handleRecordOrder(copyContext);
    }

    /**
     * 处理归档顺序中的相关id
     * @param copyContext
     */
    private void handleRecordOrder(CopyContext copyContext) {
        ProcessRecordOrder order = copyContext.getOrderMap().get(this.recordItemId + "-" + this.oldStepModelId);
        if (ObjectUtil.isNull(order)) {
            return;
        }
        order.setId(IdUtils.getSnowflake());
        order.setProcessId(getProcessId());
        order.setProcessVersion(getProcessVersion());
        order.setProcessVersionId(getProcessVersionId());
        BatchRecordVersion version = copyContext.getVersionMap().get(order.getRecordVersionId());
        // 归档顺序中的recordVersionId处理
        if (ObjectUtil.isNotEmpty(version)) {
            RelationBatchRecordItemDTO item = copyContext.getItemMap().get(version.getRecordId());
            order.setRecordVersionId(item.getBatchRecordVersionId());
        }
        // 步骤id处理
        if (!order.getReusable()) {
            order.setProcedureStepModelId(stepModelId);
        }
    }

    /**
     * 处理组件配置
     * @param copyContext
     */
    private void handleStepConfig(CopyContext copyContext) {
        if (ProcedureStepNodeFunctionEnum.changeTeamFlag(nodeFunction)) {
            return;
        }
        this.procedureStepConfigs = copyContext.getStepConfigList().stream().filter(e -> this.reusable ?
                (e.getRecordItemId().equals(this.recordItemId) && e.getReuse()) :
                e.getProcedureStepModelId().equals(oldStepModelId)).collect(Collectors.toList());
        this.procedureStepConfigs.forEach(e -> {
            e.setProcedureStepId(this.procedureStepId);
            e.setProcedureStepModelId(reusable ? ProcessConstant.REUSE_PROCEDURE_STEP_ID : this.stepModelId);
            e.setId(null);
            e.setProcessId(getProcessId());
            e.setVersion(getProcessVersion());
            BatchRecordVersion version = copyContext.getVersionMap().get(e.getRecordVersionId());
            if (ObjectUtil.isNotEmpty(version)) {
                RelationBatchRecordItemDTO item = copyContext.getItemMap().get(version.getRecordId());
                e.setRecordVersionId(item.getBatchRecordVersionId());
            }
        });
    }

    /**
     * 处理表达式
     * @param copyContext
     */
    private void handleExpression(CopyContext copyContext) {
        List<ProcedureExpression> procedureExpressions = copyContext.getExpressionMap().get(oldStepModelId);
        if (CollUtil.isEmpty(procedureExpressions)) {
            return;
        }
        procedureExpressions.forEach(expression -> {
            Long expressionId = IdUtils.getSnowflake();
            expression.setId(expressionId);
            expression.setProcedureModelId(this.getProcedureModelId());
            expression.setProcedureStepModelId(this.stepModelId);
            expression.setNodeId(nodeId);
            List<ProcedureCondition> conditionsList = expression.getConditions();
            this.expressionList.add(expression);
            conditionsList.forEach(item -> {
                item.setId(IdUtils.getSnowflake());
                item.setExpressionId(expressionId);
                item.setProcedureStepModelId(stepModelId);
                item.setConditionNodeType(item.getConditionNodeType());
            });
            this.procedureConditionList.addAll(conditionsList);
        });
    }

    /**
     * 处理工步sop信息
     * @param copyContext
     */
    private void handleStepSop(CopyContext copyContext) {
        if (CollUtil.isEmpty(copyContext.getSopMap().get(oldStepModelId))) {
            return;
        }
        this.stepSopList = copyContext.getSopMap().get(oldStepModelId).stream()
                .map(e -> {
                    ProcedureStepSop procedureStepSop = new ProcedureStepSop();
                    procedureStepSop.setStepModelId(stepModelId);
                    procedureStepSop.setOperationSopId(e.getOperationSopId());
                    return procedureStepSop;
                })
                .collect(Collectors.toList());
    }

    /**
     * 处理工步班组信息
     * @param copyContext
     */
    private void handleStepGroup(CopyContext copyContext) {
        List<ProcedureStepRole> procedureStepRoles = copyContext.getStepGroupMap().get(oldStepModelId);
        if (CollUtil.isEmpty(procedureStepRoles)) {
            return;
        }
        this.stepGroupList = procedureStepRoles.stream()
                .map(e -> ProcedureStepRole.builder().procedureStepId(stepModelId).roleId(e.getRoleId()).build())
                .collect(Collectors.toList());
    }


    /**
     * 将当前复制对象转换为工步对象
     * @return
     */
    public ProcedureStep convert2ProcedureStep() {
        ProcedureStep procedureStep = new ProcedureStep();
        procedureStep.setProcessId(getProcessId());
        procedureStep.setProcedureId(getProcedureId());
        procedureStep.setName(stepName);
        procedureStep.setType(stepType);
        procedureStep.setId(procedureStepId);
        return procedureStep;
    }

    /**
     * 将当前复制对象转换为工步模型对象
     * @return
     */
    public ProcedureStepModel convert2ProcedureStepModel() {
        ProcedureStepModel stepModel = new ProcedureStepModel();
        BeanUtil.copyProperties(this, stepModel);
        stepModel.setId(stepModelId);
        stepModel.setName(stepName);
        stepModel.setSort(stepSort);
        return stepModel;
    }

    /**
     * 复制工步模型属性
     * @param procedureStepModel
     */
    public void copyStepModel(ProcedureStepModel procedureStepModel, CopyContext copyContext) {
        this.stepName = procedureStepModel.getName();
        this.nodeId = procedureStepModel.getNodeId();
        this.oldStepModelId = procedureStepModel.getId();
        this.oldProcedureStepId = procedureStepModel.getProcedureStepId();
        this.stepName = procedureStepModel.getName();
        this.nodeFunction = procedureStepModel.getNodeFunction();
        this.reusable = procedureStepModel.getReusable();
        this.recordItemId = procedureStepModel.getRecordItemId();
        this.duration = procedureStepModel.getDuration();
        this.timeUnit = procedureStepModel.getTimeUnit();
        this.stepType = procedureStepModel.getStepType();
        this.stepSort = procedureStepModel.getSort();
        this.procedureStepId = copyContext.isCopy() ? IdUtils.getSnowflake() : procedureStepModel.getProcedureStepId();
        BatchRecordVersion version = copyContext.getVersionMap().get(procedureStepModel.getRecordVersionId());
        if (ObjectUtil.isNotEmpty(version)) {
            RelationBatchRecordItemDTO item = copyContext.getItemMap().get(version.getRecordId());
            this.recordVersionId = item.getBatchRecordVersionId();
        }
    }
}
