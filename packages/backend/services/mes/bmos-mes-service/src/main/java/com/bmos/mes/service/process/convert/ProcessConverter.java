package com.bmos.mes.service.process.convert;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.StateEnum;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.mes.service.audit.dto.FlowProcessBindDTO;
import com.bmos.mes.service.audit.model.FlowAudit;
import com.bmos.mes.service.audit.model.FlowAuditProcess;
import com.bmos.mes.service.process.dto.modify.ProcessCopyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessSaveVersionDTO;
import com.bmos.mes.service.process.dto.save.ProcessSaveDTO;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.workflow.enums.WorkflowType;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface ProcessConverter {
    ProcessConverter INSTANCE = Mappers.getMapper(ProcessConverter.class);

    Process convert(ProcessSaveDTO dto);

    @Mapping(target = "id", source = "process.id")
    ProcessDetailVO convert(Process process, ProcessVersion processVersion);

    default ProcessDetailVO convert(Process process,
                                    ProcessVersion processVersion,
                                    List<ProcessBatchRecordRelation> relations) {
        ProcessDetailVO detail = convert(process, processVersion);
        detail.setBatchRecordItems(ProcessBatchRecordRelationConverter.INSTANCE.convertList(relations));
        return detail;
    }

    default CreateDeploymentCmd convertDeployment(ProcessSaveDTO dto, String version) {
        CreateDeploymentCmd cmd = new CreateDeploymentCmd();
        cmd.setName(dto.getName() + version);
        cmd.setCategory(WorkflowType.PROCEDURE.name());
        cmd.setMetaInfo(dto.getProcessModel());
        cmd.setCreateBy("1");
        return cmd;
    }

    @Mapping(target = "id", ignore = true)
    Process convert(ProcessCopyDTO dto);

    default ProcessVersion convertVersion(ProcessCopyDTO dto, Process process) {
        return ProcessVersion.builder()
                .processId(process.getId())
                .description(dto.getDescription())
                .productFormulaVersionId(dto.getProductFormulaVersionId())
                .state(StateEnum.OFF.getValue())
                .actionState(ActionStateEnum.EDIT.getValue())
                .effectDate(StrUtil.DASHED)
                .historyState(ActionStateEnum.EDIT.getValue())
                .version(dto.getVersion())
                .build();
    }

    default List<ProcessRelationVO> convertRelations(List<ProcessRelation> processRelations, List<ProcessRelationMaterial> relationMaterials) {
        Map<Long, List<Long>> dataMap =
                CollectionUtils.convertMultiMap(relationMaterials,
                        ProcessRelationMaterial::getProcessRelationId, ProcessRelationMaterial::getMaterialId);
        return processRelations.stream().map(e -> {
            ProcessRelationVO vo = new ProcessRelationVO();
            vo.setRelationProcessId(e.getRelationProcessId());
            vo.setMaterialIds(Optional.ofNullable(dataMap.get(e.getId())).orElse(Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());
    }

    List<ProcessListItemVO> convertList(List<Process> processes);

    default List<ProductProcessTreeNodeVO> convertTree(List<Process> processes,
                                                       List<ProductMaterialCategory> categories,
                                                       List<ProductMaterial> products) {
        List<ProductProcessTreeNodeVO> categoriesNodes = categories
                .stream()
                .map(e -> ProductProcessTreeNodeVO.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .parentId(e.getParentId())
                        .productCategoryFlag(true)
                        .createTime(e.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        List<ProductProcessTreeNodeVO> productNodes = products
                .stream()
                .map(e -> ProductProcessTreeNodeVO.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .parentId(e.getMaterialCategoryId())
                        .productFlag(true)
                        .createTime(e.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        List<ProductProcessTreeNodeVO> processNodes =
                processes.stream().map(e -> ProductProcessTreeNodeVO.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .parentId(e.getProductId())
                        .processFlag(true)
                        .createTime(e.getCreateTime())
                        .build()).collect(Collectors.toList());

        categoriesNodes.addAll(productNodes);
        categoriesNodes.addAll(processNodes);
        return TreeUtil.buildTree(categoriesNodes, true);
    }

    default List<ProcessTodoPageVO> convertList(List<TaskListResp> dataList, Map<Long, ProcessTodoPageVO> dataMap) {
        return dataList.stream()
                .filter(e -> dataMap.containsKey(Long.valueOf(e.getBusinessKey())))
                .map(data -> {
            ProcessTodoPageVO vo = dataMap.get(Long.valueOf(data.getBusinessKey()));
            vo.setNodeName(data.getElementName());
            vo.setStartBy(data.getProcessStartBy());
            vo.setProcessStartTime(data.getProcessStartTime());
            vo.setPayload(data.getPayload());
            vo.setProcessInstanceId(data.getProcessInstanceId());
            vo.setDeploymentId(data.getDeploymentId());
            vo.setTaskId(data.getTaskId());
            vo.setNodeId(data.getElementKey());
            vo.setExecutionId(data.getExecutionId());
            return vo;
        }).collect(Collectors.toList());
    }

    default List<ProcessProductionLine> convertToProcessProductionLine(ProcessVersion processVersion, List<Long> productionLineIds){
        return productionLineIds.stream().map(e -> {
            ProcessProductionLine line = new ProcessProductionLine();
            line.setProductionLineId(e);
            line.setProcessId(processVersion.getProcessId());
            line.setProcessVersion(processVersion.getVersion());
            line.setProcessVersionId(processVersion.getId());
            return line;
        }).collect(Collectors.toList());
    }

    default List<FlowAuditProcess> convert2AuditProcessList(FlowProcessBindDTO dto, FlowAudit flowAudit){
        return dto.getProcessIds().stream().map(e -> {
            FlowAuditProcess auditProcess = new FlowAuditProcess();
            auditProcess.setProcessId(e);
            auditProcess.setCode(flowAudit.getCode());
            auditProcess.setCategoryCode(flowAudit.getCategoryCode());
            return auditProcess;
        }).collect(Collectors.toList());
    }

    ProcessSaveVersionDTO convert2SaveVersionDTO(ProcessCopyDTO dto);
}
