package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Service("EQUIPMENT_INFO_CUSTOM_FIELD")
public class EquipmentCustomFieldComponentStrategy implements BusinessComponentStrategy {

    private static final String CONFIG_JSON_FIELD_CODE = "fieldData";

    private static final String DEFAULT_EMPTY_VALUE_CODE = "mes.record.empty-data";

    @Autowired
    private PlatformParameterClientImpl platformParameterClient;

    @Override
    public void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {

        BusinessComponentBatchSaveDTO dto = info.getDto();
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        String componentDetail = component.getComponentDetail();
        JSONObject jsonObject = new JSONObject(componentDetail);
        String fieldCode = jsonObject.getStr(CONFIG_JSON_FIELD_CODE);
        EquipmentInfoFeignVO equipmentInfoFeignVO = info.getEquipmentInfo();
        List<EquipmentPropertyFeignVO> infoPropertyList = equipmentInfoFeignVO.getInfoPropertyList();
        // 如果没有属性，表示没有配置属性，直接返回空值
        if (CollectionUtils.isEmpty(infoPropertyList)) {
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        // 从设备属性列表找到code等于fieldCode的属性
        Optional<EquipmentPropertyFeignVO> first =
                infoPropertyList.stream().filter(item -> StringUtils.equals(item.getCode(), fieldCode)).findFirst();
        if (!first.isPresent()) {
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        EquipmentPropertyFeignVO equipmentPropertyFeignVO = first.get();
        if (StrUtil.isBlank(equipmentPropertyFeignVO.getValue())) {
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        convert.setValue(equipmentPropertyFeignVO.getValue());
        results.add(convert);
    }
}
