package com.bmos.mes.service.execute.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.service.execute.constant.ExecuteFormDataConstant;
import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.vo.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.record.vo.RecordItemVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bmos.mes.service.execute.constant.ExecuteFormDataConstant.DEFAULT_COPY_VERSION;

@Mapper
public interface ExecuteFormDataConverter {

    ExecuteFormDataConverter INSTANCE = Mappers.getMapper(ExecuteFormDataConverter.class);

    default List<ExecuteFormData> convert(FormDataBatchSaveDTO dto) {
        return dto.getItems().stream().map(e -> {
            ExecuteFormData convert = convert(dto, e);
            if (Objects.isNull(convert)) {
                return convert;
            }
            String valueExtension = convert.getValueExtension();
            convert.setOperationType(ExecuteFormDataType.SAVE.getValue());
            if (StrUtil.isEmpty(valueExtension)) {
                return convert;
            }
            convert.setExtInfo(e.getValueExtension());
            return convert;
        }).collect(Collectors.toList());
    }

    ExecuteFormData convert(FormDataBatchSaveDTO dto, FormDataBatchSaveItemDTO item);

    ExecuteFormData convert(BusinessDataHandleBaseDTO dto);

    ExecuteFormData convert(BusinessComponentBatchSaveDTO dto);

    ExecuteFormData convert(FormDataSaveDTO dto);

    ExecuteFormData convert(FormDataModifyDTO dto);

    FormDataVO convert(ExecuteFormData data);
    FormDataItemVO convert2FormDataItemVO(ExecuteFormData data);

    default List<FormDataVO> convertList(List<ExecuteFormData> list) {
        return list.stream().map(this::convert).peek(e -> {
            if (ExecuteFormDataType.MODIFY.getValue().equals(e.getOperationType())) {
                e.setReviewUsername(UserUtils.getUsername(e.getReviewUser()));
            }
        }).collect(Collectors.toList());
    }

    default List<FormDataItemVO> filterLatestAndConvert(List<ExecuteFormData> dataList, List<AttachmentVO> attachmentVOList,
                                                        String emptyData) {
        List<FormDataItemVO> result = new ArrayList<>();
        Long fieldId = null;
        for (ExecuteFormData data : dataList) {
            boolean empty = BooleanUtil.isTrue(data.getEmptyValue()) || StrUtil.equals(data.getValue(), emptyData);
            if (ObjectUtil.equals(fieldId, data.getFieldId())) {
                continue;
            }
            FormDataItemVO vo = new FormDataItemVO();
            vo.setFieldId(data.getFieldId());
            vo.setValue(data.getValue());
            vo.setComponentType(data.getComponentType());
            //判断是否是拍照上传组件并且值不为空
            if (StrUtil.equals(data.getComponentType(), BasicComponentTypeEnum.PHOTO.getValue()) && StrUtil.isNotBlank(data.getValue())) {
                List<AttachmentVO> list = CollectionUtils.filterList(attachmentVOList, item ->
                        StrUtil.split(data.getValue(), StrUtil.C_COMMA).contains(String.valueOf(item.getId())));
                vo.setValue(empty ? data.getValue() : JsonUtils.toJsonString(list));
            }
            vo.setValueExtension(data.getValueExtension());
            vo.setOperationType(data.getOperationType());
            vo.setEmptyValue(data.getEmptyValue());
            result.add(vo);
            fieldId = data.getFieldId();
        }
        return result;
    }


    default List<ExecuteFormData> filterLatest(List<ExecuteFormData> dataList) {
        if (CollUtil.isEmpty(dataList)){
            return Collections.emptyList();
        }
        //复制记录值处理
        Map<Long, List<ExecuteFormData>> copyVersionMap = CollectionUtils.convertMultiMap(dataList, ExecuteFormData::getCopyVersion);
        List<ExecuteFormData> result = new ArrayList<>();
        copyVersionMap.forEach((key,value)->{
            Long fieldId = null;
            for (ExecuteFormData data : value) {
                if (ObjectUtil.equals(fieldId, data.getFieldId())) {
                    continue;
                }
                result.add(data);
                fieldId = data.getFieldId();
            }
        });
        return result;
    }

    default Map<Long, Map<Long, Map<Long, Map<Long, List<String>>>>> convertDataMap(List<ExecuteFormData> dataList) {
        return dataList.stream().sorted(Comparator.comparing(ExecuteFormData::getBatchNo))
                //工艺
                .collect(
                        Collectors.groupingBy(ExecuteFormData::getProcessId, LinkedHashMap::new,
                                //工序步骤，如果是复用的话，返回 0
                                Collectors.groupingBy(e -> e.getReuse() ? 0 : e.getProcedureStepId(), LinkedHashMap::new,
                                        //记录项id
                                        Collectors.groupingBy(ExecuteFormData::getRecordItemId, LinkedHashMap::new,
                                                //组件id
                                                Collectors.groupingBy(ExecuteFormData::getFieldId, LinkedHashMap::new,
                                                        Collectors.mapping(ExecuteFormData::getValue, Collectors.toList()))))));
    }

    CalculateDataQueryDTO convertQuery(FormDataBatchSaveDTO dto);

    CalculateDataQueryDTO convertQuery(FormDataModifyDTO dto);

    ExecuteFormData copy(ExecuteFormData data);

