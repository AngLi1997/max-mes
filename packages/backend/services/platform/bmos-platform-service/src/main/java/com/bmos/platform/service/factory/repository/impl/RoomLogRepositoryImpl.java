package com.bmos.platform.service.factory.repository.impl;

import com.bmos.platform.service.factory.dao.FactoryRoomStatusLogMapper;
import com.bmos.platform.service.factory.mapper.FactoryRoomLogMapper;
import com.bmos.platform.service.factory.model.FactoryCleanRoomLog;
import com.bmos.platform.service.factory.model.FactoryRoomStatusLog;
import com.bmos.platform.service.factory.repository.RoomLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoomLogRepositoryImpl implements RoomLogRepository {

    @Autowired
    private FactoryRoomLogMapper factoryRoomLogMapper;

    @Autowired
    private FactoryRoomStatusLogMapper factoryRoomStatusLogMapper;

    @Override
    public FactoryCleanRoomLog selectLatestRoomLog(Long roomId) {
        return factoryRoomLogMapper.selectLatestByRoomId(roomId);
    }

    @Override
    public void saveCleanLog(FactoryCleanRoomLog factoryCleanRoomLog) {
        factoryRoomLogMapper.insert(factoryCleanRoomLog);
    }

    @Override
    public void saveStatusLog(FactoryRoomStatusLog factoryStatusRoomLog) {
        factoryRoomStatusLogMapper.insert(factoryStatusRoomLog);
    }

    @Override
    public FactoryRoomStatusLog selectStatusLogByRoomIdAndStatus(Long roomId, Integer status) {
        return factoryRoomStatusLogMapper.selectByRoomIdAndStatus(roomId, status);
    }
}
