package com.bmos.platform.service.factory.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.factory.vo.FactoryRoomFeignVO;
import com.bmos.platform.service.factory.mapper.FactoryRoomMapper;
import com.bmos.platform.service.factory.mapper.FactoryRoomStationMapper;
import com.bmos.platform.service.factory.mapper.param.RoomParam;
import com.bmos.platform.service.factory.model.FactoryRoom;
import com.bmos.platform.service.factory.model.FactoryRoomStation;
import com.bmos.platform.service.factory.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoomRepositoryImpl implements RoomRepository {

    @Autowired
    private FactoryRoomMapper roomMapper;

    @Autowired
    private FactoryRoomStationMapper roomStationMapper;
    @Autowired
    private FactoryRoomMapper factoryRoomMapper;
    @Autowired
    private FactoryRoomStationMapper factoryRoomStationMapper;

    @Override
    public boolean existRoom(Long moduleId) {
        return roomMapper.existsByModuleId(moduleId);
    }

    @Override
    public List<FactoryRoom> selectByIdList(Collection<Long> idList) {
        return roomMapper.selectBatchIds(idList);
    }

    @Override
    public List<FactoryRoomStation> selectBindStationByRoomIdList(Collection<Long> roomIdList) {
        return roomStationMapper.selectByRoomIdList(roomIdList);
    }

    @Override
    public List<FactoryRoomStation> selectBindStationByRoomId(Long roomId) {
        return roomStationMapper.selectByRoomId(roomId);
    }

    @Override
    public boolean existsByStationIdList(List<Long> stationIdList) {
        return roomStationMapper.existByStationIdList(stationIdList);
    }

    @Override
    public boolean existBindRoom(Long stationId) {
        return roomStationMapper.existsByStationId(stationId);
    }

    @Override
    public void useRoomCount(Map<Long, Boolean> roomUseMap) {
        Set<Long> roomIds = roomUseMap.keySet();
        List<FactoryRoom> factoryRoomList = factoryRoomMapper.selectBatchIds(roomIds);
        if (CollectionUtil.isEmpty(factoryRoomList)){
            throw new BmosException(PlatformResponseCode.FACTORY_ROOM_NOT_EXIST);
        }
        for (FactoryRoom factoryRoom : factoryRoomList) {
            Boolean curUse = roomUseMap.get(factoryRoom.getId());
            if (factoryRoom.getUseCount() > 0 && !curUse){
                factoryRoom.setUseCount(factoryRoom.getUseCount() - 1);
            } else {
                factoryRoom.setUseCount(factoryRoom.getUseCount() + 1);
            }
        }
        factoryRoomMapper.updateBatch(factoryRoomList);
    }

    @Override
    public FactoryRoom selectRoomByStationId(Long stationId) {
        FactoryRoomStation factoryRoomStation = factoryRoomStationMapper.selectByStationId(stationId);
        if (Objects.isNull(factoryRoomStation)){
            return null;
        }
        FactoryRoom factoryRoom = factoryRoomMapper.selectById(factoryRoomStation.getRoomId());
        if (Objects.isNull(factoryRoom)){
            return null;
        }
        return factoryRoom;
    }

    /**
     * 获取当前工位绑定的房间
     * @param stationIdList
     */
    @Override
    public Map<Long, FactoryRoom> stationBindRoom(List<Long> stationIdList) {
        if (CollectionUtil.isEmpty(stationIdList)){
            return new HashMap<>();
        }
        List<FactoryRoomStation> roomStationList = roomStationMapper.selectByStationIdList(stationIdList);
        if (CollectionUtil.isEmpty(roomStationList)){
            return new HashMap<>();
        }
        Set<Long> roomIdList = roomStationList.stream().map(FactoryRoomStation::getRoomId).collect(Collectors.toSet());
        List<FactoryRoom> factoryRooms = selectByIdList(roomIdList);
        if (CollectionUtil.isEmpty(factoryRooms)){
            return new HashMap<>();
        }
        Map<Long, FactoryRoom> roomMap = factoryRooms.stream().collect(Collectors.toMap(FactoryRoom::getId, Function.identity()));
        Map<Long, FactoryRoom> roomStationMap = new HashMap<>();
        for (FactoryRoomStation factoryRoomStation : roomStationList) {
            roomStationMap.put(factoryRoomStation.getStationId(), roomMap.get(factoryRoomStation.getRoomId()));
        }
        return roomStationMap;
    }

    @Override
    public List<FactoryRoom> selectByDeptIdList(List<Long> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)){
            return new ArrayList<>();
        }
        return roomMapper.selectByParam(RoomParam.builder().deptIdList(deptIdList).build());
    }

    @Override
    public List<FactoryRoomFeignVO> queryRoomListByRoomIds(List<Long> roomIds) {
        return roomMapper.queryRoomListByRoomIds(roomIds);
    }

    @Override
    public void deleteRelationByStationId(Long stationId) {
        roomStationMapper.deleteByStationId(stationId);
    }
}
