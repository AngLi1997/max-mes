package com.bmos.platform.service.factory.repository;

import com.bmos.platform.service.factory.model.EquipmentStation;

import java.util.List;
import java.util.Map;

public interface FactoryStationRepository {

    /**
     * 判断模型id下是否含有工位
     * @param moduleId
     * @return
     */
    boolean existStation(Long moduleId);

    /**
     * 根据房间id获取房间下的所有工位信息
     * @param longs
     * @return
     */
    List<EquipmentStation> getStationByRoomIds(List<Long> longs);

    /**
     * 根据id集合查询工位信息
     * @param idList
     * @return
     */
    List<EquipmentStation> selectByStationIdList(List<Long> idList);

    /**
     * 业务配置 绑定/解除工位
     * @param stationUseMap
     */
    void useStationCount(Map<Long, Boolean> stationUseMap);
}
