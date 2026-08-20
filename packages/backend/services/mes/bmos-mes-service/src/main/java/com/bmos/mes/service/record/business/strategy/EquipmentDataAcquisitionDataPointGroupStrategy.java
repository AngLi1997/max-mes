package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 数采组件-数采点策略
 *
 * @author yigaohui
 * @date 2024/4/24
 **/
@Service("EQUIPMENT_DATA_ACQUISITION_GROUP")
public class EquipmentDataAcquisitionDataPointGroupStrategy implements BusinessComponentStrategy {


    @Autowired
    private EquipmentDataAcquisitionTimeStrategy timeComponentStrategy;

    @Autowired
    private EquipmentDataAcquisitionDataPointStrategy dataAcquisitionDataPointStrategy;

    @Resource
    private EquipmentExpandTableStrategy equipmentExpandTableStrategy;


    @Override
    public void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        if (CollectionUtil.isEmpty(children)) {
            return;
        }
        children.forEach(item -> {
            // 设备数采组件放在根节点上的,将自己的配置全部替换成根节点的配置
            // 设备数采组件放在根节点上的,将自己的配置全部替换成根节点的配置
            configMap.put(item.getId(), configMap.get(component.getId()));
            if (item.getComponentType().equals("EQUIPMENT_DATA_ACQUISITION_TIME")) {
                timeComponentStrategy.handleBusinessComponent(results, item, info, configMap, null);
            }
            if (item.getComponentType().equals("CUSTOM_FIELD")) {
                dataAcquisitionDataPointStrategy.handleBusinessComponent(results, item, info, configMap, null);
            }
            if ("EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE".equals(item.getComponentType())) {
                equipmentExpandTableStrategy.handleBusinessComponent(results, item, info, configMap, null);
            }
        });
    }
}
