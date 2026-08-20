package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecordDetail;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配料投入-->详情
 */
@Service(value = "INGREDIENTS_INPUT_FEEDING_DETAILS")
public class IngredientInputDetailComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<IngredientInputRecordDetail> ingredientInputList = info.getIngredientInputRecordDetailList();
        if (index >= ingredientInputList.size()) {
            return;
        }
        IngredientInputRecordDetail ingredientInputRecord = ingredientInputList.get(index);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    setValueByType(convert, e.getComponentType(), ingredientInputRecord);
                    return convert;
                }).collect(Collectors.toList()));
    }

    private void setValueByType(ExecuteFormData data, String type, IngredientInputRecordDetail ingredientInputRecord) {
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        String value = null;
        switch (enumByValue) {
            case INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_NAME:
                value = ingredientInputRecord.getMaterialName();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_CODE:
                value = ingredientInputRecord.getMaterialMergeCode();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_SPECIFICATION:
                value = ingredientInputRecord.getSpecification();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_BATCHNO:
                value = ingredientInputRecord.getStorageMaterialBatchNo();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_PARTNO:
                value = ingredientInputRecord.getStorageMaterialNo();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_MATERIAL_QUANTITY:
                value = unitCache.convert(ingredientInputRecord.getQuantity(),
                        unitCache.getBaseUnitId(ingredientInputRecord.getUnitId()),
                        ingredientInputRecord.getUnitId()).stripTrailingZeros().toPlainString();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_UNIT:
                value = unitCache.getGlobalUnitName(ingredientInputRecord.getUnitId());
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDER:
                value = UserUtils.getUser(ingredientInputRecord.getImporterId()).getUserName();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDING_TIME:
                value = DateUtil.format(ingredientInputRecord.getInputTime(), DatePattern.NORM_DATETIME_PATTERN);
                Long timestamp = TimeUtil.getTimestamp(ingredientInputRecord.getInputTime());
                data.setExtInfo(timestamp == null ? null : JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(timestamp.toString())));
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_DEVICE_NAME:
                value = ingredientInputRecord.getDeviceName();
                break;
            case INGREDIENTS_INPUT_FEEDING_DETAILS_DEVICE_CODE:
                value = ingredientInputRecord.getDeviceCode();
                break;
        }
        data.setValue(value);
    }
}
