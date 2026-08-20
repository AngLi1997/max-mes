package com.bmos.platform.service.equipment.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.datasource.dto.MqttAccreditInfoDTO;
import com.bmos.platform.service.equipment.service.dto.DataPointNameValueDTO;
import com.bmos.platform.service.equipment.service.dto.DataPointValuePageQueryDTO;

import java.util.List;

/**
 * @author yigaohui
 * @date 设备接口
 **/
public interface EquipmentService {

    /**
     * 通过设备id批量获取设备点位数据
     *
     * @param equipmentId 设备id
     * @return 查询结果
     */
    List<DataPointNameValueDTO> getData(Long equipmentId);


    /**
     * 获取点位的历史数据
     *
     * @param dataPointValuePageQueryDTO 分页查询DTO
     * @return
     */
    CommonPage<DataPointNameValueDTO> getHistoryData(DataPointValuePageQueryDTO dataPointValuePageQueryDTO);


    /**
     * 获取hub mqtt授权
     * @return 获取结果
     */
    MqttAccreditInfoDTO getMqttAccreditInfo();
}
