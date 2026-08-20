package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.enums.execute.AttachmentTypeEnum;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.dataset.handle.data.PlanAttachment;
import com.bmos.mes.service.dataset.util.options.DocxTakePhotoLegendReplaceOption;
import com.bmos.mes.service.execute.mapper.ExecuteAttachmentMapper;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.dto.ProcessRecordQueryDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PlanAttachmentBuilder {

    @Autowired
    private ExecuteAttachmentMapper executeAttachmentMapper;

    @Autowired
    UserFeign userFeign;

    @Autowired
    MinioFileClient minioFileClient;


    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcedureModelService procedureModelService;

    @Value("${minio.url}")
    private String minioUrl;

    public PlanAttachment build(List<Long> sortPlanIdList) {
        // 加载所有批次的拍照取证的附件
        List<ExecuteAttachment> attachments = executeAttachmentMapper.selectByPlanIdListAndType(sortPlanIdList, AttachmentTypeEnum.EVIDENCE_PICTURE);
        PlanAttachment planAttachment = new PlanAttachment();
        if (CollUtil.isEmpty(attachments)){
            return planAttachment;
        }
        // 根据图片的工艺id、工艺版本、记录项id、是否复用查询当前图片的步骤信息
        Map<Long, List<ProcedureStepModel>> procedureStepModelMap = this.getProcedureStepModelMap(attachments);
        // 根据步骤id查询所属的工序
        Map<Long, List<ProcedureModel>> procedureModelMap = this.getProcedureModelMap(procedureStepModelMap);
        // 根据工艺id以及工艺版本查询工艺
        Map<Long, Process> processModelMap = this.getProcessModelMap(attachments);

        Set<String> userSet = attachments.stream().map(ExecuteAttachment::getCreateBy).collect(Collectors.toSet());
        ResponseInfo<Map<String, FeignUserVO>> responseInfo = FeignUtils.handleRequest(userFeign::getByUserIds, userSet);
        Map<String, FeignUserVO> userVOMap = responseInfo.getData();
        String attachmentBucketName = minioFileClient.getBucketName(MinioBucket.BMOS_PRODUCT);
        for (ExecuteAttachment attachment : attachments) {
            String url = fillIpPhotoPath(attachment.getPath(), attachmentBucketName);
            DocxTakePhotoLegendReplaceOption.TakePhotoData takePhotoData = new DocxTakePhotoLegendReplaceOption.TakePhotoData();
            takePhotoData.setId(attachment.getId());
            takePhotoData.setImageUrl(url);
            takePhotoData.setProcessChangeNumber(attachment.getProcessChangeNumber());
            takePhotoData.setProcedureChangeNumber(attachment.getProcedureChangeNumber());
            takePhotoData.setCreator(Objects.nonNull(userVOMap.get(attachment.getCreateBy())) ? userVOMap.get(attachment.getCreateBy()).getUserName() : attachment.getCreateBy());
            takePhotoData.setTime(LocalDateTimeUtil.format(attachment.getCreateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            takePhotoData.setRemark(attachment.getRemark());
            if (planAttachment.containsKey(attachment.getProductPlanId())){
                planAttachment.get(attachment.getProductPlanId()).add(takePhotoData);
            }else {
                planAttachment.put(attachment.getProductPlanId(), Lists.newArrayList(takePhotoData));
            }
        }
        for (Long planId : planAttachment.keySet()) {
            List<DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoDataList = planAttachment.get(planId);
            // 填充图片的工序名称以及工艺名称
            List<DocxTakePhotoLegendReplaceOption.TakePhotoData> reallyTakePhotoDataList = this.fillPhotoProcessAnaProcedure(processModelMap, procedureModelMap, takePhotoDataList);
            planAttachment.put(planId, reallyTakePhotoDataList);
        }
        return planAttachment;
    }

    /**
     * 填充ip
     *
     * @param path
     * @return
     */
    private String fillIpPhotoPath(String path, String bucket) {
        String url;
        if (StrUtil.isNotEmpty(bucket)){
            url = StrUtil.format("{}/{}/{}", minioUrl, bucket, path);
        } else {
            url = StrUtil.format("{}/{}", minioUrl, path);
        }
        return url;
    }


    public Map<Long, Process> getProcessModelMap(List<ExecuteAttachment> executeAttachments) {
        if (CollUtil.isEmpty(executeAttachments)){
            return new HashMap<>();
        }
        Map<Long, List<Long>> processIdMap = new HashMap<>();
        for (ExecuteAttachment executeAttachment : executeAttachments) {
            if (Objects.isNull(executeAttachment.getProcessId())){
                continue;
            }
            List<Long> orDefault = processIdMap.getOrDefault(executeAttachment.getProcessId(), new ArrayList<>());
            orDefault.add(executeAttachment.getId());
            processIdMap.put(executeAttachment.getProcessId(), orDefault);
        }
        List<Process> processModels = processService.selectByIdList(Lists.newArrayList(processIdMap.keySet()));
        if (CollUtil.isEmpty(processModels)){
            return new HashMap<>();
        }
        Map<Long, Process> processModelMap = new HashMap<>();
        for (Process processModel : processModels) {
            List<Long> attachmentIdList = processIdMap.get(processModel.getId());
            for (Long attachmentId : attachmentIdList) {
                processModelMap.put(attachmentId, processModel);
            }
        }
        return processModelMap;
    }

    public Map<Long, List<ProcedureModel>> getProcedureModelMap(Map<Long, List<ProcedureStepModel>> procedureStepModelMap) {
        if (CollUtil.isEmpty(procedureStepModelMap)){
            return new HashMap<>();
        }
        List<Long> procedureModelIdList = new ArrayList<>();
        Map<Long, List<Long>> procedureModelIdMap = new HashMap<>();
        for (Long attachmentId : procedureStepModelMap.keySet()) {
            List<ProcedureStepModel> procedureStepModels = procedureStepModelMap.get(attachmentId);
            for (ProcedureStepModel procedureStepModel : procedureStepModels) {
                procedureModelIdList.add(procedureStepModel.getProcedureModelId());
                List<Long> orDefault = procedureModelIdMap.getOrDefault(procedureStepModel.getProcedureModelId(), new ArrayList<>());
                orDefault.add(attachmentId);
                procedureModelIdMap.put(procedureStepModel.getProcedureModelId(), orDefault);
            }
        }
        List<ProcedureModel> procedures = procedureModelService.getByIds(procedureModelIdList);
        if (CollUtil.isEmpty(procedures)){
            return new HashMap<>();
        }
        Map<Long, List<ProcedureModel>> procedureModelMap = new HashMap<>();
        for (ProcedureModel procedureModel : procedures) {
            List<Long> attachmentIds = procedureModelIdMap.get(procedureModel.getId());
            for (Long attachmentId : attachmentIds) {
                if (CollUtil.isEmpty(procedureModelMap.get(procedureModel.getId()))){
                    procedureModelMap.put(attachmentId, Lists.newArrayList(procedureModel));
                } else {
                    procedureModelMap.get(attachmentId).add(procedureModel);
                }
            }
        }
        return procedureModelMap;
    }

    public Map<Long, List<ProcedureStepModel>> getProcedureStepModelMap(List<ExecuteAttachment> executeAttachments) {
        if (CollUtil.isEmpty(executeAttachments)){
            return new HashMap<>();
        }
        List<ProcessRecordQueryDTO> queryDTOS = new ArrayList<>();
        Map<String, List<ExecuteAttachment>> executeAttachmentMap = new HashMap<>();
        for (ExecuteAttachment executeAttachment : executeAttachments) {
            if (Objects.isNull(executeAttachment.getProcessId()) || Objects.isNull(executeAttachment.getRecordVersionId())){
                continue;
            }
            ProcessRecordQueryDTO processRecordQueryDTO = new ProcessRecordQueryDTO();
            processRecordQueryDTO.setProcessId(executeAttachment.getProcessId());
            processRecordQueryDTO.setProcessVersion(executeAttachment.getProcessVersion());
            processRecordQueryDTO.setRecordId(executeAttachment.getRecordItemId());
            processRecordQueryDTO.setReuse(executeAttachment.getReuse());
            queryDTOS.add(processRecordQueryDTO);
            String key = StrUtil.format("{}-{}-{}-{}-{}", executeAttachment.getProcessId(), executeAttachment.getRecordVersionId(), executeAttachment.getProcessVersion(), executeAttachment.getReuse(), executeAttachment.getProcedureStepId());
            List<ExecuteAttachment> orDefault = executeAttachmentMap.getOrDefault(key, new ArrayList<>());
            orDefault.add(executeAttachment);
            executeAttachmentMap.put(key, orDefault);
        }
        List<ProcedureStepModel> procedureStepModels = procedureStepModelService.getByProcessAndRecord(queryDTOS);
        if (CollUtil.isEmpty(procedureStepModels)){
            return new HashMap<>();
        }
        Map<Long, List<ProcedureStepModel>> procedureStepModelMap = new HashMap<>();
        for (ProcedureStepModel procedureStepModel : procedureStepModels) {
            String key = StrUtil.format("{}-{}-{}-{}-{}", procedureStepModel.getProcessId(), procedureStepModel.getRecordVersionId(), procedureStepModel.getProcessVersion(), procedureStepModel.getReusable(), procedureStepModel.getReusable() ? 0 : procedureStepModel.getProcedureStepId());
            if (!executeAttachmentMap.containsKey(key)){
                continue;
            }
            List<ExecuteAttachment> curExecuteAttachments = executeAttachmentMap.get(key);
            for (ExecuteAttachment curExecuteAttachment : curExecuteAttachments) {
                List<ProcedureStepModel> orDefault = procedureStepModelMap.getOrDefault(curExecuteAttachment.getId(), new ArrayList<>());
                orDefault.add(procedureStepModel);
                procedureStepModelMap.put(curExecuteAttachment.getId(), orDefault);
            }
        }
        return procedureStepModelMap;
    }

    public List<DocxTakePhotoLegendReplaceOption.TakePhotoData> fillPhotoProcessAnaProcedure(Map<Long, Process> processModelMap,
                                                                                              Map<Long, List<ProcedureModel>> procedureModelMap,
                                                                                              List<DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoDataList) {
        List<DocxTakePhotoLegendReplaceOption.TakePhotoData> reallyTakePhotoDataList = new ArrayList<>();
        for (DocxTakePhotoLegendReplaceOption.TakePhotoData takePhotoData : takePhotoDataList) {
            if (Objects.isNull(takePhotoData.getId())){
                continue;
            }
            List<ProcedureModel> procedureModels = procedureModelMap.get(takePhotoData.getId());
            if (CollUtil.isEmpty(procedureModels)){
                continue;
            }
            Process process = processModelMap.get(takePhotoData.getId());
            for (ProcedureModel procedureModel : procedureModels) {
                takePhotoData.setProcessName(procedureModel.getName());
                DocxTakePhotoLegendReplaceOption.TakePhotoData data = new DocxTakePhotoLegendReplaceOption.TakePhotoData();
                data.setProcedureName(procedureModel.getName());
                data.setId(takePhotoData.getId());
                data.setImageUrl(takePhotoData.getImageUrl());
                data.setCreator(takePhotoData.getCreator());
                data.setTime(takePhotoData.getTime());
                data.setProcessChangeNumber(takePhotoData.getProcessChangeNumber());
                data.setProcedureChangeNumber(takePhotoData.getProcedureChangeNumber());
                data.setRemark(takePhotoData.getRemark());
                if (Objects.nonNull(process)){
                    data.setProcessName(process.getName());
                    takePhotoData.setProcessName(process.getName());
                }
                reallyTakePhotoDataList.add(data);
            }
        }
        return reallyTakePhotoDataList;

    }

}
