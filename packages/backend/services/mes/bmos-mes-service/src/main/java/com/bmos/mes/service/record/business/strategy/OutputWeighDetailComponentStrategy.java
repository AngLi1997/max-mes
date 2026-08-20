package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.output.weigh.vo.OutputWeighRecordComponentView;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 产出称量详情组件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/5/13 14:17
 */
@Service(value = "OUTPUT_WEIGHING_DETAILS")
public class OutputWeighDetailComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<OutputWeighRecordComponentView> outputWeighRecords = info.getOutputWeighRecords();
        if (index >= outputWeighRecords.size()) {
            return;
        }
        OutputWeighRecordComponentView outputWeighRecordComponentView = outputWeighRecords.get(index);
        List<ExecuteFormData> list = component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    setValueByType(convert, info.getFormulaInfo(), e.getComponentType(), outputWeighRecordComponentView);
                    return convert;
                }).collect(Collectors.toList());
        results.addAll(list);
    }

    private void setValueByType(ExecuteFormData data, ProductFormulaInfo productFormulaInfo, String type, OutputWeighRecordComponentView view) {
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        ProductFormulaMaterial productFormulaMaterial = productFormulaInfo.getMaterialIdMap().get(view.getMaterialId());
        String value = null;
        switch (enumByValue) {
            case OUTPUT_WEIGHING_DETAILS_NAME:
                value = view.getMaterialName();
                break;
            case OUTPUT_WEIGHING_DETAILS_CODE:
                value = view.getMergeCode();
                break;
            case OUTPUT_WEIGHING_DETAILS_SPECIFICATION:
                value = view.getSpecification();
                break;
            case OUTPUT_WEIGHING_DETAILS_BATCHNO:
                value = view.getMaterialBatchNo();
                break;
            case OUTPUT_WEIGHING_DETAILS_PART_NUMBER:
                value = view.getMaterialNo();
                break;
            case OUTPUT_WEIGHING_DETAILS_NET_WEIGHT:
                // 已作废的值全部显示为 "-"
                if (Objects.equals(view.getWeighSignStatus(), WeighSignStatus.SCRAPED)) {
                    value = "-";
                } else {
                    value = renderValue(view.getNetWeight(), productFormulaMaterial, view.getUnitId());
                }
                break;
            case OUTPUT_WEIGHING_DETAILS_QUANTITY:
                // 已作废的值全部显示为 "-"
                if (Objects.equals(view.getWeighSignStatus(), WeighSignStatus.SCRAPED)) {
                    value = "-";
                } else {
                    value = renderValue(view.getQuantity(), productFormulaMaterial, view.getUnitId());
                }
                break;
            case OUTPUT_WEIGHING_DETAILS_TARE_WEIGHT:
                if (Objects.equals(view.getWeighSignStatus(), WeighSignStatus.SCRAPED)) {
                    value = "-";
                } else {
                    value = renderValue(view.getTareWeight(), productFormulaMaterial, view.getUnitId());
                }
                break;
            case OUTPUT_WEIGHING_DETAILS_GROSS_WEIGHT:
                if (Objects.equals(view.getWeighSignStatus(), WeighSignStatus.SCRAPED)) {
                    value = "-";
                } else {
                    value = renderValue(view.getGrossWeight(), productFormulaMaterial, view.getUnitId());
                }
                break;
            case OUTPUT_WEIGHING_DETAILS_UNIT:
                value = view.getUnit();
                break;
            case OUTPUT_WEIGHING_DETAILS_WEIGHER:
                value = view.getWeigherName();
                break;
            case OUTPUT_WEIGHING_DETAILS_REVIEWER:
                value = view.getReCheckerName();
                break;
            case OUTPUT_WEIGHING_DETAILS_WEIGHING_TIME:
                value = view.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Long timestamp = TimeUtil.getTimestamp(view.getWeighTime());
                data.setExtInfo(timestamp == null ? null : JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(timestamp.toString())));
                break;
        }
        data.setValue(value);
    }

    private String renderValue(BigDecimal value, ProductFormulaMaterial productFormulaMaterial, Long unitId) {
        if (value == null) {
            return null;
        }
        if (productFormulaMaterial == null) {
            return null;
        }
        return unitCache.toExt(value, unitId).stripTrailingZeros().toPlainString();
    }
}
