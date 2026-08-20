package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.datasource.DataSourceStrategy;
import com.bmos.platform.service.equipment.datasource.dto.MqttAccreditInfoDTO;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.service.AcquisitionPointService;
import com.bmos.platform.service.equipment.service.EquipmentService;
import com.bmos.platform.service.equipment.service.EquipmentTagService;
import com.bmos.platform.service.equipment.service.data.EquipmentPropertyData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagData;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointDTO;
import com.bmos.platform.service.equipment.service.dto.DataPointNameValueDTO;
import com.bmos.platform.service.equipment.service.dto.DataPointValuePageQueryDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yigaohui
 * @date 设备service实现类
 **/
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Resource
    private DataSourceStrategy dataSourceStrategy;

    @Resource
    private AcquisitionPointService acquisitionPointService;

    @Resource
    private EquipmentTagService tagService;


    @Override
    public List<DataPointNameValueDTO> getData(Long equipmentId) {
        EquipmentTagData equipmentProperty = tagService.getEquipmentProperty(equipmentId);
        if (equipmentProperty == null || CollectionUtils.isEmpty(equipmentProperty.getDataPropertyList())) {
            return new ArrayList<>();
        }
        List<Long> acquisitionIds =
                equipmentProperty.getDataPropertyList().stream().map(EquipmentPropertyData::getValue).map(Long::valueOf).collect(Collectors.toList());
        List<AcquisitionPointDTO> acquisitionPointDTOS = acquisitionPointService.getList(acquisitionIds);
        List<DataPointNameValueDTO> data =
                dataSourceStrategy.getData(acquisitionPointDTOS.stream().map(AcquisitionPointDTO::getDataPointName).collect(Collectors.toSet()));
        Map<String, DataPointNameValueDTO> dataPointValue =
                data.stream().collect(Collectors.toMap(DataPointNameValueDTO::getDataPointName, Function.identity()));
        return acquisitionPointDTOS.stream().map(item -> {
            DataPointNameValueDTO dataPointNameValueDTO = new DataPointNameValueDTO();
            DataPointNameValueDTO pointNameValueDTO = dataPointValue.get(item.getDataPointName());
            if (pointNameValueDTO != null) {
                dataPointNameValueDTO.setValue(pointNameValueDTO.getValue());
                dataPointNameValueDTO.setTimeStamp(pointNameValueDTO.getTimeStamp());
                dataPointNameValueDTO.setDataPointName(item.getDataPointName());
                dataPointNameValueDTO.setAcquisitionPointId(item.getId());
                dataPointNameValueDTO.setAcquisitionPointCode(item.getCode());
                dataPointNameValueDTO.setEquipmentId(equipmentId);
            }
            return dataPointNameValueDTO;
        }).collect(Collectors.toList());
    }


    @Override
    public CommonPage<DataPointNameValueDTO> getHistoryData(DataPointValuePageQueryDTO dataPointValuePageQueryDTO) {
        EquipmentTagData equipmentProperty = tagService.getEquipmentProperty(dataPointValuePageQueryDTO.getEquipmentId());
        if (equipmentProperty == null || CollectionUtils.isEmpty(equipmentProperty.getDataPropertyList())) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        List<Long> acquisitionIds =
                equipmentProperty.getDataPropertyList().stream().map(EquipmentPropertyData::getValue).map(Long::valueOf).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(acquisitionIds) || acquisitionIds.stream().noneMatch(item -> item.equals(dataPointValuePageQueryDTO.getAcquisitionPointId()))) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        AcquisitionPoint acquisitionPoint = acquisitionPointService.getById(dataPointValuePageQueryDTO.getAcquisitionPointId());
        CommonPage<DataPointNameValueDTO> historyData =
                dataSourceStrategy.getHistory(acquisitionPoint.getDataPointName(),
                        dataPointValuePageQueryDTO.getStartTime(), dataPointValuePageQueryDTO.getEndTime(), acquisitionPoint.getDataType(),
                        BeanUtil.toBean(dataPointValuePageQueryDTO, BasePage.class));
        historyData.getList().forEach(item -> {
            item.setEquipmentId(dataPointValuePageQueryDTO.getEquipmentId());
            item.setAcquisitionPointId(dataPointValuePageQueryDTO.getAcquisitionPointId());
        });
        return historyData;
    }


    @Override
    public MqttAccreditInfoDTO getMqttAccreditInfo() {
        return dataSourceStrategy.getMqttAccreditInfo();
    }
}
