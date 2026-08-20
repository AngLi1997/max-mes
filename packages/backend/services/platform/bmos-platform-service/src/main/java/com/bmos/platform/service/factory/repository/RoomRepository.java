package com.bmos.platform.service.factory.repository;

import com.bmos.platform.facade.factory.vo.FactoryRoomFeignVO;
import com.bmos.platform.service.factory.model.FactoryRoom;
import com.bmos.platform.service.factory.model.FactoryRoomStation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RoomRepository {

    /**
     * 当前模型id是否存在房间
     * @param moduleId
     * @return
     */
    boolean existRoom(Long moduleId);

    /**
     * 查询房间集合
     * @param idList
     * @return
     */
    List<FactoryRoom> selectByIdList(Collection<Long> idList);

    /**
     * 根据房间id集合查询工位与房间的绑定关系
     * @param roomIdList
     * @return
     */
    List<FactoryRoomStation> selectBindStationByRoomIdList(Collection<Long> roomIdList);

    /**
     * 根据房间id查询房间与工位绑定关系
     *
     * @param roomId
     * @return
     */
    List<FactoryRoomStation> selectBindStationByRoomId(Long roomId);

    /**
     * 工位id是否绑定在其他房间下
     * @param stationIdList
     * @return
     */
    boolean existsByStationIdList(List<Long> stationIdList);

    /**
     * 判断工位是否绑定房间
     * @param stationId
     * @return
     */
    boolean existBindRoom(Long stationId);

    /**
     * 业务配置 绑定/解除房间
     * @param roomUseMap
     */
    void useRoomCount(Map<Long, Boolean> roomUseMap);

    /**
     * 根据工位id查询房间
     *
     * @param stationId
     * @return
     */
    FactoryRoom selectRoomByStationId(Long stationId);

    /**
     * 查询工位与房间的绑定关系
     * @param stationIdList
     * @return
     */
    Map<Long, FactoryRoom> stationBindRoom(List<Long> stationIdList);

    /**
     * 获取具有数据权限的房间信息
     *
     * @param deptIdList
     * @return
     */
    List<FactoryRoom> selectByDeptIdList(List<Long> deptIdList);

    List<FactoryRoomFeignVO> queryRoomListByRoomIds(List<Long> roomIds);

    /**
     * 根据工位id删除房间绑定关系
     * @param stationId
     */
    void deleteRelationByStationId(Long stationId);
}
