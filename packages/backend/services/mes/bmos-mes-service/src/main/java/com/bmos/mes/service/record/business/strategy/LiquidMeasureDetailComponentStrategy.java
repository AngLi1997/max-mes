package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
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
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.preparation.measure.vo.MeasureResultRecordVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.CustomFieldUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 配液量取组件-->详情
 */
@Service(value = "LIQUID_PREPARATION_MEASURE_DETAIL")
public class LiquidMeasureDetailComponentStrategy implements BusinessComponentStrategy {

    private final UnitCache unitCache;

    public LiquidMeasureDetailComponentStrategy(UnitCache unitCache) {
        this.unitCache = unitCache;
    }

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<MeasureResultRecordVO> resultList = info.getMeasureResultRecordList();
        if (index >= resultList.size()) {
            return;
        }
        MeasureResultRecordVO resultVO = resultList.get(index);
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = info.getFormulaInfo().getMaterialMap();
        ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(resultVO.getFormulaMaterialId());
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    setValueByType(convert, e, resultVO, formulaMaterial, info.getCustomFieldList());
                    return convert;
                }).collect(Collectors.toList()));
    }

    private void setValueByType(ExecuteFormData data, ComponentListVO component, MeasureResultRecordVO view,
                                ProductFormulaMaterial formulaMaterial, List<CustomFieldDetailInfo> customFieldList) {
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(component.getComponentType());
        String value = null;
        switch (enumByValue) {
            case LIQUID_PREPARATION_MEASURE_DETAIL_NAME:
                value = view.getMaterialName();
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_CODE:
                value = view.getMaterialMergeCode();
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_SPECIFICATION:
                value = view.getSpecification();
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_BATCHNO:
                value = view.getMaterialBatchNo();
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_PARTNO:
                value = view.getStorageMaterialNo();
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_QUANTITY:
                value = view.getQuantity().toPlainString();
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_UNIT:
                value = unitCache.getGlobalUnitName(view.getUnitId());
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_OPERATOR:
                value = UserUtils.getUsername(view.getMeasurerId());
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_REVIEWER:
                value = UserUtils.getUsername(view.getReCheckerId());
                break;
            case LIQUID_PREPARATION_MEASURE_DETAIL_OPERATION_TIME:
                value = view.getMeasureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Long timestamp = TimeUtil.getTimestamp(view.getMeasureTime());
                data.setExtInfo(timestamp == null ? null :
                        JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(timestamp.toString())));
                break;
            case CUSTOM_FIELD:
                ComponentDetail componentInfo = JsonUtils.parseObject(component.getComponentDetail(),
                        ComponentDetail.class);
                if (Objects.isNull(componentInfo) || StrUtil.isEmpty(componentInfo.getFieldData())){
                    break;
                }
                value = CustomFieldUtil.getFieldValue(customFieldList, componentInfo, view.getStorageMaterialBatchId());
                break;
        }
        data.setValue(value);
    }

}
