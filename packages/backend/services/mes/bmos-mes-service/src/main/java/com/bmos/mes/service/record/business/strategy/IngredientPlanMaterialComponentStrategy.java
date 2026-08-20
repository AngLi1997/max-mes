package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatch;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配料计划 物料汇总
 */
@Service(value = "INGREDIENTS_PLAN_MATERIAL")
@Slf4j
public class IngredientPlanMaterialComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long formulaMaterialId;
        if (ObjectUtil.isNotNull(index)) {
            List<IngredientMaterialBatchDetailInfo> ingredientMaterialBatchList = info.getIngredientMaterialBatchList();
            List<Long> formulaIds = ingredientMaterialBatchList
                    .stream()
                    .map(IngredientMaterialBatch::getFormulaMaterialId).distinct()
                    .sorted()
                    .collect(Collectors.toList());
            if (formulaIds.size() <= index) {
                return;
            }
            formulaMaterialId = formulaIds.get(index);
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterialId = configInfo.getFormulaMaterialId();
        }
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> ObjectUtil.isNotNull(e.getFieldId()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    BusinessComponentTypeEnum enumByValue =
                            BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    convert.setValue(this.getValueByType(info, enumByValue,
                            formulaMaterialId));
                    return convert;
                })
                .collect(Collectors.toList()));
    }


    String getValueByType(ProductionDetailInfo info, BusinessComponentTypeEnum enumByValue, Long id) {

        List<IngredientMaterialBatchDetailInfo> ingredientMaterialBatchList = info.getIngredientMaterialBatchList();
        Map<Long, List<IngredientMaterialBatchDetailInfo>> ingredientMap =
                CollectionUtils.convertMultiMap(ingredientMaterialBatchList,
                        IngredientMaterialBatchDetailInfo::getFormulaMaterialId);
        Map<Long, ProductFormulaMaterial> materialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial = materialMap.get(id);
        List<IngredientMaterialBatchDetailInfo> ingredientBatchList = ingredientMap.get(id);
        if (CollUtil.isEmpty(ingredientBatchList)) {
            return null;
        }
        String value = null;
        switch (enumByValue) {
            case INGREDIENTS_PLAN_MATERIAL_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case INGREDIENTS_PLAN_MATERIAL_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case INGREDIENTS_PLAN_MATERIAL_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case INGREDIENTS_PLAN_MATERIAL_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            // 配料计划
            case INGREDIENTS_PLAN_MATERIAL_BATCHNO:
                List<String> strings = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getMaterialBatchNo())
                        ? StrUtil.DASHED : e.getMaterialBatchNo()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(strings);
                break;
            case INGREDIENTS_PLAN_MATERIAL_QUANTITY:
                List<String> quantitys = CollectionUtils.convertList(ingredientBatchList,
                        e -> MaterialQuantityCalculateUtil.roundingOff(e.getIngredientQuantity(), formulaMaterial).toPlainString());
                value = BusinessComponentStrategy.getLFStrings(quantitys);
                break;
            case INGREDIENTS_PLAN_MATERIAL_TOTAL:
                value = ingredientBatchList.stream()
                        .map(e -> MaterialQuantityCalculateUtil.roundingOff(e.getIngredientQuantity(), formulaMaterial))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .toPlainString();
                break;
            case INGREDIENTS_PLAN_MATERIAL_MOISTURE:
                List<String> moistureList = ingredientBatchList.stream().map(e -> e.getHydration() == null ?
                        StrUtil.DASHED : e.getHydration().toPlainString()
                ).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(moistureList);
                break;
            case INGREDIENTS_PLAN_MATERIAL_CONTENT:
                List<String> contents = ingredientBatchList.stream().map(e -> e.getNoHydrationContent() == null ?
                        StrUtil.DASHED : e.getNoHydrationContent().toPlainString()
                ).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(contents);
                break;
            case INGREDIENTS_PLAN_MATERIAL_SUPPLIER:
                List<String> suppliers = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getSupplier())
                        ? StrUtil.DASHED : e.getSupplier()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(suppliers);
                break;
            case INGREDIENTS_PLAN_MATERIAL_MANUFACTURER:
                List<String> producers = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getProducer())
                        ? StrUtil.DASHED : e.getProducer()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(producers);
                break;
            case INGREDIENTS_PLAN_MATERIAL_ORIGINAL_BATCHNO:
                List<String> oriBatchNos = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getFactoryBatchNo())
                        ? StrUtil.DASHED : e.getFactoryBatchNo()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(oriBatchNos);
                break;
            case INGREDIENTS_PLAN_MATERIAL_ORIGINAL_CODE:
                List<String> oriCode = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getOriginalBatchNo())
                        ? StrUtil.DASHED : e.getOriginalBatchNo()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(oriCode);
                break;
            case INGREDIENTS_PLAN_MATERIAL_REPORT_NO:
                List<String> reportNo = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getReportNo())
                        ? StrUtil.DASHED : e.getReportNo()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(reportNo);
                break;
            case INGREDIENTS_PLAN_MATERIAL_RELEASE_NO:
                List<String> licenceNo = ingredientBatchList.stream().map(e -> StrUtil.isEmpty(e.getLicenceNo())
                        ? StrUtil.DASHED : e.getLicenceNo()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(licenceNo);
                break;
            case INGREDIENTS_PLAN_MATERIAL_EXPIRATION_DATE:
                List<String> expiredDate = ingredientBatchList.stream().map(e -> e.getExpiredDate() == null
                        ? StrUtil.DASHED : e.getExpiredDate().toString()).collect(Collectors.toList());
                value = BusinessComponentStrategy.getLFStrings(expiredDate);
                break;
        }
        return value;
    }

}
