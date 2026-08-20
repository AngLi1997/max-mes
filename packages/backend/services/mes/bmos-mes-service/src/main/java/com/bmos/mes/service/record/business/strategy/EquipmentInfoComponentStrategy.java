package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 设备信息处理组件
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
@Service("EQUIPMENT_INFO")
public class EquipmentInfoComponentStrategy implements BusinessComponentStrategy {

    @Autowired
    private EquipmentNameComponentStrategy equipmentNameComponentStrategy;
    @Autowired
    private EquipmentCodeComponentStrategy equipmentCodeComponentStrategy;

    @Autowired
    private EquipmentCustomFieldComponentStrategy equipmentCustomFieldComponentStrategy;

    @Override
    public void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        List<ComponentListVO> children = component.getChildren();
        if (CollectionUtil.isEmpty(children)) {
            return;
        }
        children.forEach(item -> {
            if (item.getComponentType().equals(BusinessComponentTypeEnum.EQUIPMENT_INFO_NAME.getValue())) {
                equipmentNameComponentStrategy.handleBusinessComponent(results, item, info, configMap, null);
            }
            if (item.getComponentType().equals(BusinessComponentTypeEnum.EQUIPMENT_INFO_CODE.getValue())) {
                equipmentCodeComponentStrategy.handleBusinessComponent(results, item, info, configMap, null);
            }
            if (item.getComponentType().equals(BusinessComponentTypeEnum.CUSTOM_FIELD.getValue())) {
                equipmentCustomFieldComponentStrategy.handleBusinessComponent(results, item, info, configMap, null);
            }
        });
    }
}
