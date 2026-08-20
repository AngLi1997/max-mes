package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientWeighRecordComponentView;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配料称量详情组件
 * @author liang
 * @version 1.0.0
 * @date 2024/5/13 14:17
 */
@Service(value = "WEIGHING_INGREDIENTS_DETAILS")
public class IngredientWeighDetailComponentStrategy implements BusinessComponentStrategy {

    private final UnitCache unitCache;

    public IngredientWeighDetailComponentStrategy(UnitCache unitCache) {
        this.unitCache = unitCache;
    }

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        Collection<FormDataItemVO> formDataCollection = info.getFormDataCollection();
        Collection<Long> existedFieldIdList = CollectionUtils.convertSet(formDataCollection, FormDataItemVO::getFieldId);
        List<IngredientWeighRecordComponentView> ingredientWeighRecords = info.getIngredientWeighRecords();
        ProductFormulaInfo formulaInfo = info.getFormulaInfo();
        if (index >= ingredientWeighRecords.size()) {
            return;
        }
        IngredientWeighRecordComponentView ingredientWeighRecordComponentView = ingredientWeighRecords.get(index);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    setValueByType(convert, formulaInfo, e.getComponentType(), ingredientWeighRecordComponentView);
                    return convert;
                }).collect(Collectors.toList()));
    }

    private void setValueByType(ExecuteFormData data, ProductFormulaInfo productFormulaInfo, String type, IngredientWeighRecordComponentView view) {
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        ProductFormulaMaterial productFormulaMaterial = productFormulaInfo.getMaterialIdMap().get(view.getMaterialId());
        String value = null;
        switch (enumByValue) {
            case WEIGHING_INGREDIENTS_DETAILS_NAME:
                value = view.getMaterialName();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_CODE:
                value = view.getMergeCode();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_SPECIFICATION:
                value = view.getSpecification();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_BATCHNO:
                value = view.getMaterialBatchNo();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_PART_NUMBER:
                value = view.getMaterialNo();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_NET_WEIGHT:
                value = renderValue(view.getNetWeight(), productFormulaMaterial);
                break;
            case WEIGHING_INGREDIENTS_DETAILS_TARE_WEIGHT:
                value = renderValue(view.getTareWeight(), productFormulaMaterial);
                break;
            case WEIGHING_INGREDIENTS_DETAILS_GROSS_WEIGHT:
                value = renderValue(view.getGrossWeight(), productFormulaMaterial);
                break;
            case WEIGHING_INGREDIENTS_DETAILS_UNIT:
                value = view.getUnit();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_WEIGHER:
                value = view.getWeigherName();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_REVIEWER:
                value = view.getReCheckerName();
                break;
            case WEIGHING_INGREDIENTS_DETAILS_WEIGHING_TIME:
                value = view.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Long timestamp = TimeUtil.getTimestamp(view.getWeighTime());
                data.setExtInfo(timestamp == null ? null : JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(timestamp.toString())));
                break;
        }
        data.setValue(value);
    }

    private String renderValue(BigDecimal value, ProductFormulaMaterial productFormulaMaterial){
        if (value == null){
            return null;
        }
        if (productFormulaMaterial == null){
            return null;
        }
       return unitCache.toExt(value, productFormulaMaterial.getUnitId()).stripTrailingZeros().toPlainString();
    }
}
