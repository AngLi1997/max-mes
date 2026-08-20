package com.bmos.platform.service.factory.service;

import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.EquipmentStationInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface EquipmentStationInfoService {

    List<String> getStationInfoNameListByStationId(Long id);

    Boolean bindEquipment(List<EquipmentStationInfo> stationUserList);

    List<EquipmentStationInfo> queryStationInfoByEquipmentId(Long id);

    List<Long> queryInfoIdListByUserId(String userId);

    List<EquipmentStationInfo> queryEquipmentByStationId(Long stationId);

    Boolean deletcByIdList(List<EquipmentStationInfo> stationInfos);

    Map<Long,List<Long>> queryStationInfoByStationIdList(List<Long> convertList);

    List<EquipmentStation> queryStationNameByEquipmentId(Long id);

    /**
     * 根据id获取设备与工位的绑定关系
     * @param equipmentId
     * @return
     */
    List<EquipmentStationInfo> selectByEquipmentId(Long equipmentId);

    /**
     * 校验工位是否绑定设别
     * @param stationId
     * @return
     */
    boolean existByStationId(Long stationId);

    /**
     * 根据工位id查询工位信息
     * @param stationId
     * @return
     */
    @Nullable
    FactoryStationFeignVO queryStationById(Long stationId);

    /**
     * 根据工位id删除与设别的绑定关系
     * @param stationId
     */
    void deleteRelationByStationId(Long stationId);

}
