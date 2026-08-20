package com.bmos.lims2.server.eln.entry.converter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.enums.BasicComponentTypeEnum;
import com.bmos.lims2.server.eln.entry.constant.ExecuteFormDataConstant;
import com.bmos.lims2.server.eln.entry.dto.*;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataItemVO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Mapper
public interface ExecuteFormDataConverter {

    ExecuteFormDataConverter INSTANCE = Mappers.getMapper(ExecuteFormDataConverter.class);

    FormDataModifyDTO convertToModifyDTO(FormDataUpdateDTO dto);

    FormDataItemVO convert2FormDataItemVO(ExecuteFormData data);

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


    @org.mapstruct.Mappings({
            @org.mapstruct.Mapping(source = "parameterId", target = "parameterId"),
            @org.mapstruct.Mapping(source = "parameterConfigId", target = "parameterConfigId")
    })
    ExecuteFormData convert(BusinessComponentBatchSaveDTO dto);

    default ExecuteFormData buildFormData(CalculateDataQueryDTO query,
                                          Set<Long> fieldsInDB,
                                          LocalDateTime operationTime,
                                          ExecuteFormData calculateResult) {
        return ExecuteFormData.builder()
                .value(calculateResult.getValue())
                .fieldId(calculateResult.getFieldId())
                .extInfo(calculateResult.getExtInfo())
                .valueExtension(calculateResult.getValueExtension())
                .taskId(query.getTaskId())
                .batchNo(query.getBatchNo())
                .schemeId(query.getSchemeId())
                .schemeVersionId(query.getSchemeVersionId())
                .inspectionOrderId(query.getInspectionOrderId())
                .recordId(query.getRecordId())
                .recordVersionId(query.getRecordVersionId())
                .recordItemId(ObjectUtil.defaultIfNull(calculateResult.getRecordItemId(), query.getRecordItemId()))
                .itemId(query.getItemId())
                .itemConfigId(query.getItemConfigId())
                .componentType(calculateResult.getComponentType())
                // 使用计算结果的工步id
                .parameterConfigId(query.getParameterConfigId())
                .parameterId(ObjectUtil.defaultIfNull(calculateResult.getParameterId(), query.getParameterId()))
                .operationTime(operationTime)
                .operationUser(ExecuteFormDataConstant.OPERATION_USER_SYSTEM)
                .discard(false)
                .operationType(fieldsInDB.contains(calculateResult.getFieldId()) ?
                        ExecuteFormDataType.UPDATE.getValue() : ExecuteFormDataType.SAVE.getValue())
                .systemCreate(true)
                .build();
    }


    default CalculateDataQueryDTO convert2CalculateQueryDto(InspectionOrder order, InspectionSchemeParameter inspectionSchemeParameter) {
        return CalculateDataQueryDTO.builder()
                .batchNo(order.getBatchNo())
                .schemeId(inspectionSchemeParameter.getSchemeId())
                .schemeVersionId(inspectionSchemeParameter.getVersionId())
                .inspectionOrderId(order.getId())
                .itemId(inspectionSchemeParameter.getInspectItemId())
                .itemConfigId(inspectionSchemeParameter.getItemConfigId())
                .parameterId(inspectionSchemeParameter.getParameterId())
                .parameterConfigId(inspectionSchemeParameter.getId())
                .recordItemId(inspectionSchemeParameter.getRecordItemId())
                .recordVersionId(inspectionSchemeParameter.getRecordVersionId())
                .build();
    }


    @org.mapstruct.Mappings({
            @org.mapstruct.Mapping(source = "dto.itemId", target = "itemId"),
            @org.mapstruct.Mapping(source = "dto.itemConfigId", target = "itemConfigId"),
            @org.mapstruct.Mapping(source = "dto.parameterId", target = "parameterId"),
            @org.mapstruct.Mapping(source = "dto.parameterConfigId", target = "parameterConfigId")
    })
    ExecuteFormData convert(FormDataBatchSaveDTO dto, FormDataBatchSaveItemDTO item);


    ExecuteFormData convert(FormDataModifyDTO dto);


    CalculateDataQueryDTO convertQuery(FormDataBatchSaveDTO dto);

    CalculateDataQueryDTO convertQuery(FormDataModifyDTO dto);

    CalculateDataQueryDTO convertQuery(BusinessComponentBatchSaveDTO dto);


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

    default ExecuteFormData convert(InspectionOrder order, InspectionSchemeParameter parameter) {
        ExecuteFormData executeFormData = new ExecuteFormData();
        executeFormData.setBatchNo(order.getBatchNo());
        executeFormData.setSchemeId(parameter.getSchemeId());
        executeFormData.setSchemeVersionId(parameter.getVersionId());
        executeFormData.setInspectionOrderId(order.getId());
        executeFormData.setItemId(parameter.getInspectItemId());
        executeFormData.setItemConfigId(parameter.getItemConfigId());
        executeFormData.setParameterId(parameter.getParameterId());
        executeFormData.setParameterConfigId(parameter.getId());
        executeFormData.setRecordItemId(parameter.getRecordItemId());
        executeFormData.setRecordVersionId(parameter.getRecordVersionId());
        return executeFormData;
    }


    default List<ExecuteFormData> filterLatest(List<ExecuteFormData> dataList) {
        if (CollUtil.isEmpty(dataList)){
            return Collections.emptyList();
        }
        List<ExecuteFormData> result = new ArrayList<>();
            Long fieldId = null;
            for (ExecuteFormData data : dataList) {
                if (ObjectUtil.equals(fieldId, data.getFieldId())) {
                    continue;
                }
                result.add(data);
                fieldId = data.getFieldId();

        };
        return result;
    }
}
