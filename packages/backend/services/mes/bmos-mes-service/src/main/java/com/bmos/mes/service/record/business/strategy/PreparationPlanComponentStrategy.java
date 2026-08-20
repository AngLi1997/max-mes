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
 * 配液计划策略
 */
@Service("LIQUID_PREPARATION_PLAN")
public class PreparationPlanComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private PreparationPlanBatchComponentStrategy batchComponentStrategy;

    @Autowired
    private PreparationPlanSumComponentStrategy sumComponentStrategy;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        Map<String, List<ComponentListVO>> childMap = CollectionUtils.convertMultiMap(children, ComponentListVO::getComponentType);
        // 批次分组
        List<ComponentListVO> batchList = childMap.get(BusinessComponentTypeEnum.LIQUID_PREPARATION_PLAN_BATCH.getValue());
        if (CollUtil.isNotEmpty(batchList)) {
            for (int i = 0; i < batchList.size(); i++) {
                ComponentListVO child = batchList.get(i);
                batchComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        }
        // 汇总分组
        List<ComponentListVO> summary = childMap.get(BusinessComponentTypeEnum.LIQUID_PREPARATION_PLAN_SUMMARY.getValue());
        handleSummaryChildren(results, info, configMap, summary);
    }

    private void handleSummaryChildren(List<ExecuteFormData> results, ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, List<ComponentListVO> summary) {
        if (CollUtil.isEmpty(summary)) {
            return;
        }
        List<ComponentListVO> summaryCollect = summary.stream().filter(c -> ObjectUtil.isNotNull(configMap.get(c.getId()))).collect(Collectors.toList());
        if (CollUtil.isEmpty(summaryCollect)) {
            for (int i = 0; i < summary.size(); i++) {
                ComponentListVO child = summary.get(i);
                sumComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        } else {
            summary.forEach(child -> {
                sumComponentStrategy.handleBusinessComponent(results, child, info, configMap, null);
            });
        }
    }
}