    default ExecuteFormData buildFormData(CalculateDataQueryDTO query,
                                          Set<Long> fieldsInDB,
                                          LocalDateTime operationTime,
                                          ExecuteFormData calculateResult) {
        return ExecuteFormData.builder()
                .value(calculateResult.getValue())
                .fieldId(calculateResult.getFieldId())
                .extInfo(calculateResult.getExtInfo())
                .valueExtension(calculateResult.getValueExtension())
                .batchNo(query.getBatchNo())
                // 使用计算结果的copyVersion而非query中的copyVersion
                // query中的copyVersion仅表示操作页版本
                .copyVersion(calculateResult.getCopyVersion())
                .processId(query.getProcessId())
                .processVersion(query.getProcessVersion())
                .productPlanId(query.getProductPlanId())
                .recordItemId(calculateResult.getRecordItemId())
                .componentType(calculateResult.getComponentType())
                // 使用计算结果的工步id
                .procedureStepModelId(query.getProcedureStepModelId())
                .procedureStepId(calculateResult.getProcedureStepId())
                .processChangeNumber(calculateResult.getProcessChangeNumber())
                .procedureChangeNumber(calculateResult.getProcedureChangeNumber())
                .operationTime(operationTime)
                .operationUser(ExecuteFormDataConstant.OPERATION_USER_SYSTEM)
                .discard(false)
                .operationType(fieldsInDB.contains(calculateResult.getFieldId()) ?
                        ExecuteFormDataType.UPDATE.getValue() : ExecuteFormDataType.SAVE.getValue())
                .systemCreate(true)
                .reuse(calculateResult.getReuse())
                .build();
    }

    default ExecuteRecordCopy convertCopy(RecordCopyQueryDTO dto, Plan plan,Long versionMaxValue) {
        ExecuteRecordCopy copy = new ExecuteRecordCopy();
        copy.setProductPlanId(dto.getProductPlanId());
        copy.setProcessId(plan.getProcessId());
        copy.setProcessVersion(plan.getProcessVersion());
        copy.setProcedureStepId(dto.getReuse() ? 0L : dto.getProcedureStepId());
        copy.setVersion(dto.getReuse() ? DEFAULT_COPY_VERSION : versionMaxValue + 1);
        copy.setProcedureChangeNumber(dto.getReuse() ? 0 : dto.getProcedureChangeNumber());
        copy.setProcessChangeNumber(dto.getReuse() ? 0 : dto.getProcessChangeNumber());
        copy.setBatchNo(plan.getBatchNo());
        copy.setDiscard(false);
        copy.setReuse(dto.getReuse());
        copy.setRecordItemId(dto.getRecordItemId());
        copy.setRecordVersionId(dto.getRecordVersionId());
        return copy;
    }

    List<IntactFormDataItemVO> convertList2(List<ExecuteFormData> data);

    CalculateDataQueryDTO convertQuery(BusinessComponentBatchSaveDTO dto);

    FormDataModifyDTO convertToModifyDTO(FormDataUpdateDTO dto);

    ExecuteFormData convert(ExecuteRecordCopy copy);

    default CalculateDataQueryDTO convert2CalculateQueryDto(Plan plan, ProcedureStepModel procedureStepModel, Long copyVersion){
        return CalculateDataQueryDTO.builder()
                .batchNo(plan.getBatchNo())
                .processId(plan.getProcessId())
                .processVersion(plan.getProcessVersion())
                .productPlanId(plan.getId())
                .procedureStepId(procedureStepModel.getProcedureStepId())
                .procedureStepModelId(procedureStepModel.getId())
                .reuse(procedureStepModel.getReusable())
                .copyVersion(copyVersion)
                .recordItemId(procedureStepModel.getRecordItemId())
                .recordVersionId(procedureStepModel.getRecordVersionId())
                .build();
    }

    default List<ComponentTrendAnalysisVO> convert2TrendAnalysisVO(List<ExecuteFormData> executeFormDataList, Plan plan){
        List<ComponentTrendAnalysisVO> componentTrendAnalysisVOS = new ArrayList<>();
        if (CollUtil.isEmpty(executeFormDataList)){
            return componentTrendAnalysisVOS;
        }
        Integer index = 0;
        for (ExecuteFormData formData : executeFormDataList) {
            ComponentTrendAnalysisVO componentTrendAnalysisVO = new ComponentTrendAnalysisVO();
            try{
                componentTrendAnalysisVO.setValue(new BigDecimal(formData.getValue()));
            } catch (Exception ignored){
                componentTrendAnalysisVO.setValue(null);
            }
            componentTrendAnalysisVO.setBatchNo(plan.getBatchNo());
            componentTrendAnalysisVO.setPlanId(plan.getId().toString());
            componentTrendAnalysisVO.setSerialNo(++index);
            componentTrendAnalysisVOS.add(componentTrendAnalysisVO);
        }
        return componentTrendAnalysisVOS;
    }

    @Mapping(target = "recordName", source = "name")
    @Mapping(target = "recordItemId", source = "itemId")
    SubsidiaryRecordDocVO convert2SubsidiaryRecordDocVO(RecordItemVO recordItemVO);

    List<ExecuteEquipmentVO> convert2ExecuteEquipmentVO(List<EquipmentInfoFeignVO> data);

    ExecuteEquipmentVO convert2ExecuteEquipmentVO(EquipmentInfoFeignVO data);
}
