package com.bmos.mes.service.record.business.strategy;

import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationProduceDetailInfo;
import com.bmos.mes.service.record.business.model.preparation.PreparationProduceMaterialInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 配液投入汇总组件
 */
@Component("LIQUID_PREPARATION_OUTPUT_SUMMARY")
public class PreparationProduceSummaryComponentStrategy implements BusinessComponentStrategy {
    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {

        PreparationProduceDetailInfo detailInfo = (PreparationProduceDetailInfo) info;
        List<PreparationProduceMaterialInfo> allPreparationProduceMaterialInfo = Lists.newArrayList(detailInfo.getPreparationProduceMaterialInfos());
        // 根据物料id进行分类
        if (index > 0){
            return ;
        }
        summary(allPreparationProduceMaterialInfo, results, component, info.getDto());
    }


    private void summary(List<PreparationProduceMaterialInfo> preparationProduceMaterialInfos, List<ExecuteFormData> results,
                         ComponentListVO componentListVO, BusinessComponentBatchSaveDTO dto){
        for (ComponentListVO child : componentListVO.getChildren()) {
            ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(dto);
            formData.setFieldId(child.getFieldId());
            formData.setComponentType(child.getComponentType());
            formData.setDiscard(false);
            formData.setValue(this.getValueByType(child.getComponentType(), preparationProduceMaterialInfos));
            results.add(formData);
        }
    }

    private String getValueByType(String componentType, List<PreparationProduceMaterialInfo> preparationProduceMaterialInfos) {
        // 计算总量
        BigDecimal quantity = BigDecimal.ZERO;
        Integer count = 0;
        for (PreparationProduceMaterialInfo preparationProduceMaterialInfo : preparationProduceMaterialInfos) {
            // 汇总剔除作废的
            if (PrepareSignStatusEnum.SCRAPED.getValue().equals(preparationProduceMaterialInfo.getSignStatus().getValue())){
                continue;
            }
            count++;
            quantity = quantity.add(preparationProduceMaterialInfo.getQuantity());
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_SUMMARY_NAME.getValue().equals(componentType)){
            return preparationProduceMaterialInfos.get(0).getMaterialName();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_SUMMARY_CODE.getValue().equals(componentType)){
            return preparationProduceMaterialInfos.get(0).getMaterialCode();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_SUMMARY_SPECIFICATION.getValue().equals(componentType)){
            return preparationProduceMaterialInfos.get(0).getSpecification();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_SUMMARY_TOTAL_QUANTITY.getValue().equals(componentType)){
            return String.valueOf(quantity);
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_SUMMARY_UNIT.getValue().equals(componentType)){
            return preparationProduceMaterialInfos.get(0).getUnit();
        }
        if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_SUMMARY_PARTNO.getValue().equals(componentType)){
            return String.valueOf(String.valueOf(count));
        }
        return null;
    }

}
