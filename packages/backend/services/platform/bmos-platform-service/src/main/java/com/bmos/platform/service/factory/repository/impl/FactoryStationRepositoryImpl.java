package com.bmos.platform.service.factory.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.factory.mapper.EquipmentStationMapper;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.FactoryRoom;
import com.bmos.platform.service.factory.repository.FactoryStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class FactoryStationRepositoryImpl implements FactoryStationRepository {

    @Autowired
    private EquipmentStationMapper equipmentStationMapper;

    @Override
    public boolean existStation(Long id) {
        return equipmentStationMapper.existStation(id);
    }

    @Override
    public List<EquipmentStation> getStationByRoomIds(List<Long> roomIdList) {
        return equipmentStationMapper.queryStationListByRoomIds(roomIdList, Boolean.TRUE);
    }

    @Override
    public List<EquipmentStation> selectByStationIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)){
            return new ArrayList<>();
        }
        return equipmentStationMapper.selectBatchIds(idList);
    }

    @Override
    public void useStationCount(Map<Long, Boolean> stationUseMap) {
        Set<Long> stationIds = stationUseMap.keySet();
        List<EquipmentStation> equipmentStationList = equipmentStationMapper.selectBatchIds(stationIds);
        if (CollectionUtil.isEmpty(equipmentStationList)){
            throw new BmosException(PlatformResponseCode.FACTORY_STATION_NOT_EXIST);
        }
        for (EquipmentStation equipmentStation : equipmentStationList) {
            Boolean curUse = stationUseMap.get(equipmentStation.getId());
            if (equipmentStation.getUseCount() > 0 && !curUse){
                equipmentStation.setUseCount(equipmentStation.getUseCount() - 1);
            } else {
                equipmentStation.setUseCount(equipmentStation.getUseCount() + 1);
            }
        }
        equipmentStationMapper.updateBatch(equipmentStationList);
    }
}
