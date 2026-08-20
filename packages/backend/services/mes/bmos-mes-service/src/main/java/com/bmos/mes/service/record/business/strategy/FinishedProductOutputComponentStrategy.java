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
 * 成品产出组件
 */
@Service(value = "PRODUCT_OUTPUT")
public class FinishedProductOutputComponentStrategy implements BusinessComponentStrategy {

    @Resource
    private FinishedProductOutputDetailComponentStrategy detailComponentStrategy;

    @Resource
    private FinishedProductOutputSummaryComponentStrategy summaryComponentStrategy;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        Map<String, List<ComponentListVO>> childMap = CollectionUtils.convertMultiMap(children, ComponentListVO::getComponentType);
        List<ComponentListVO> detail = childMap.get(BusinessComponentTypeEnum.PRODUCT_OUTPUT_DETAILS.getValue());
        if (CollUtil.isNotEmpty(detail)) {
            for (int i = 0; i < detail.size(); i++) {
                ComponentListVO child = detail.get(i);
                detailComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        }
        List<ComponentListVO> summary = childMap.get(BusinessComponentTypeEnum.PRODUCT_OUTPUT_SUMMARY.getValue());
        if (CollUtil.isNotEmpty(summary)) {
            for (int i = 0; i < summary.size(); i++) {
                ComponentListVO child = summary.get(i);
                summaryComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
        }
    }

}
