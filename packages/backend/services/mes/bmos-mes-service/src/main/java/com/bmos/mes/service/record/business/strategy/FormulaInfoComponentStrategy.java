package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
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
 * 配方信息业务组件处理
 */
@Service(value = "BUSINESS_FORMULA_INFO")
public class FormulaInfoComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private FormulaMaterialComponentStrategy formulaMaterialComponentStrategy;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        // 已修改为componentId
        List<ComponentListVO> collect =
                children.stream().filter(c -> ObjectUtil.isNotNull(configMap.get(c.getId()))).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            for (int i = 0; i < children.size(); i++) {
                ComponentListVO child = children.get(i);
                formulaMaterialComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
            return;
        }
        children.forEach(child -> {
            formulaMaterialComponentStrategy.handleBusinessComponent(results, child, info, configMap, null);
        });
    }
}
