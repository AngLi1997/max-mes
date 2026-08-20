package com.bmos.platform.service.equipment.datasource;

import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.datasource.dto.MqttAccreditInfoDTO;
import com.bmos.platform.service.equipment.service.dto.DataPointNameValueDTO;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author yigaohui
 * @date 数据源策略
 **/
public interface DataSourceStrategy {

    /**
     * 获取单个点位的值，以String 的形式返回
     *
     * @param dataPointName 点位名称
     * @return 获取结果
     */
    DataPointNameValueDTO getData(String dataPointName);

    /**
     * 批量获取值，以name value 的形式返回
     *
     * @return 查询结果
     */
    List<DataPointNameValueDTO> getData(Set<String> dataPointNames);


    /**
     * 写入单个点位的值
     *
     * @param dataPointWriteValueDTO 点位数据
     */
    void writeDataPointValue(DataPointNameValueDTO dataPointWriteValueDTO);


    /**
     * 批量写入点位的值
     *
     * @param dataPointWriteValueDTOS
     */
    void writeDataPointValue(List<DataPointNameValueDTO> dataPointWriteValueDTOS);


    /**
     * 分页获取点位的历史数据
     *
     * @param dataPointName 点位名称
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param dataType      数据的类型
     * @param basePage      分页条件
     * @return hub的数据值
     */
    CommonPage<DataPointNameValueDTO> getHistory(String dataPointName, LocalDateTime startTime, LocalDateTime endTime,
                                                 AcquisitionPointDataTypeEnum dataType, BasePage basePage);


    /**
     * 获取hub mqtt 授权
     * @return hub授权信息
     */
    MqttAccreditInfoDTO getMqttAccreditInfo();
}
