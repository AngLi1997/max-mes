package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.dataset.GenerateSourceEnum;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.component.ComponentCheckBoxDetail;
import com.bmos.mes.common.model.execute.AcquisitionPictureExtInfo;
import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import com.bmos.mes.service.dataset.handle.data.AssemblePrepareData;
import com.bmos.mes.service.dataset.handle.data.ExecuteFormLoadingData;
import com.bmos.mes.service.dataset.handle.data.PlanBatchDocumentData;
import com.bmos.mes.service.dataset.util.options.DocxImageCaptionReplaceOption;
import com.bmos.mes.service.dataset.util.options.DocxTakePhotoLegendReplaceOption;
import com.bmos.mes.service.execute.mapper.ExecuteAttachmentMapper;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.dto.ProcessVersionQueryDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.record.dto.ComponentDetailDTO;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentDetailVO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@lombok.extern.slf4j.Slf4j
@Component
@Slf4j
public class AssemblePrepareBuilder {

    @Autowired
    private FormLoadingBuilder formLoadingBuilder;

    @Autowired
    DataSetProcessBuilder dataSetProcessBuilder;

    @Autowired
    private PlanCopyVersionBuilder planCopyVersionBuilder;

    @Autowired
    PlanAttachmentBuilder planAttachmentBuilder;

    @Autowired
    UserFeign userFeign;

    @Value("${minio.url}")
    private String minioUrl;

    @Autowired
    ExecuteAttachmentMapper attachmentMapper;

    @Autowired
    ProcessVersionService processVersionService;

    @Autowired
    BatchRecordComponentService batchRecordComponentService;

    @Autowired
    PlanService planService;

    public AssemblePrepareData build(PlanBatchDocumentData documentData){
        AssemblePrepareData assemblePrepareData = convertBaseData(documentData);
        // 所有的生产计划的数据
        assemblePrepareData.setPlanList(planService.getByIds(assemblePrepareData.getSortPlanIdList()));
        // 构建数据集与工艺之间的绑定关系
        assemblePrepareData.setDataSetProcess(dataSetProcessBuilder.build(formLoadingBuilder.getDatasetPoints(), documentData.getDynamicRenderingData(), documentData.getPlanId()));
        // 构建作业数据以及作业数据与数据点之间的关系
        assemblePrepareData.setExecuteFormLoadingData(formLoadingBuilder.build(documentData.getSortPlanIdList(), assemblePrepareData.getDataSetProcess()));
        // 构建所有批次下的所有复制版本
        assemblePrepareData.setPlanCopyVersion(planCopyVersionBuilder.build(formLoadingBuilder.getDatasetPoints(), documentData.getSortPlanIdList()));
        // 构建附件信息
        assemblePrepareData.setPlanAttachment(planAttachmentBuilder.build(documentData.getSortPlanIdList()));
        // 处理所有的value
        // 对作业中的value进行特殊处理，有一些存的是id，根据componentType获取具体的值
        fixValue(assemblePrepareData.getExecuteFormLoadingData(), documentData.getSourceEnum());
        return assemblePrepareData;
    }

    private AssemblePrepareData convertBaseData(PlanBatchDocumentData documentData) {
        AssemblePrepareData assemblePrepareData = new AssemblePrepareData();
        assemblePrepareData.setPlanId(documentData.getPlanId());
        assemblePrepareData.setRenderTemplateDataList(documentData.getRenderTemplateDataList());
        assemblePrepareData.setPlanLoadingData(documentData.getPlanLoadingData());
        assemblePrepareData.setSourceEnum(documentData.getSourceEnum());
        assemblePrepareData.setSortPlanIdList(documentData.getSortPlanIdList());
        assemblePrepareData.setDynamicRenderingData(documentData.getDynamicRenderingData());
        return assemblePrepareData;

    }

