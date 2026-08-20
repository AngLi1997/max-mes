package com.bmos.platform.service.factory.repository;

import com.bmos.platform.service.factory.model.FactoryCleanRoomLog;
import com.bmos.platform.service.factory.model.FactoryRoomStatusLog;

public interface RoomLogRepository {

    /**
     * 查询此房间最新一条房间日志
     *
     * @param roomId
     * @return
     */
    FactoryCleanRoomLog selectLatestRoomLog(Long roomId);

    /**
     * 保存房间清洁日志
     *
     * @param factoryCleanRoomLog
     */
    void saveCleanLog(FactoryCleanRoomLog factoryCleanRoomLog);

    /**
     * 保存房间状态变更日志
     *
     * @param factoryStatusRoomLog
     */
    void saveStatusLog(FactoryRoomStatusLog factoryStatusRoomLog);


    /**
     * 根据房间id和状态查询最新一次房间状态变更日志
     * @param roomId
     * @param status
     * @return
     */
    FactoryRoomStatusLog selectStatusLogByRoomIdAndStatus(Long roomId, Integer status);
}
