package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
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
import com.bmos.mes.service.record.business.model.preparation.PreparationProduceDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationProduceMaterialInfo;
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
@Component("LIQUID_PREPARATION_OUTPUT_DETAILS")
public class PreparationProduceDetailComponentStrategy implements BusinessComponentStrategy {

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {

        PreparationProduceDetailInfo detailInfo = (PreparationProduceDetailInfo) info;
        List<PreparationProduceMaterialInfo> allPreparationProduceMaterialInfo = Lists.newArrayList(detailInfo.getPreparationProduceMaterialInfos());
        if (index > allPreparationProduceMaterialInfo.size() - 1){
            return ;
        }
        PreparationProduceMaterialInfo preparationProduceMaterialInfo = allPreparationProduceMaterialInfo.get(index);
        for (ComponentListVO child : component.getChildren()) {
            BusinessComponentBatchSaveDTO dto = info.getDto();
            ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(dto);
            formData.setFieldId(child.getFieldId());
            formData.setComponentType(child.getComponentType());
            formData.setDiscard(false);
            formData.setValue(this.getValueByType(child, preparationProduceMaterialInfo, info.getCustomFieldList()));
            formData.setExtInfo(this.buildExtInfo(child.getComponentType(), preparationProduceMaterialInfo));
            results.add(formData);
        }
    }

    private String buildExtInfo(String componentType, PreparationProduceMaterialInfo preparationProduceMaterialInfo) {
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_OUTPUT_TIME.getValue().equals(componentType)
                && Objects.nonNull(preparationProduceMaterialInfo.getProduceTime())){
            ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
            executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(preparationProduceMaterialInfo.getProduceTime()));
            JSON.toJSONString(executeFormDataBaseExtInfo);
        }
        return null;

    }

    private String getValueByType(ComponentListVO component, PreparationProduceMaterialInfo preparationProduceMaterialInfo,
                                  List<CustomFieldDetailInfo> customFieldDetailInfos) {

        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_NAME.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getMaterialName();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_CODE.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getMaterialCode();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_SPECIFICATION.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getSpecification();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_BATCHNO.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getStorageMaterialBatchNo();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_PARTNO.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getStorageMaterialNo();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_QUANTITY.getValue().equals(component.getComponentType())){
            if (PrepareSignStatusEnum.SCRAPED.getValue().equals(preparationProduceMaterialInfo.getSignStatus().getValue())){
                return StrUtil.DASHED;
            }
            return String.valueOf(preparationProduceMaterialInfo.getQuantity());
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_UNIT.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getUnit();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_OPERATOR.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getProducerName();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_OUTPUT_TIME.getValue().equals(component.getComponentType()) && Objects.nonNull(preparationProduceMaterialInfo.getProduceTime())){
            return preparationProduceMaterialInfo.getProduceTime().format(DateTimeFormatter.ofPattern(TimeUtil.F_DATETIME));
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS_REVIEWER.getValue().equals(component.getComponentType())){
            return preparationProduceMaterialInfo.getReCheckerName();
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
            return CustomFieldUtil.getFieldValue(customFieldDetailInfos, componentInfo, preparationProduceMaterialInfo.getStorageMaterialBatchId());
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
