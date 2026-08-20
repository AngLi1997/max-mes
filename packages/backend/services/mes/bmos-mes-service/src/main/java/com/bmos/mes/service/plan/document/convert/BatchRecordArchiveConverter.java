package com.bmos.mes.service.plan.document.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveOperateTypeEnum;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveStatusEnum;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.model.ExecuteSubsidiaryRecord;
import com.bmos.mes.service.execute.vo.IntactFormDataVO;
import com.bmos.mes.service.plan.document.controller.vo.*;
import com.bmos.mes.service.plan.document.model.*;
import com.bmos.mes.service.plan.document.service.dto.ArchiveSaveLogDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.record.model.BatchRecordItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface BatchRecordArchiveConverter {
    
    BatchRecordArchiveConverter INSTANCE = Mappers.getMapper(BatchRecordArchiveConverter.class);

    List<BatchRecordArchiveLog> convert2DOList(List<ArchiveSaveLogDTO> saveLogDTO);

    default List<ArchiveHistoryVO> convert2VOList(List<BatchRecordArchiveLog> batchRecordArchiveLog){
        if (CollUtil.isEmpty(batchRecordArchiveLog)){
            return new ArrayList<>();
        }
        List<ArchiveHistoryVO> res = new ArrayList<>();
        for (BatchRecordArchiveLog batchRecordArchiveLog1 : batchRecordArchiveLog) {
            ArchiveHistoryVO archiveHistoryVO = new ArchiveHistoryVO();
            archiveHistoryVO.setId(archiveHistoryVO.getId());
            archiveHistoryVO.setOperationType(CommonEnum.getEnumByValue(BatchRecordArchiveOperateTypeEnum.class, batchRecordArchiveLog1.getOperateType()));
            archiveHistoryVO.setCreateBy(batchRecordArchiveLog1.getCreateBy());
            archiveHistoryVO.setCreateUsername(batchRecordArchiveLog1.getOperatorName());
            archiveHistoryVO.setCreateTime(batchRecordArchiveLog1.getOperateTime());
            archiveHistoryVO.setComment(batchRecordArchiveLog1.getAuditOpinion());
            archiveHistoryVO.setNodeName(batchRecordArchiveLog1.getElementName());
            archiveHistoryVO.setPath(batchRecordArchiveLog1.getPath());
            archiveHistoryVO.setRemark(batchRecordArchiveLog1.getRemark());
            res.add(archiveHistoryVO);
        }
        return res;
    }

    default BatchRecordVersionVO convert2BatchRecordVersionVO(Plan plan, BatchTemplateInfo templateInfo){
        BatchRecordVersionVO batchRecordVersionVO = new BatchRecordVersionVO();
        batchRecordVersionVO.setProductName(plan.getProductName());
        batchRecordVersionVO.setProductCode(plan.getProductMergeCode());
        batchRecordVersionVO.setProductSpecification(plan.getProductSpecification());
        batchRecordVersionVO.setProcessId(plan.getProcessId());
        batchRecordVersionVO.setProcessName(plan.getProcessName());
        batchRecordVersionVO.setBatchNo(plan.getBatchNo());
        batchRecordVersionVO.setStartTime(plan.getStartTime());
        batchRecordVersionVO.setEndTime(plan.getEndTime());
        batchRecordVersionVO.setProductId(plan.getProductId());
        if (Objects.nonNull(templateInfo)){
            batchRecordVersionVO.setTemplateName(templateInfo.getName());
        }
        return batchRecordVersionVO;
    }

    default List<BatchRecordArchiveVO> convert2BatchRecordArchiveVOList(List<BatchRecordArchive> batchRecordArchives){
        List<BatchRecordArchiveVO> res = new ArrayList<>();
        if (CollUtil.isEmpty(batchRecordArchives)){
            return res;
        }
        for (BatchRecordArchive batchRecordArchive : batchRecordArchives) {
            BatchRecordArchiveVO batchRecordArchiveVO = new BatchRecordArchiveVO();
            batchRecordArchiveVO.setArchiveId(batchRecordArchive.getId());
            batchRecordArchiveVO.setArchiveNo(batchRecordArchive.getArchiveNo());
            batchRecordArchiveVO.setPath(batchRecordArchive.getPath());
            batchRecordArchiveVO.setTemplateVersion(batchRecordArchive.getTemplateVersion());
            batchRecordArchiveVO.setOperatorName(batchRecordArchive.getOperatorName());
            batchRecordArchiveVO.setArchiveTime(batchRecordArchive.getArchiveTime());
            batchRecordArchiveVO.setEffectiveTime(batchRecordArchive.getEffectiveTime());
            batchRecordArchiveVO.setStatus(BatchRecordArchiveStatusEnum.getEnumByValue(batchRecordArchive.getStatus()));
            batchRecordArchiveVO.setRemark(batchRecordArchive.getRemark());
            batchRecordArchiveVO.setInstanceId(batchRecordArchive.getInstanceId());
            res.add(batchRecordArchiveVO);
        }
        return res;
    }

    default List<ArchiveAuditPageVO> convert2AuditPageVOList(List<TaskListResp> data, Map<String, BatchRecordArchive> batchRecordArchiveMap, Map<Long, Plan> planMap){
        if (CollUtil.isEmpty(data)){
            return new ArrayList<>();
        }
        List<ArchiveAuditPageVO> res = new ArrayList<>();
        for (TaskListResp taskListResp : data) {
            BatchRecordArchive batchRecordArchive = batchRecordArchiveMap.get(taskListResp.getProcessInstanceId());
            Plan plan = planMap.get(batchRecordArchive.getPlanId());
            ArchiveAuditPageVO archiveAuditPageVO = new ArchiveAuditPageVO();
            archiveAuditPageVO.setArchiveId(batchRecordArchive.getId());
            archiveAuditPageVO.setBatchNo(plan.getBatchNo());
            archiveAuditPageVO.setArchiveNo(batchRecordArchive.getArchiveNo());
            archiveAuditPageVO.setProductName(plan.getProductName());
            archiveAuditPageVO.setProductMergeCode(plan.getProductMergeCode());
            archiveAuditPageVO.setProcessName(plan.getProcessName());
            archiveAuditPageVO.setTemplateName(batchRecordArchive.getTemplateName());
            archiveAuditPageVO.setTaskId(taskListResp.getTaskId());
            archiveAuditPageVO.setTemplateVersion(batchRecordArchive.getTemplateVersion());
            archiveAuditPageVO.setArchiveTime(batchRecordArchive.getArchiveTime());
            archiveAuditPageVO.setFlowAuditStartByName(batchRecordArchive.getAuditorName());
            archiveAuditPageVO.setSendTime(taskListResp.getStartTime());
            archiveAuditPageVO.setPath(batchRecordArchive.getPath());
            archiveAuditPageVO.setDeploymentId(taskListResp.getDeploymentId());
            archiveAuditPageVO.setExecutionId(taskListResp.getExecutionId());
            archiveAuditPageVO.setNodeId(taskListResp.getElementKey());
            archiveAuditPageVO.setPayload(taskListResp.getPayload());
            archiveAuditPageVO.setInstanceId(taskListResp.getProcessInstanceId());
            res.add(archiveAuditPageVO);
        }
        return res;
    }


    default BatchRecordArchiveGenerate convert2ArchiveGenerateDO(Long templateVersionId, Plan plan){
        BatchRecordArchiveGenerate batchRecordArchiveGenerate = new BatchRecordArchiveGenerate();
        batchRecordArchiveGenerate.setBatchTemplateVersionId(templateVersionId);
        batchRecordArchiveGenerate.setPlanId(plan.getId());
        return batchRecordArchiveGenerate;
    }

    default List<RecordArchiveTemplateVersionVO> convert2TemplateVersionVO(List<BatchTemplateInfo> batchTemplateInfos, Long planId){
        List<RecordArchiveTemplateVersionVO> res = new ArrayList<>();
        if (CollUtil.isEmpty(batchTemplateInfos)){
            return res;
        }
        for (BatchTemplateInfo batchTemplateInfo : batchTemplateInfos) {
            RecordArchiveTemplateVersionVO recordArchiveTemplateVersionVO = new RecordArchiveTemplateVersionVO();
            recordArchiveTemplateVersionVO.setPlanId(planId);
            recordArchiveTemplateVersionVO.setTemplateInfoId(batchTemplateInfo.getId());
            recordArchiveTemplateVersionVO.setTemplateName(batchTemplateInfo.getName());
            res.add(recordArchiveTemplateVersionVO);
        }
        return res;
    }

    default List<IntactFormDataVO> convert2IntactFormDataVO(List<ExecuteRecordCopy> list, Map<Long, BatchRecordItem> itemMap, ExecuteSubsidiaryRecord record){
        return list.stream().map(e -> {
                    IntactFormDataVO intactFormDataVO = convert2IntactFormDataVO(e);
                    BatchRecordItem batchRecordItem = itemMap.get(intactFormDataVO.getRecordItemId());
                    intactFormDataVO.setFileContent(batchRecordItem.getFileContent());
                    intactFormDataVO.setHeaderContent(batchRecordItem.getDocxHeader());
                    intactFormDataVO.setFooterContent(batchRecordItem.getDocxFooter());
                    intactFormDataVO.setCopyVersion(e.getVersion());
                    intactFormDataVO.setProcedureStepModelId(record.getProcedureStepModelId());
                    return intactFormDataVO;
                })
                .collect(Collectors.toList());
    }

    IntactFormDataVO convert2IntactFormDataVO(ExecuteRecordCopy copy);
}
