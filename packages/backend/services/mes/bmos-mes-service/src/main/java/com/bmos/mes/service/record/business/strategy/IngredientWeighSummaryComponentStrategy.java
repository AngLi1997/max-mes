package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.ProcedureStepConfigInfo;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientWeighRecordComponentView;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 配料称量汇总组件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/5/13 14:17
 */
@Service(value = "WEIGHING_INGREDIENTS_SUMMARY")
public class IngredientWeighSummaryComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Long materialId;
        Map<Long, ProductFormulaMaterial> materialMap =
                CollectionUtils.convertMap(info.getFormulaInfo().getMaterials(), ProductFormulaMaterial::getMaterialId);
        List<IngredientWeighRecordComponentView> ingredientWeighRecords = info.getIngredientWeighRecords();
        Map<Long, List<IngredientWeighRecordComponentView>> detailMap = CollectionUtils.convertMultiMap(ingredientWeighRecords,
                IngredientWeighRecordComponentView::getMaterialId);
        List<IngredientWeighRecordComponentView> detailList = new ArrayList<>();
        ProductFormulaMaterial formulaMaterial = null;

        List<Long> finishedBatchList = info.getFinishedStorageBatchIdSummaryList();
        if (finishedBatchList.contains(info.getIngredientStorageBatchId()) && finishedBatchList.indexOf(info.getIngredientStorageBatchId()) != index){
            return;
        }
        if (ObjectUtil.isNotNull(index)) {
            if (finishedBatchList.size() <= index){
                return;
            }
            materialId = info.getFinishedMaterialIdSummaryList().get(index);
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

        if (formulaMaterial != null
                && info.getIngredientWeighMaterialId() != null
                && !Objects.equals(formulaMaterial.getMaterialId(), info.getIngredientWeighMaterialId())){
            return;
        }
        if (detailList == null){
            detailList = new ArrayList<>();
        }
        ProductFormulaMaterial finalFormulaMaterial = formulaMaterial;
        List<IngredientWeighRecordComponentView> finalDetailList = detailList.stream()
                .filter(item -> Objects.equals(WeighType.INGREDIENT, item.getWeighType()))
                .collect(Collectors.toList());
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

    private String getValueByType(ProductFormulaMaterial formulaMaterial, String type, List<IngredientWeighRecordComponentView> views) {
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        String value = null;
        switch (enumByValue) {
            case WEIGHING_INGREDIENTS_SUMMARY_NAME:
                value = formulaMaterial == null ? null : formulaMaterial.getMaterialName();
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_CODE:
                value = formulaMaterial == null ? null : formulaMaterial.getMaterialMergeCode();
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_SPECIFICATION:
                value = formulaMaterial == null ? null : formulaMaterial.getMaterialSpecification();
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_NET_WEIGHT:
                value = renderValue(views.stream().map(IngredientWeighRecordComponentView::getNetWeight)
                        .reduce(BigDecimal.ZERO, BigDecimal::add), formulaMaterial);
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_TARE_WEIGHT:
                value = renderValue(views.stream().map(IngredientWeighRecordComponentView::getTareWeight)
                        .reduce(BigDecimal.ZERO, BigDecimal::add), formulaMaterial);
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_GROSS_WEIGHT:
                value = renderValue(views.stream().map(IngredientWeighRecordComponentView::getGrossWeight)
                        .reduce(BigDecimal.ZERO, BigDecimal::add), formulaMaterial);
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_UNIT:
                value = unitCache.getGlobalUnitName(formulaMaterial == null ? null : formulaMaterial.getUnitId());
                break;
            case WEIGHING_INGREDIENTS_SUMMARY_TOTAL_NUMBER:
                value = String.valueOf(views.size());
                break;
        }
        return value;
    }

    private String renderValue(BigDecimal value, ProductFormulaMaterial productFormulaMaterial) {
        if (value == null){
            return null;
        }
        if (productFormulaMaterial == null){
            return null;
        }
        return unitCache.toExt(value, productFormulaMaterial.getUnitId()).stripTrailingZeros().toPlainString();
    }
}