    private void fixValue(List<ExecuteFormLoadingData> clearExecuteFormLoadingData, GenerateSourceEnum sourceEnum) {
        List<Long> valueIdList = new ArrayList<>();
        Map<ExecuteFormLoadingData, List<Long>> specialMap = new HashMap<>();
        Set<String> processIdAndVersionSet = new HashSet<>();
        List<ProcessVersionQueryDTO> processVersionQueryDTOS = new ArrayList<>();
        List<ExecuteFormLoadingData> checkBoxFormData = new ArrayList<>();
        for (ExecuteFormLoadingData executeFormLoadingData : clearExecuteFormLoadingData) {
            if (StrUtil.isEmpty(executeFormLoadingData.getValue())) {
                continue;
            }
            //时间戳
            if (Objects.nonNull(executeFormLoadingData.getFormDataExtInfo())
                    && Objects.nonNull(executeFormLoadingData.getFormDataExtInfo().getTimeStamp())
                    && GenerateSourceEnum.BATCH_ISSUE.equals(sourceEnum)) {
                // 若是批签发 所有时间值都为时间戳
                executeFormLoadingData.setValue(executeFormLoadingData.getFormDataExtInfo().getTimeStamp());
            }
            // 拍照组件
            if (BasicComponentTypeEnum.PHOTO.getValue().equals(executeFormLoadingData.getComponentType())) {
                String[] split = executeFormLoadingData.getValue().split(StrUtil.COMMA);
                boolean digit = true;
                for (int i = 0; i < split.length; i++) {
                    if (!split[i].matches("-?\\d+(\\.\\d+)?")){
                        digit = false;
                        break;
                    }
                }
                if (digit){
                    List<Long> collect = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
                    specialMap.put(executeFormLoadingData, collect);
                    valueIdList.addAll(collect);
                }
            }
            // 签名组件处理
            if (BusinessComponentTypeEnum.HANDLE_SUBMIT_SIGN.getValue().equals(executeFormLoadingData.getComponentType())
                    || BusinessComponentTypeEnum.HANDLE_REVIEW_SIGN.getValue().equals(executeFormLoadingData.getComponentType())){
                if (!executeFormLoadingData.isEmpty()) {
                    String url = fillIpPhotoPath(executeFormLoadingData.getValue(), StrUtil.EMPTY);
                    executeFormLoadingData.setValue(url);
                }
            }
            // 选择框
            if (BasicComponentTypeEnum.RADIO.getValue().equals(executeFormLoadingData.getComponentType())
                    || BasicComponentTypeEnum.CHECKBOX.getValue().equals(executeFormLoadingData.getComponentType())){
                String key = StrUtil.format("{}{}", executeFormLoadingData.getProcessId(), executeFormLoadingData.getProcessVersion());
                if (!processIdAndVersionSet.contains(key)){
                    ProcessVersionQueryDTO processVersionQueryDTO = new ProcessVersionQueryDTO().setVersion(executeFormLoadingData.getProcessVersion()).setProcessId(executeFormLoadingData.getProcessId());
                    processVersionQueryDTOS.add(processVersionQueryDTO);
                    processIdAndVersionSet.add(key);
                }
                checkBoxFormData.add(executeFormLoadingData);
            }
            // 数采绘图组件
            if (BusinessComponentTypeEnum.EQUIPMENT_DATA_DRAW.getValue().equals(executeFormLoadingData.getComponentType())) {
                executeFormLoadingData.setValue(acquisitionDrawToImageCaptionValue(executeFormLoadingData.getValue()));
            }
        }
        if (CollUtil.isNotEmpty(valueIdList)){
            List<ExecuteAttachment> executeAttachments = attachmentMapper.selectBatchIds(valueIdList);
            // 根据图片的工艺id、工艺版本、记录项id、是否复用查询当前图片的步骤信息
            Map<Long, List<ProcedureStepModel>> procedureStepModelMap = planAttachmentBuilder.getProcedureStepModelMap(executeAttachments);
            // 根据步骤id查询所属的工序
            Map<Long, List<ProcedureModel>> procedureModelMap = planAttachmentBuilder.getProcedureModelMap(procedureStepModelMap);
            // 根据工艺id以及工艺版本查询工艺
            Map<Long, Process> processModelMap = planAttachmentBuilder.getProcessModelMap(executeAttachments);
            Set<String> userIdList = new HashSet<>();
            Map<Long, ExecuteAttachment> map = executeAttachments.stream().map(e-> {
                userIdList.add(e.getCreateBy());
                return e;
            }).collect(Collectors.toMap(ExecuteAttachment::getId, executeAttachment -> executeAttachment));
            Map<String, FeignUserVO> userVOMap = FeignUtils.handleRequest(userFeign::getByUserIds, userIdList).getData();
            for (ExecuteFormLoadingData executeFormLoadingData : specialMap.keySet()) {
                List<DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoDataList = new ArrayList<>();
                List<Long> idList = specialMap.get(executeFormLoadingData);
                for (Long id : idList) {
                    ExecuteAttachment executeAttachment = map.get(id);
                    if (Objects.isNull(executeAttachment)) {
                        continue;
                    }
                    String url = fillIpPhotoPath(executeAttachment.getPath(), StrUtil.EMPTY);
                    DocxTakePhotoLegendReplaceOption.TakePhotoData takePhotoData = new DocxTakePhotoLegendReplaceOption.TakePhotoData();
                    takePhotoData.setId(id);
                    takePhotoData.setImageUrl(url);
                    takePhotoData.setCreator(Objects.nonNull(userVOMap.get(executeAttachment.getCreateBy())) ? userVOMap.get(executeAttachment.getCreateBy()).getUserName() : executeAttachment.getCreateBy());
                    takePhotoData.setTime(LocalDateTimeUtil.format(executeAttachment.getCreateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    takePhotoData.setRemark(executeAttachment.getRemark());
                    takePhotoDataList.add(takePhotoData);
                }
                // 填充图片的工序名称以及工艺名称
                List<DocxTakePhotoLegendReplaceOption.TakePhotoData> reallyTakePhotoDataList = planAttachmentBuilder.fillPhotoProcessAnaProcedure(processModelMap, procedureModelMap, takePhotoDataList);
                executeFormLoadingData.setValue(JSON.toJSONString(reallyTakePhotoDataList));
                executeFormLoadingData.setType(DatasetTransValueDataType.TAKE_PHOTO);
            }
        }
        if (CollUtil.isNotEmpty(checkBoxFormData)){
            // 查询选择框中的选项
            List<ProcessVersion> processVersionList = processVersionService.selectByQueryDTOLIst(processVersionQueryDTOS);
            Map<String, ProcessVersion> processVersionMap = processVersionList.stream().collect(Collectors.toMap(processVersion -> StrUtil.format("{}{}", processVersion.getProcessId(), processVersion.getVersion()), processVersion -> processVersion));
            List<ComponentDetailDTO> componentDetailDTOS = new ArrayList<>();
            Set<String> set = new HashSet<>();
            for (ExecuteFormLoadingData checkBoxFormDatum : checkBoxFormData) {
                String key = StrUtil.format("{}{}{}{}", checkBoxFormDatum.getProcessId(), checkBoxFormDatum.getProcessVersion(), checkBoxFormDatum.getRecordItemId(), checkBoxFormDatum.getFieldId());
                if (set.contains(key)){
                    continue;
                }
                set.add(key);
                String processVersionKey = StrUtil.format("{}{}", checkBoxFormDatum.getProcessId(), checkBoxFormDatum.getProcessVersion());
                ProcessVersion processVersion = processVersionMap.get(processVersionKey);
                componentDetailDTOS.add(new ComponentDetailDTO().setFieldId(checkBoxFormDatum.getFieldId()).setRecordItemId(checkBoxFormDatum.getRecordItemId()).setProcessVersionId(processVersion.getId()).setFieldId(checkBoxFormDatum.getFieldId()));
            }
            List<ComponentDetailVO> componentDetailVOS = batchRecordComponentService.selectByComponentDetailDTOS(componentDetailDTOS);
            Map<String, ComponentDetailVO> componentDetailMap = new HashMap<>();
            for (ComponentDetailVO componentDetailVO : componentDetailVOS) {
                String key = StrUtil.format("{}{}{}", componentDetailVO.getProcessVersionId(), componentDetailVO.getRecordItemId(), componentDetailVO.getFieldId());
                componentDetailMap.put(key, componentDetailVO);
            }
            for (ExecuteFormLoadingData checkBoxFormDatum : checkBoxFormData) {
                String key = StrUtil.format("{}{}{}",
                        processVersionMap.get(StrUtil.format("{}{}", checkBoxFormDatum.getProcessId(), checkBoxFormDatum.getProcessVersion())).getId(),
                        checkBoxFormDatum.getRecordItemId(),
                        checkBoxFormDatum.getFieldId());
                ComponentDetailVO componentDetailVO = componentDetailMap.get(key);
                if (Objects.isNull(componentDetailVO)){
                    continue;
                }
                List<ComponentCheckBoxDetail> checkBoxDetails = JSON.parseArray(componentDetailVO.getComponentDetail(), ComponentCheckBoxDetail.class);
                List<Boolean> valueList = new ArrayList<>();
                Set<String> valueSet = new HashSet<>();
                // 此数据需要判空
                if (!BasicComponentTypeEnum.RADIO.getValue().equals(checkBoxFormDatum.getComponentType()) && StrUtil.isNotEmpty(checkBoxFormDatum.getValue())){
                    // 不是单选则进行拆分
                    try{
                        List<String> values = JSON.parseArray(checkBoxFormDatum.getValue(), String.class);
                        valueSet = new HashSet<>(values);
                    } catch (Exception e){
                        log.error("序列化选择框数据失败，checkBox={}", JSON.toJSONString(checkBoxFormDatum), e);
                    }
                } else {
                    valueSet.add(checkBoxFormDatum.getValue());
                }
                if (CollUtil.isEmpty(checkBoxDetails)){
                    continue;
                }
                for (ComponentCheckBoxDetail checkBoxDetail : checkBoxDetails) {
                    valueList.add(valueSet.contains(checkBoxDetail.getField()));
                }
                checkBoxFormDatum.setType(DatasetTransValueDataType.CHECKBOX);
                checkBoxFormDatum.setValue(JSON.toJSONString(valueList));
            }
        }


    }

    private String acquisitionDrawToImageCaptionValue(String value) {
        DocxImageCaptionReplaceOption.ImageCaptionData imageCaptionData = new DocxImageCaptionReplaceOption.ImageCaptionData();
        AcquisitionPictureExtInfo pictureExtInfo = JsonUtils.parseObject(value, AcquisitionPictureExtInfo.class);
        imageCaptionData.setImageUrl(fillIpPhotoPath(pictureExtInfo.getUrl(), StrUtil.EMPTY));
        String format = "设备信息：%s    设备数据：%s    采集人：%s    采集时间：%s    ";
        imageCaptionData.setImageCaption(String.format(format, pictureExtInfo.getEquipmentInfo(), pictureExtInfo.getEquipmentData(), pictureExtInfo.getAcquisitionUser(), pictureExtInfo.getAcquisitionTime()));
        return JsonUtils.toJsonString(imageCaptionData);
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

}
