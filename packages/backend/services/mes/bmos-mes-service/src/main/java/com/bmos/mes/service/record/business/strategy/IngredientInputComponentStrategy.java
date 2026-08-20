package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配料投入
 */
@Service(value = "INGREDIENTS_INPUT")
public class IngredientInputComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private IngredientInputDetailComponentStrategy detailComponentStrategy;

    @Autowired
    private IngredientInputSummaryComponentStrategy summaryComponentStrategy;


    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        Map<String, List<ComponentListVO>> childMap = CollectionUtils.convertMultiMap(children, ComponentListVO::getComponentType);
        List<ComponentListVO> details = childMap.get(BusinessComponentTypeEnum.INGREDIENTS_INPUT_FEEDING_DETAILS.getValue());
        if(CollUtil.isNotEmpty(details)){
            for (int i = 0; i < details.size(); i++) {
                ComponentListVO child = details.get(i);
                detailComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        }
        List<ComponentListVO> summary = childMap.get(BusinessComponentTypeEnum.INGREDIENTS_INPUT_SUMMARY.getValue());
        if(CollUtil.isNotEmpty(summary)){
            handleChildren(results, info, configMap, summary);
        }
    }

    private void handleChildren(List<ExecuteFormData> results, ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, List<ComponentListVO> summary) {
        List<ComponentListVO> summaryCollect = summary.stream().filter(c -> ObjectUtil.isNotNull(configMap.get(c.getId()))).collect(Collectors.toList());
        if (CollUtil.isEmpty(summaryCollect)) {
            for (int i = 0; i < summary.size(); i++) {
                ComponentListVO child = summary.get(i);
                summaryComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        } else {
            summary.forEach(child -> {
                summaryComponentStrategy.handleBusinessComponent(results, child, info, configMap, null);
            });
        }
    }


}
