package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配液产出组件
 */
@Component("LIQUID_PREPARATION_OUTPUT")
public class PreparationProduceComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    PreparationProduceDetailComponentStrategy detailComponentStrategy;

    @Autowired
    PreparationProduceSummaryComponentStrategy summaryComponentStrategy;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        Map<String, List<ComponentListVO>> componentTypeMap = component.getChildren().stream().collect(
                Collectors.groupingBy(ComponentListVO::getComponentType));
        for (String componentType : componentTypeMap.keySet()) {
            List<ComponentListVO> componentListVOS = componentTypeMap.get(componentType);
            if (BusinessComponentTypeEnum.LIQUID_PREPARATION_OUTPUT_DETAILS.getValue().equals(componentType)){
                for (int i = 0; i < componentListVOS.size(); i++) {
                    detailComponentStrategy.handleBusinessComponent(results, componentListVOS.get(i), info, configMap, i);
                }
            } else {
                List<ComponentListVO> summaryCollect = componentListVOS.stream().filter(c -> ObjectUtil.isNotNull(configMap.get(c.getId()))).collect(Collectors.toList());
                if (CollUtil.isEmpty(summaryCollect)){
                    for (int i = 0; i < componentListVOS.size(); i++) {
                        summaryComponentStrategy.handleBusinessComponent(results, componentListVOS.get(i), info, configMap, i);
                    }
                } else {
                    for (int i = 0; i < summaryCollect.size(); i++) {
                        summaryComponentStrategy.handleBusinessComponent(results, summaryCollect.get(i), info, configMap, i);
                    }
                }
            }
        }
    }
}
