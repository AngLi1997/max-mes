package com.bmos.platform.service.factory.repository;

import com.bmos.platform.service.factory.model.FactoryLine;
import com.bmos.platform.service.factory.model.FactoryLineRoom;
import com.bmos.platform.service.factory.model.FactoryLineStation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LineRepository {

    /**
     * 判断房间是否绑定产线
     * @param roomId
     * @return
     */
    boolean existByRoomId(Long roomId);

    /**
     * 根据产线idList查询产线信息
     * @param productLineIds
     * @return
     */
    List<FactoryLineStation> selectByLineIdList(Collection<Long> productLineIds);

    /**
     * 根据产线id查询查询产线与房间绑定关系
     * @param lineId
     * @return
     */
    List<FactoryLineRoom> selectByLineId(Long lineId);

    /**
     * 根据产线id查询查询产线与房间绑定关系
     * @param lineIdList
     * @return
     */
    List<FactoryLineRoom> selectBindRoomByLineIdList(List<Long> lineIdList);

    /**
     * 查询产线与工位的直接绑定关系
     * @param lineId
     * @return
     */
    List<FactoryLineStation> selectStationByLineId(Long lineId);

    /**
     * 判断工位是否绑定产线
     * @param id
     * @return
     */
    boolean existStationBindLine(Long id);

    /**
     * 当前模型id下是否存在产线
     * @param moduleId
     * @return
     */
    boolean existLine(Long moduleId);

    /**
     * 校验工位是否绑定在其他产线下
     * @param stationIdList
     * @return
     */
    boolean existsByStationIdList(List<Long> stationIdList);

    /**
     * 根据工位id查询产线名称
     * @param stationId
     * @return
     */
    String selectLineNameByStationId(Long stationId);

    /**
     * 查询工位与产线绑定关系
     * @param stationIdList
     * @return
     */
    Map<Long, FactoryLine> stationBindLine(List<Long> stationIdList);

    /**
     * 根据房间id查询产线名称
     * @param id
     * @return
     */
    List<FactoryLine> selectLineByRoomId(Long id);

    /**
     * 获取这些房间与工位的绑定关系
     * @param roomIdList
     * @return
     */
    List<FactoryLineRoom> selectRelationByRoomIdList(List<Long> roomIdList);

    /**
     * 根据工位id删除工位与产线绑定关系
     * @param stationId
     */
    void deleteRelationByStationId(Long stationId);

    /**
     * 根据房间id删除房间与产线的绑定关系
     * @param roomId
     */
    void deleteRelationByRoomId(Long roomId);
}
