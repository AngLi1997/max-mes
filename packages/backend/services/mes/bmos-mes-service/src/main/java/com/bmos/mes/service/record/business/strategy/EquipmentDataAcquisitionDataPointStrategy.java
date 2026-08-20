package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionPointDTO;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyAcquisitionPointFeignVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import springfox.documentation.spring.web.json.Json;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 数采组件-数采点策略
 *
 * @author yigaohui
 * @date 2024/4/24
 **/
@Service("EQUIPMENT_DATA_CUSTOM_FIELD")
@Slf4j
public class EquipmentDataAcquisitionDataPointStrategy implements BusinessComponentStrategy {

    @Autowired
    private PlatformParameterClientImpl platformParameterClient;

    private static final String CONFIG_JSON_FIELD_CODE = "fieldData";

    private static final String DEFAULT_EMPTY_VALUE_CODE = "mes.record.empty-data";

    private static final String EQUIPMENT_DATA_ATTR_LIST = "equipmentDataAttrList";

    private static final String COMPONENT_DETAIL = "componentDetail";

    private static final String EQUIPMENT_ATTR_ROUND_CODE = "roundCode";

    private static final String EQUIPMENT_ATTR_PRECISION = "precision";

    @Override
    public void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        String componentDetail = component.getComponentDetail();
        BusinessComponentBatchSaveDTO dto = info.getDto();
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        if (StringUtils.isEmpty(componentDetail)) {
            log.info("组件没有配置设备数据信息，默认填充空值");
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        // 从设备属性列表找到code等于fieldCode的属性
        EquipmentInfoFeignVO equipmentInfo = info.getEquipmentInfo();
        List<EquipmentPropertyAcquisitionPointFeignVO> dataPropertyList = equipmentInfo.getDataPropertyList();
        // 如果没有属性，表示没有配置属性，直接返回空值
        if (CollectionUtils.isEmpty(dataPropertyList)) {
            log.info("设备没有配置数据信息，默认填充空值");
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        // 从设备属性列表找到code等于fieldCode的属性
        JSONObject jsonObject = new JSONObject(componentDetail);
        String fieldCode = jsonObject.getStr(CONFIG_JSON_FIELD_CODE);
        Optional<EquipmentPropertyAcquisitionPointFeignVO> first =
                dataPropertyList.stream().filter(item -> com.alibaba.cloud.commons.lang.StringUtils.equals(item.getCode(), fieldCode)).findFirst();
        if (!first.isPresent()) {
            log.info("设备没有找到与组件上配置的设备数据一致的设备数据字段，默认填充空值");
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        List<EquipmentAcquisitionPointDTO> equipmentAcquisitionPointList = info.getEquipmentAcquisitionPointList();
        if (CollectionUtils.isEmpty(equipmentAcquisitionPointList)) {
            log.info("数采的数据为空，默认填充空值");
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        EquipmentPropertyAcquisitionPointFeignVO equipmentPropertyAcquisitionPointFeignVO = first.get();
        Optional<EquipmentAcquisitionPointDTO> equipmentAcquisitionPointDTO;
        if (equipmentPropertyAcquisitionPointFeignVO.getAcquisitionPointCode() != null) {
            // 找到和设备数据匹配的设备数采点的数据
            equipmentAcquisitionPointDTO = equipmentAcquisitionPointList.stream()
                    .filter(item -> com.alibaba.cloud.commons.lang.StringUtils.equals(item.getAcquisitionCode(),
                            equipmentPropertyAcquisitionPointFeignVO.getAcquisitionPointCode()))
                    .findFirst();
        }else {
            // 设备数据没有匹配采集点，直接根据设备数据进行值匹配
            equipmentAcquisitionPointDTO = equipmentAcquisitionPointList.stream()
                    .filter(item -> com.alibaba.cloud.commons.lang.StringUtils.equals(item.getDataPropertyCode(),
                            equipmentPropertyAcquisitionPointFeignVO.getCode()))
                    .findFirst();
        }
        if (!equipmentAcquisitionPointDTO.isPresent()) {
            log.info("数采数据中没有找到和设备数据匹配的数采点数据，默认填充空值");
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        if (StringUtils.isEmpty(equipmentAcquisitionPointDTO.get().getDataPointValue())) {
            log.info("数采点数据为空，默认填充空值");
            String valueByCode = platformParameterClient.getValueByCode(DEFAULT_EMPTY_VALUE_CODE);
            convert.setValue(valueByCode);
            results.add(convert);
            return;
        }
        convert.setValue(revising(configMap.get(component.getId()),
                fieldCode,
                equipmentAcquisitionPointDTO.get().getDataPointValue()));
        results.add(convert);
    }

    private String revising(BusinessComponentConfigDetailVO businessComponentConfigDetailVO, String code,
                            String dataPointValue) {
        if (businessComponentConfigDetailVO == null || StringUtils.isEmpty(businessComponentConfigDetailVO.getConfigInfo())) {
            return dataPointValue;
        }
        if (!NumberUtil.isNumber(dataPointValue)) {
            return dataPointValue;
        }

        JSONObject jsonObject = new JSONObject(businessComponentConfigDetailVO.getConfigInfo());
        JSONArray dataAttrList = jsonObject.getJSONArray(EQUIPMENT_DATA_ATTR_LIST);
        if (CollectionUtils.isEmpty(dataAttrList)) {
            return dataPointValue;
        }
        JSONObject equipmentAttr = null;
        for (int i = 0; i < dataAttrList.size(); i++) {
            JSONObject entry = dataAttrList.getJSONObject(i);
            JSONObject component = entry.getJSONObject(COMPONENT_DETAIL);
            String dataFieldCode = component.getStr(CONFIG_JSON_FIELD_CODE);
            if (com.alibaba.cloud.commons.lang.StringUtils.equals(dataFieldCode, code)) {
                equipmentAttr = entry;
            }
        }
        if (equipmentAttr == null) {
            return dataPointValue;
        }
        String roundCode = equipmentAttr.getStr(EQUIPMENT_ATTR_ROUND_CODE);
        Integer precision = equipmentAttr.getInt(EQUIPMENT_ATTR_PRECISION);
        if (StringUtils.isEmpty(roundCode) || precision == null) {
            return dataPointValue;
        }
        return BusinessComponentStrategy.roundingOff(new BigDecimal(dataPointValue),
                BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10,
                        precision))),
                precision,
                RoundingMode.valueOf(roundCode)).toPlainString();
    }
}
