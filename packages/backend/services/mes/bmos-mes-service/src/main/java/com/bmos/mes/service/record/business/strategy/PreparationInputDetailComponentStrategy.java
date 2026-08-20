package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.component.ComponentDetail;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationInputDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationInputMaterialInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.CustomFieldUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 配液投入详情组件
 */
@Component("LIQUID_PREPARATION_INPUT_DETAIL")
public class PreparationInputDetailComponentStrategy implements BusinessComponentStrategy {

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {

        PreparationInputDetailInfo detailInfo = (PreparationInputDetailInfo) info;
        List<PreparationInputMaterialInfo> allPreparationInputMaterialInfo = Lists.newArrayList(detailInfo.getPreInputStorageMaterialList());
        allPreparationInputMaterialInfo.addAll(detailInfo.getCurrentInputStoratageMaterialList());
        if (index > allPreparationInputMaterialInfo.size() - 1){
            return ;
        }
        PreparationInputMaterialInfo preparationInputMaterialInfo = allPreparationInputMaterialInfo.get(index);
        for (ComponentListVO child : component.getChildren()) {
            BusinessComponentBatchSaveDTO dto = info.getDto();
            ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(dto);
            formData.setFieldId(child.getFieldId());
            formData.setComponentType(child.getComponentType());
            formData.setDiscard(false);
            formData.setValue(this.getValueByType(child, preparationInputMaterialInfo, detailInfo.getCustomFieldList()));
            formData.setExtInfo(this.buildExtInfo(child.getComponentType(), preparationInputMaterialInfo));
            results.add(formData);
        }
    }

    private String buildExtInfo(String componentType, PreparationInputMaterialInfo preparationInputMaterialInfo) {
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_INPUT_TIME.getValue().equals(componentType)
                && Objects.nonNull(preparationInputMaterialInfo.getInputTime())){
            ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
            executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(preparationInputMaterialInfo.getInputTime()));
            JSON.toJSONString(executeFormDataBaseExtInfo);
        }
        return null;

    }

    private String getValueByType(ComponentListVO component, PreparationInputMaterialInfo preparationInputMaterialInfo,
                                  List<CustomFieldDetailInfo> customFieldDetailInfos) {
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_NAME.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getMaterialName();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_CODE.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getMaterialMergeCode();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_SPECIFICATION.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getSpecification();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_BATCH_NO.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getStorageMaterialBatchNo();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_PART_NO.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getStorageMaterialNo();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_QUANTITY.getValue().equals(component.getComponentType())){
            return String.valueOf(preparationInputMaterialInfo.getQuantity());
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_UNIT.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getUnit();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_IMPORTER.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getImporterName();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_INPUT_TIME.getValue().equals(component.getComponentType()) && Objects.nonNull(preparationInputMaterialInfo.getInputTime())){
            return preparationInputMaterialInfo.getInputTime().format(DateTimeFormatter.ofPattern(TimeUtil.F_DATETIME));
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_DEVICE_NO.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getDeviceCode();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_INPUT_DETAIL_DEVICE_NAME.getValue().equals(component.getComponentType())){
            return preparationInputMaterialInfo.getDeviceName();
        }
        if (BusinessComponentTypeEnum.CUSTOM_FIELD.getValue().equals(component.getComponentType())){
            String componentDetail = component.getComponentDetail();
            if (StrUtil.isNotEmpty(componentDetail)){
                return null;
            }
            ComponentDetail componentInfo = JsonUtils.parseObject(component.getComponentDetail(),
                    ComponentDetail.class);
            if (Objects.isNull(componentInfo)){
                return null ;
            }
            if (StrUtil.isNotEmpty(componentInfo.getFieldData())){
                return componentInfo.getFieldData();
            }
            return CustomFieldUtil.getFieldValue(customFieldDetailInfos, componentInfo, preparationInputMaterialInfo.getStorageMaterialBatchId());
        }
        return null;
    }

    private String convertToTimeStamp(LocalDateTime time) {
        // 结合系统默认时区转为ZonedDateTime
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.systemDefault());
        // 将ZonedDateTime转换为时间戳
        return String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }
}
