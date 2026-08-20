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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 投料回收组件
 */
@Service(value = "FEED_RECYCLE")
public class ChargeRecycleComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private ChargeRecycleDetailComponentStrategy detailComponentStrategy;

    @Resource
    private ChargeRecycleSumComponentStrategy sumComponentStrategy;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        Map<String, List<ComponentListVO>> childMap = CollectionUtils.convertMultiMap(children,
                ComponentListVO::getComponentType);
        List<ComponentListVO> detail = childMap.get(BusinessComponentTypeEnum.FEED_RECYCLE_FEEDING_DETAILS.getValue());
        if (CollUtil.isNotEmpty(detail)) {
            for (int i = 0; i < detail.size(); i++) {
                ComponentListVO child = detail.get(i);
                detailComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        }
        List<ComponentListVO> summary = childMap.get(BusinessComponentTypeEnum.FEED_RECYCLE_SUMMARY.getValue());
        if (CollUtil.isNotEmpty(summary)) {
            handleChildren(results, info, configMap, summary);
        }
    }

    private void handleChildren(List<ExecuteFormData> results, ProductionDetailInfo info, Map<Long,
            BusinessComponentConfigDetailVO> configMap, List<ComponentListVO> summary) {
        List<ComponentListVO> summaryCollect =
                summary.stream().filter(c -> ObjectUtil.isNotNull(configMap.get(c.getId()))).collect(Collectors.toList());
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
