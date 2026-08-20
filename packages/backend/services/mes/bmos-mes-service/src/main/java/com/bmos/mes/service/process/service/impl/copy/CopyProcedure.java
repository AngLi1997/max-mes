package com.bmos.mes.service.process.service.impl.copy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.service.process.convert.Task.ProcedureConditionConverter;
import com.bmos.mes.service.process.convert.Task.ProcedureExpressionConverter;
import com.bmos.mes.service.process.dto.ProcedureCopyDTO;
import com.bmos.mes.service.process.dto.task.ConditionSaveDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class CopyProcedure extends CopyProcessVersion {

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序模型id
     */
    private Long procedureModelId;

    /**
     * 旧工序模型id
     */
    private Long oldProcedureModelId;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 阶段编码
     */
    private String stageCode;
    /**
     * 负责人
     */
    private Long principal;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 时长
     */
    private Long duration;

    /**
     * 时长单位
     */
    private String timeUnit;

    /**
     * 流程节点id
     */
    private String nodeId;

    /**
     * 流程模型id
     */
    private String procedureProcessModelId;

    /**
     * 工序房间
     */
    private List<ProcedureModelRoom> rooms;

    /**
     * 工序表达式
     */
    private ProcedureExpression expression;

    /**
     * 工序条件
     */
    private List<ProcedureCondition> condition = new ArrayList<>();

    /**
     * 工序物料
     */
    private List<ProcedureModelMaterial> materials;

    /**
     * 工序班组
     */
    private List<ProcedureModelGroup> groups;

    /**
     * 拷贝工步列表
     */
    private List<CopyProcedureStep> copyProcedureStepList;

    /**
     * 将当前复制对象转换为工序对象
     * @return
     */
    public Procedure convert2Procedure() {
        Procedure procedure = new Procedure();
        procedure.setId(procedureId);
        procedure.setProcessId(getProcessId());
        procedure.setName(procedureName);
        return procedure;
    }

    /**
     * 将当前复制对象转换为工序模型对象
     * @return
     */
    public ProcedureModel convert2ProcedureModel() {
        ProcedureModel procedureModel = new ProcedureModel();
        BeanUtil.copyProperties(this, procedureModel);
        procedureModel.setName(procedureName);
        procedureModel.setId(procedureModelId);
        procedureModel.setProcessModelId(procedureProcessModelId);
        return procedureModel;
    }

    /**
     * 构建完整的复制工序
     * 处理工序相关关联信息以及下层的工步及其关联信息
     * @param copyContext
     * @param dto
     */
    public void buildProcedure(CopyContext copyContext, ProcedureCopyDTO dto) {
        // 工序基础信息
        handleProcedureInfo(dto, copyContext);
        // 工序房间
        this.rooms = buildRoomRelation(dto.getRoomIdList());
        // 工序物料
        this.materials = buildMaterialRelation(dto.getFormulaMaterialIdList());
        // 工序班组
        this.groups = buildGroupRelation(dto.getGroupIds());
        // 复制工步
        copyProcedureStep(copyContext.getStepModelMap().get(dto.getId()), copyContext);
        // 工序表达式
        if (ObjectUtil.isNotEmpty(dto.getCompleteCondition())) {
            this.expression = buildProcedureExpression(dto.getCompleteCondition());
            // 工序条件
            this.condition = buildProcedureCondition(dto.getCompleteCondition().getConditionList());
        }
    }

    /**
     * 复制工序基础信息
     * @param dto
     * @param copyContext
     */
    private void handleProcedureInfo(ProcedureCopyDTO dto, CopyContext copyContext) {
        this.procedureName = dto.getName();
        this.stageCode = dto.getStageCode();
        this.sort = ObjectUtil.isNull(dto.getSort()) ? copyContext.getSort().incrementAndGet() : dto.getSort();
        this.principal = dto.getPrincipal();
        this.timeUnit = dto.getTimeUnit();
        this.duration = dto.getDuration();
        this.nodeId = dto.getNodeId();
        this.procedureProcessModelId = dto.getProcessModelId();
        this.setOldProcedureModelId(dto.getId());
        if (ObjectUtil.isNull(dto.getProcedureId()) || copyContext.isCopy()) {
            this.procedureId = IdUtils.getSnowflake();
        } else {
            this.procedureId = dto.getProcedureId();
        }

    }

    /**
     * 处理工序班组关联
     * @param groupIds
     * @return
     */
    private List<ProcedureModelGroup> buildGroupRelation(List<Long> groupIds) {
        if (CollUtil.isEmpty(groupIds)) {
            return new ArrayList<>();
        }
        return groupIds.stream().map(groupId -> {
            ProcedureModelGroup group = new ProcedureModelGroup();
            group.setProcedureModelId(this.getProcedureModelId());
            group.setGroupId(groupId);
            return group;
        }).collect(Collectors.toList());
    }

    /**
     * 处理工序物料关联
     * @param formulaMaterialIdList
     * @return
     */
    private List<ProcedureModelMaterial> buildMaterialRelation(List<Long> formulaMaterialIdList) {
        if (CollUtil.isEmpty(formulaMaterialIdList)) {
            return new ArrayList<>();
        }
        return formulaMaterialIdList.stream().map(formulaMaterialId -> {
            ProcedureModelMaterial material = new ProcedureModelMaterial();
            material.setProcedureModelId(this.getProcedureModelId());
            material.setProductFormulaMaterialId(formulaMaterialId);
            return material;
        }).collect(Collectors.toList());
    }

    /**
     * 处理工序房间关联
     * @param roomIdList
     * @return
     */
    private List<ProcedureModelRoom> buildRoomRelation(List<String> roomIdList) {
        if (CollUtil.isEmpty(roomIdList)) {
            return new ArrayList<>();
        }
        return roomIdList.stream().map(roomIdPath -> {
            ProcedureModelRoom room = new ProcedureModelRoom();
            room.setProcedureModelId(this.getProcedureModelId());
            room.setRoomIdPath(roomIdPath);
            List<String> pathList = StrUtil.split(roomIdPath, StrUtil.DASHED);
            room.setRoomId(Long.valueOf(CollUtil.getLast(pathList)));
            return room;
        }).collect(Collectors.toList());
    }

    /**
     * 初始化复制工步 将工艺工序相关信息复制
     * @return
     */
    public CopyProcedureStep initCopyProcedureStep() {
        CopyProcedureStep copyProcedureStep = new CopyProcedureStep();
        copyProcedureStep.setStepModelId(IdUtils.getSnowflake());
        copyProcedureStep.setProcessId(getProcessId());
        copyProcedureStep.setProcessVersionId(getProcessVersionId());
        copyProcedureStep.setProcessVersion(getProcessVersion());
        copyProcedureStep.setProcedureId(procedureId);
        copyProcedureStep.setProcedureModelId(procedureModelId);
        copyProcedureStep.setProcedureName(procedureName);
        return copyProcedureStep;
    }

    /**
     * 复制处理当前工序下的工步信息
     * @param procedureStepModels
     * @param copyContext
     */
    public void copyProcedureStep(List<ProcedureStepModel> procedureStepModels, CopyContext copyContext) {
        this.copyProcedureStepList = Optional.ofNullable(procedureStepModels).map(models -> models.stream()
                .map(e -> {
                    // 初始化工步
                    CopyProcedureStep copyProcedureStep = initCopyProcedureStep();
                    // 复制工步基础信息
                    copyProcedureStep.copyStepModel(e, copyContext);
                    // 处理工步关联数据
                    copyProcedureStep.buildProcedureStepRelatedInfo(copyContext);
                    return copyProcedureStep;
                })
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }


    /**
     * 处理工序表达式
     * @param completeCondition
     * @return
     */
    private ProcedureExpression buildProcedureExpression(ExpressionSaveDTO completeCondition){
        ProcedureExpression expression = ProcedureExpressionConverter.INSTANCE.convertToExpression(completeCondition);
        expression.setId(IdUtils.getSnowflake());
        expression.setProcedureModelId(procedureModelId);
        expression.setExpressionType(ExpressionTypeEnum.COMPLETE_CONDITION.getValue());
        expression.setExpressionNodeType(NodeTypeEnum.PROCEDURE.getValue());
        return expression;
    }

    /**
     * 处理工序条件
     * @param conditionList
     * @return
     */
    private List<ProcedureCondition> buildProcedureCondition(List<ConditionSaveDTO> conditionList){
        conditionList.forEach(e -> e.setId(IdUtils.getSnowflake()));
        List<ProcedureCondition> conditions = ProcedureConditionConverter.INSTANCE.convertConditionList(conditionList,
                this.expression.getId(), null, NodeTypeEnum.PROCEDURE.getValue());
        Map<Long, CopyProcedureStep> stepMap = CollectionUtils.convertMap(copyProcedureStepList,
                CopyProcedureStep::getOldStepModelId);
        conditions.forEach(item->{
            ConditionDetailVO detailVO = JsonUtils.parseObject(item.getConditionDetails(), ConditionDetailVO.class);
            CopyProcedureStep copyProcedureStep = stepMap.get(detailVO.getTaskNodeId());
            detailVO.setTaskNodeId(copyProcedureStep.getStepModelId());
            detailVO.setProcedureId(procedureModelId);
            item.setConditionDetails(JsonUtils.toJsonString(detailVO));
        });
        return conditions;
    }

}
