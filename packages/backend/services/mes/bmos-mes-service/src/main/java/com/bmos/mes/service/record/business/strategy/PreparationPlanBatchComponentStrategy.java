package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.component.ComponentDetail;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatchDetailInfo;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.CustomFieldUtil;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 配液计划->物料批次策略
 */
@Service("LIQUID_PREPARATION_PLAN_BATCH")
public class PreparationPlanBatchComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        if (ObjectUtil.isNull(index)) {
            return;
        }
        List<LiquidPreparationMaterialBatchDetailInfo> preparationBatchList = info.getLiquidPreparationBatchList();
        List<LiquidPreparationMaterialBatchDetailInfo> sorted = preparationBatchList.stream()
                .sorted(Comparator.comparing(LiquidPreparationMaterialBatchDetailInfo::getOrder)
                        .thenComparing(LiquidPreparationMaterialBatchDetailInfo::getCreateTime))
                .collect(Collectors.toList());
        if (sorted.size() <= index) {
            return;
        }
        LiquidPreparationMaterialBatchDetailInfo detail = sorted.get(index);
        ProductFormulaInfo formulaInfo = info.getFormulaInfo();
        ProductFormulaMaterial formulaMaterial = formulaInfo.getMaterialMap().get(detail.getFormulaMaterialId());
        List<CustomFieldDetailInfo> customFieldList = info.getCustomFieldList();
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    BusinessComponentTypeEnum componentType =
                            BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    String componentDetail = e.getComponentDetail();
                    convert.setValue(this.getValueByType(formulaMaterial, componentType, detail, componentDetail,
                            customFieldList));
                    convert.setExtInfo(this.buildExtInfo(componentType, convert.getValue()));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String buildExtInfo(BusinessComponentTypeEnum componentType, String value) {
        ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
        switch (componentType) {
            case LIQUID_PREPARATION_PLAN_BATCH_EXPIRY_DATE:
                if (ObjectUtil.isNull(value)) {
                    break;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(value));
            default:
                break;
        }
        return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
    }

    private String convertToTimeStamp(String value) {
        LocalDateTime parse = LocalDateTimeUtil.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime zonedDateTime = parse.atZone(ZoneId.systemDefault());
        return String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }

    private String getValueByType(ProductFormulaMaterial formulaMaterial, BusinessComponentTypeEnum componentType,
                                  LiquidPreparationMaterialBatchDetailInfo detail, String componentDetail,List<CustomFieldDetailInfo> list) {
        String value = null;
        switch (componentType) {
            case LIQUID_PREPARATION_PLAN_BATCH_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_BATCHNO:
                value = detail.getMaterialBatchNo();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_QUANTITY:
                value = detail.getPreparationQuantity().toPlainString();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_EXPIRY_DATE:
                value = detail.getExpiredDate();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_SUPPLIER:
                value = detail.getSupplier();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_MANUFACTURER:
                value = detail.getProducer();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_ORIGINAL_BATCHNO:
                value = detail.getFactoryBatchNo();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_ORIGINAL_CODE:
                value = detail.getOriginalBatchNo();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_REPORT_NO:
                value = detail.getReportNo();
                break;
            case LIQUID_PREPARATION_PLAN_BATCH_RELEASE_NO:
                value = detail.getLicenceNo();
                break;
            case CUSTOM_FIELD:
                ComponentDetail componentInfo = JsonUtils.parseObject(componentDetail, ComponentDetail.class);
                if (Objects.isNull(componentInfo) || StrUtil.isEmpty(componentInfo.getFieldData())){
                    break;
                }
                value = CustomFieldUtil.getFieldValue(list, componentInfo, detail.getMaterialBatchId());
                break;
        }

        return value;
    }
}
