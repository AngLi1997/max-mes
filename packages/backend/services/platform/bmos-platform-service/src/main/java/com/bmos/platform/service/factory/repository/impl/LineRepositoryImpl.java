package com.bmos.platform.service.factory.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.service.factory.mapper.FactoryLineMapper;
import com.bmos.platform.service.factory.mapper.FactoryLineRoomMapper;
import com.bmos.platform.service.factory.mapper.FactoryLineStationMapper;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.repository.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class LineRepositoryImpl implements LineRepository {

    @Autowired
    private FactoryLineRoomMapper factoryLineRoomMapper;

    @Autowired
    private FactoryLineStationMapper lineStationMapper;
    @Autowired
    private FactoryLineMapper factoryLineMapper;
    @Autowired
    private FactoryLineStationMapper factoryLineStationMapper;

    @Override
    public boolean existByRoomId(Long roomId) {
        return factoryLineRoomMapper.existByRoomId(roomId);
    }

    @Override
    public List<FactoryLineStation> selectByLineIdList(Collection<Long> productLineIds) {
        if (CollUtil.isEmpty(productLineIds)){
            return new ArrayList<>();
        }
        return lineStationMapper.selectByLineIdList(productLineIds);
    }

    @Override
    public List<FactoryLineRoom> selectByLineId(Long lineId) {
        return factoryLineRoomMapper.selectByLineId(lineId);
    }

    @Override
    public List<FactoryLineRoom> selectBindRoomByLineIdList(List<Long> lineIdList) {
        return factoryLineRoomMapper.selectByLineIdList(lineIdList);
    }

    @Override
    public List<FactoryLineStation> selectStationByLineId(Long lineId) {
        return lineStationMapper.selectByLineId(lineId);
    }

    @Override
    public boolean existStationBindLine(Long id) {
        return factoryLineStationMapper.existStationBindLine(id);
    }

    @Override
    public boolean existLine(Long moduleId) {
        return factoryLineMapper.existByModuleId(moduleId);
    }

    @Override
    public boolean existsByStationIdList(List<Long> stationIdList) {
        return factoryLineStationMapper.existsByStationIdList(stationIdList);
    }

    @Override
    public String selectLineNameByStationId(Long stationId) {
        FactoryLineStation factoryLineStation = factoryLineStationMapper.selectByStationId(stationId);
        if (Objects.isNull(factoryLineStation)){
            return null;
        }
        FactoryLine factoryLine = factoryLineMapper.selectById(factoryLineStation.getLineId());
        if (Objects.isNull(factoryLine)){
            return null;
        }
        return factoryLine.getName();
    }

    @Override
    public Map<Long, FactoryLine> stationBindLine(List<Long> stationIdList) {
        if (CollectionUtil.isEmpty(stationIdList)){
            return new HashMap<>();
        }
        List<FactoryLineStation> lineStationList = lineStationMapper.selectByStationIdList(stationIdList);
        if (CollectionUtil.isEmpty(lineStationList)){
            return new HashMap<>();
        }
        Set<Long> roomIdList = lineStationList.stream().map(FactoryLineStation::getLineId).collect(Collectors.toSet());
        List<FactoryLine> factoryLines = factoryLineMapper.selectBatchIds(roomIdList);
        if (CollectionUtil.isEmpty(factoryLines)){
            return new HashMap<>();
        }
        Map<Long, FactoryLine> lineMap = factoryLines.stream().collect(Collectors.toMap(FactoryLine::getId, Function.identity()));
        Map<Long, FactoryLine> factoryLineStationMap = new HashMap<>();
        for (FactoryLineStation factoryLineStation : lineStationList) {
            factoryLineStationMap.put(factoryLineStation.getStationId(), lineMap.get(factoryLineStation.getLineId()));
        }
        return factoryLineStationMap;
    }

    @Override
    public List<FactoryLine> selectLineByRoomId(Long id) {
        List<FactoryLineRoom> factoryLineRooms = factoryLineRoomMapper.selectByRoomId(id);
        if (CollUtil.isEmpty(factoryLineRooms)){
            return new ArrayList<>();
        }
        List<FactoryLine> factoryLines = factoryLineMapper.selectBatchIds(factoryLineRooms.stream().map(FactoryLineRoom::getLineId).collect(Collectors.toList()));
        if (CollUtil.isEmpty(factoryLines)){
            return new ArrayList<>();
        }
        return factoryLines;
    }

    @Override
    public List<FactoryLineRoom> selectRelationByRoomIdList(List<Long> roomIdList) {
        if (CollUtil.isEmpty(roomIdList)){
            return new ArrayList<>();
        }
        return factoryLineRoomMapper.selectByRoomIdList(roomIdList);
    }

    @Override
    public void deleteRelationByStationId(Long stationId) {
        lineStationMapper.deleteByStationId(stationId);
    }

    @Override
    public void deleteRelationByRoomId(Long roomId) {
        factoryLineRoomMapper.deleteByRoomId(roomId);
    }
}
