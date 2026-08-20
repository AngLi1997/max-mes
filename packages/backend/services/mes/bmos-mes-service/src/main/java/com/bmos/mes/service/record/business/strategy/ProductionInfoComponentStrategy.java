package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 生产信息业务组件处理
 */
@Service(value = "BUSINESS_PRODUCT_INFO")
public class ProductionInfoComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private UnitCache unitCache;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        results.addAll(component.getChildren().stream().filter(e -> ObjectUtil.isNotNull(e.getFieldId())).map(e -> {
            ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(info.getDto());
            convert.setValue(this.getValueByType(info, e.getComponentType()));
            convert.setFieldId(e.getFieldId());
            convert.setComponentType(e.getComponentType());
            return convert;
        }).collect(Collectors.toList()));
    }

    String getValueByType(ProductionDetailInfo info, String type) {
        String value = null;
        Plan plan = Optional.ofNullable(info.getPlan()).orElse(new Plan());
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        switch (enumByValue) {
            case BUSINESS_PRODUCT_INFO_SPECIFICATION:
                value = plan.getProductSpecification();
                break;
            case BUSINESS_PRODUCT_INFO_CODE:
                value = plan.getProductMergeCode();
                break;
            case BUSINESS_PRODUCT_INFO_PROCESS_NAME:
                value = plan.getProcessName();
                break;
            case BUSINESS_PRODUCT_INFO_BATCHNO:
                value = plan.getBatchNo();
                break;
            case BUSINESS_PRODUCT_INFO_UNIT:
                value = unitCache.getGlobalUnitName(plan.getUnitId());
                break;
            case BUSINESS_PRODUCT_INFO_NAME:
                value = plan.getProductName();
                break;
            case BUSINESS_PRODUCT_INFO_BATCH:
                value = BusinessComponentStrategy.getDecimalStripString(plan.getBatchQuantity());
                break;
            case BUSINESS_PRODUCT_INFO_REVISION_NUMBER:
                value = String.valueOf(Optional.ofNullable(plan.getModifyCount()).orElse(0));
                break;
        }
        return value;
    }
}
