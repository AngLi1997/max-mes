package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecordDetail;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配料投入-->物料汇总
 */
@Service(value = "INGREDIENTS_INPUT_SUMMARY")
public class IngredientInputSummaryComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long materialId;
        Map<Long, ProductFormulaMaterial> materialMap =
                CollectionUtils.convertMap(info.getFormulaInfo().getMaterials(), ProductFormulaMaterial::getMaterialId);
        List<IngredientInputRecordDetail> ingredientInputList = info.getIngredientInputRecordDetailList();
        Map<Long, List<IngredientInputRecordDetail>> detailMap = CollectionUtils.convertMultiMap(ingredientInputList,
                IngredientInputRecordDetail::getMaterialId);
        List<IngredientInputRecordDetail> detailList;
        ProductFormulaMaterial formulaMaterial;
        if (ObjectUtil.isNotNull(index)) {
            List<Long> ids = ingredientInputList.stream()
                    .map(IngredientInputRecordDetail::getMaterialId).distinct().collect(Collectors.toList());
            if (ids.size() <= index) {
                return;
            }
            materialId = ids.get(index);
            formulaMaterial = materialMap.get(materialId);
            detailList = detailMap.get(materialId);
        } else {
            BusinessComponentConfigDetailVO config = configMap.get(component.getId());
            if (ObjectUtil.isNull(config)) {
                return;
            }
            ProcedureStepConfigInfo configInfo = JsonUtils.parseObject(config.getConfigInfo(),
                    ProcedureStepConfigInfo.class);
            formulaMaterial = info.getFormulaInfo().getMaterialMap().get(configInfo.getFormulaMaterialId());
            detailList = detailMap.get(formulaMaterial.getMaterialId());
        }
        if (CollUtil.isEmpty(detailList)) {
            return;
        }
        ProductFormulaMaterial finalFormulaMaterial = formulaMaterial;
        List<IngredientInputRecordDetail> finalDetailList = detailList;
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(getValueByType(finalFormulaMaterial, e.getComponentType(), finalDetailList));
                    return convert;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(ProductFormulaMaterial formulaMaterial, String type,
                                  List<IngredientInputRecordDetail> detailList) {
        String value = null;
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        switch (enumByValue) {
            case INGREDIENTS_INPUT_SUMMARY_MATERIAL_NAME:
                value = formulaMaterial.getMaterialName();
                break;
            case INGREDIENTS_INPUT_SUMMARY_MATERIAL_CODE:
                value = formulaMaterial.getMaterialMergeCode();
                break;
            case INGREDIENTS_INPUT_SUMMARY_MATERIAL_SPECIFICATION:
                value = formulaMaterial.getMaterialSpecification();
                break;
            case INGREDIENTS_INPUT_SUMMARY_TOTAL_QUANTITY:
                if (detailList == null) {
                    value = String.valueOf(0);
                    break;
                }
                value = detailList.stream().map(e -> {
                    BigDecimal quantity = e.getQuantity();
                    return unitCache.convert(quantity, unitCache.getBaseUnitId(formulaMaterial.getUnitId()),
                            formulaMaterial.getUnitId());
                }).reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros().toPlainString();
                break;
            case INGREDIENTS_INPUT_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial.getUnitId());
                break;
            case INGREDIENTS_INPUT_SUMMARY_TOTAL_NUMBER:
                value = String.valueOf(detailList == null ? 0 : detailList.size());
                break;
        }
        return value;
    }
}
