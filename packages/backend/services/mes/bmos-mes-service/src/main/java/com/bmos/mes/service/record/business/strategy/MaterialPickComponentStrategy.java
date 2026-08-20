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
 * 按物料量领料
 */
@Service(value = "MATERIAL_QUANTITY_PICK")
public class MaterialPickComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private MaterialPickMaterialComponentStrategy materialComponentStrategy;

    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        List<ComponentListVO> collect =
                children.stream().filter(c -> ObjectUtil.isNotNull(configMap.get(c.getId()))).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            for (int i = 0; i < children.size(); i++) {
                ComponentListVO child = children.get(i);
                materialComponentStrategy.handleBusinessComponent(results, child, info, configMap, i);
            }
            return;
        }
        children.forEach(child -> {
            materialComponentStrategy.handleBusinessComponent(results, child, info, configMap, null);
        });
    }

}
