package com.bmos.platform.service.factory.service;

import com.bmos.platform.service.factory.model.EquipmentStationUser;
import com.bmos.platform.service.factory.service.dto.UserBindStationsDTO;

import java.util.List;
import java.util.Map;

public interface EquipmentStationUserService {

    Boolean saveUserList(List<EquipmentStationUser> stationUserList);

    List<String> getStationUserNameByStationId(Long id);

    List<String> getUserIdListByStationId(Long stationId);

    List<EquipmentStationUser> getStationUserByStationId(Long stationId);

    Boolean deleteById(List<EquipmentStationUser> stationUser);

    Map<Long,List<String>> queryStationUserByStationIdList(List<Long> stationIdList);

    List<String> getUserIdListByStationIdList(List<Long> stationIdList);

    /**
     * 根据用户id获取工位信息
     * @param userId
     * @return
     */
    List<EquipmentStationUser> getStationByUserId(String userId);

    /**
     * 校验工位id是否绑定人员
     * @param stationId
     * @return
     */
    boolean existByStationId(Long stationId);

    /**
     * 用户绑定工位
     * @param dto
     */
    void userBindStations(UserBindStationsDTO dto);

    /**
     * 根据用户id获取工位id
     * @param userId
     * @return
     */
    List<Long> userStationList(String userId);

    /**
     * 根据用户id删除与用户的绑定关系
     * @param id
     */
    void deleteStationUserByStationId(Long id);
}
