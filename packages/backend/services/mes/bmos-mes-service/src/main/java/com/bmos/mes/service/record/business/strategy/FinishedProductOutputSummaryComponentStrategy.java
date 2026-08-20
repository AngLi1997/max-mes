package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 成品产出-->物料汇总
 */
@Service(value = "PRODUCT_OUTPUT_SUMMARY")
public class FinishedProductOutputSummaryComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<FinishedProductOutputResult> outputResults = info.getOutputResults();
        if (CollUtil.isEmpty(outputResults)) {
            return;
        }
        Map<Long, String> map = CollectionUtils.convertMap(info.getFormDataCollection(), FormDataItemVO::getFieldId,
                FormDataItemVO::getValue);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(getValueByType(info, e.getComponentType(), null));
                    if (Objects.equals(map.get(e.getFieldId()), convert.getValue())) {
                        return null;
                    }
                    return convert;
                }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private String getValueByType(ProductionDetailInfo info, String type, Long id) {
        List<FinishedProductOutputResult> outputResults = info.getOutputResults();
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        FinishedProductOutputResult first = CollUtil.getFirst(outputResults);
        String value = null;
        switch (enumByValue) {
            case PRODUCT_OUTPUT_SUMMARY_NAME:
                value = first.getProductName();
                break;
            case PRODUCT_OUTPUT_SUMMARY_CODE:
                value = first.getProductMergeCode();
                break;
            case PRODUCT_OUTPUT_SUMMARY_SPECIFICATION:
                value = first.getSpecification();
                break;
            case PRODUCT_OUTPUT_SUMMARY_OUTPUT_TOTAL:
                Map<Long, List<FinishedProductOutputResult>> unitMap = CollectionUtils.convertMultiMap(outputResults,
                        FinishedProductOutputResult::getUnitId);
                List<String> collect = unitMap.entrySet().stream().map(entry -> {
                    List<FinishedProductOutputResult> resultList = entry.getValue();
                    BigDecimal result = resultList.stream().map(e -> {
                        Integer number = e.getNumber();
                        BigDecimal singleQuantity = e.getSingleQuantity();
                        return singleQuantity.multiply(new BigDecimal(number));
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);
                    String globalUnitName = unitCache.getGlobalUnitName(entry.getKey());
                    return result.stripTrailingZeros().toPlainString() + globalUnitName;
                }).collect(Collectors.toList());
                value = StrUtil.join("+", collect);
                break;
            case PRODUCT_OUTPUT_SUMMARY_SIZE_TOTAL:
                value = String.valueOf(outputResults.stream().mapToInt(FinishedProductOutputResult::getNumber).sum());
                break;
        }
        return value;
    }
}
