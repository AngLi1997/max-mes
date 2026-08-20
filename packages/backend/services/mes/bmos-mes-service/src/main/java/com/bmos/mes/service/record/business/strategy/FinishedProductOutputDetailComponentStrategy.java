package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成品产出-->产出详情
 */
@Service(value = "PRODUCT_OUTPUT_DETAILS")
public class FinishedProductOutputDetailComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        List<FinishedProductOutputResult> outputResults = info.getOutputResults();
        if(index >= outputResults.size()){
            return;
        }
        Collection<FormDataItemVO> formDataCollection = info.getFormDataCollection();
        Set<Long> existedIds = CollectionUtils.convertSet(formDataCollection, FormDataItemVO::getFieldId);
        FinishedProductOutputResult result = outputResults.get(index);
        results.addAll(component.getChildren()
                .stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(e -> {
                    if (existedIds.contains(e.getFieldId())) {
                        return null;
                    }
                    ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
                    BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(e.getComponentType());
                    convert.setFieldId(e.getFieldId());
                    convert.setComponentType(e.getComponentType());
                    convert.setValue(getValueByType(info, enumByValue, result.getId()));
                    convert.setExtInfo(this.buildExtInfo(enumByValue, convert.getValue()));
                    return convert;
                }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private String buildExtInfo(BusinessComponentTypeEnum componentType, String value) {
        ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
        switch (componentType) {
            case PRODUCT_OUTPUT_DETAILS_OPERATE_TIME:
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
        LocalDateTime parse = LocalDateTimeUtil.parse(value, DatePattern.NORM_DATETIME_PATTERN);
        ZonedDateTime zonedDateTime = parse.atZone(ZoneId.systemDefault());
        return String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }

    private String getValueByType(ProductionDetailInfo info, BusinessComponentTypeEnum enumByValue, Long id) {
        // 不直接传以适配以前的getValueByType方法 后续修改
        List<FinishedProductOutputResult> outputResults = info.getOutputResults();
        Map<Long, FinishedProductOutputResult> map = CollectionUtils.convertMap(outputResults, FinishedProductOutputResult::getId);
        FinishedProductOutputResult result = map.get(id);
        String value = null;
        switch (enumByValue) {
            case PRODUCT_OUTPUT_DETAILS_NAME:
                value = result.getProductName();
                break;
            case PRODUCT_OUTPUT_DETAILS_CODE:
                value = result.getProductMergeCode();
                break;
            case PRODUCT_OUTPUT_DETAILS_SPECIFICATION:
                value = result.getSpecification();
                break;
            case PRODUCT_OUTPUT_DETAILS_BATCH_NO:
                value = result.getProductBatchNo();
                break;
            case PRODUCT_OUTPUT_DETAILS_SINGLE_QUANTITY:
                value = result.getSingleQuantity().stripTrailingZeros().toPlainString();
                break;
            case PRODUCT_OUTPUT_DETAILS_UNIT:
                value = unitCache.getGlobalUnitName(result.getUnitId());
                break;
            case PRODUCT_OUTPUT_DETAILS_SIZE:
                value = String.valueOf(result.getNumber());
                break;
            case PRODUCT_OUTPUT_DETAILS_OPERATOR:
                value = UserUtils.getUsername(result.getOperatorId());
                break;
            case PRODUCT_OUTPUT_DETAILS_OPERATE_TIME:
                value = DateUtil.format(result.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN);
                break;
        }
        return value;
    }
}
