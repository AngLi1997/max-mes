package com.bmos.platform.service.factory.convert;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.facade.factory.vo.RoomMobilePageFeignVO;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.service.dto.ModuleSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author renjinguang
 */
@Mapper
public interface FactoryModuleConverter {

    FactoryModuleConverter INSTANCE = Mappers.getMapper(FactoryModuleConverter.class);

    FactoryModule convertToModel(ModuleSaveDTO dto);

    List<StationModuleTreeNodeVO> convertToTreeListVo(List<FactoryModule> moduleList);

    ModuleVO convertToModuleVo(FactoryModule factoryModule);

    default RoomInfoVO convert2RoomVO(FactoryRoom factoryRoom, List<CodeNameVO> stationNameList){
        RoomInfoVO roomInfoVO = new RoomInfoVO();
        roomInfoVO.setId(factoryRoom.getId());
        roomInfoVO.setCode(factoryRoom.getCode());
        roomInfoVO.setName(factoryRoom.getName());
        roomInfoVO.setDescription(factoryRoom.getDescription());
        roomInfoVO.setTimeLimit(String.valueOf(factoryRoom.getTimeLimit() / (double) GlobalConstants.HOUR_OF_SECOND));
        roomInfoVO.setCleanLevel(factoryRoom.getCleanLevel());
        roomInfoVO.setThreeDModelId(factoryRoom.getThreeDModelId());
        roomInfoVO.setStationDetails(stationNameList);
        roomInfoVO.setStatus(CommonEnum.getKeyValueEnumByValue(RoomStatusEnum.class,factoryRoom.getStatus().toString()));
        return roomInfoVO;
    }

    default List<RoomPageVO> convert2RoomPageVO(List<FactoryRoom> factoryRoomList, Map<Long, List<Long>> roomStationIdMap, Map<Long, FactoryTenement> tenementMap, Map<Long, FactoryTenementFloor> floorMap){
        List<RoomPageVO> roomPageVOList = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryRoomList)){
            return roomPageVOList;
        }
        for (FactoryRoom factoryRoom : factoryRoomList) {
            RoomPageVO roomPageVO = new RoomPageVO();
            roomPageVO.setId(factoryRoom.getId());
            roomPageVO.setCode(factoryRoom.getCode());
            roomPageVO.setName(factoryRoom.getName());
            roomPageVO.setModuleId(factoryRoom.getModuleId());
            roomPageVO.setTimeLimit(String.valueOf(factoryRoom.getTimeLimit() / (double) GlobalConstants.HOUR_OF_SECOND));
            roomPageVO.setDescription(factoryRoom.getDescription());
            roomPageVO.setStationIdList(roomStationIdMap.get(factoryRoom.getId()));
            roomPageVO.setOperator(factoryRoom.getOperator());
            roomPageVO.setEnable(factoryRoom.getEnable());
            roomPageVO.setThreeDModelId(factoryRoom.getThreeDModelId());
            roomPageVO.setTenementId(factoryRoom.getTenementId());
            roomPageVO.setFloorId(factoryRoom.getFloorId());
            roomPageVO.setCleanLevel(factoryRoom.getCleanLevel());
            if (tenementMap.containsKey(factoryRoom.getTenementId())) {
                roomPageVO.setTenementName(tenementMap.get(factoryRoom.getTenementId()).getName());
            }
            if (floorMap.containsKey(factoryRoom.getFloorId())) {
                roomPageVO.setFloorName(floorMap.get(factoryRoom.getFloorId()).getName());
            }
            roomPageVO.setOperateTime(LocalDateTimeUtil.format(factoryRoom.getOperateTime(), GlobalConstants.DATE_TIME_FORMAT));
            roomPageVOList.add(roomPageVO);
        }
        return roomPageVOList;
    }

    default List<RoomTreeNodeVO> convert2RoomTreeNodeVO(List<FactoryModule> factoryRoomList, Map<Long, List<FactoryRoom>> roomMap){
        List<RoomTreeNodeVO> roomTreeNodeVOList = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryRoomList)){
            return roomTreeNodeVOList;
        }
        for (FactoryModule factoryModule : factoryRoomList) {
            RoomTreeNodeVO roomTreeNodeVO = new RoomTreeNodeVO();
            roomTreeNodeVO.setParentId(factoryModule.getParentId());
            roomTreeNodeVO.setId(factoryModule.getId());
            roomTreeNodeVO.setCode(factoryModule.getCode());
            roomTreeNodeVO.setName(factoryModule.getName());
            roomTreeNodeVO.setInfoList(convert2RoomList(roomMap.get(factoryModule.getId())));
            roomTreeNodeVOList.add(roomTreeNodeVO);
        }
        return roomTreeNodeVOList;
    }

    List<RoomEasyInfoVO> convert2RoomList(List<FactoryRoom> factoryRooms);

    default List<RoomMobilePageFeignVO> convert2RoomMobilePageVO(List<FactoryRoom> factoryRoomList){
        List<RoomMobilePageFeignVO> roomMobilePageFeignVOList = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryRoomList)){
            return roomMobilePageFeignVOList;
        }
        for (FactoryRoom factoryRoom : factoryRoomList) {
            RoomMobilePageFeignVO roomMobilePageFeignVO = new RoomMobilePageFeignVO();
            roomMobilePageFeignVO.setId(factoryRoom.getId());
            roomMobilePageFeignVO.setCode(factoryRoom.getCode());
            roomMobilePageFeignVO.setName(factoryRoom.getName());
            if (Objects.nonNull(factoryRoom.getExpireTime())){
                roomMobilePageFeignVO.setExpireTime(LocalDateTimeUtil.format(factoryRoom.getExpireTime(), GlobalConstants.DATE_TIME_FORMAT));
            }
            roomMobilePageFeignVO.setStatus(factoryRoom.getStatus());
            roomMobilePageFeignVOList.add(roomMobilePageFeignVO);
        }
        return roomMobilePageFeignVOList;
    }

    default List<LinePageVO> convert2LinePageVO(List<FactoryLine> factoryLineList, Map<Long, List<Long>> lineStationIdMap, Map<Long, List<Long>> lineRoomIdMap){
        List<LinePageVO> linePageVOList = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryLineList)){
            return linePageVOList;
        }
        for (FactoryLine factoryLine : factoryLineList) {
            LinePageVO linePageVO = new LinePageVO();
            linePageVO.setId(factoryLine.getId());
            linePageVO.setCode(factoryLine.getCode());
            linePageVO.setModuleId(factoryLine.getModuleId());
            linePageVO.setName(factoryLine.getName());
            linePageVO.setDescription(factoryLine.getDescription());
            linePageVO.setEnable(factoryLine.getEnable());
            linePageVO.setStationIdList(lineStationIdMap.get(factoryLine.getId()));
            linePageVO.setRoomIdList(lineRoomIdMap.get(factoryLine.getId()));
            linePageVO.setOperator(factoryLine.getOperator());
            linePageVO.setOperateTime(LocalDateTimeUtil.format(factoryLine.getOperateTime(), GlobalConstants.DATE_TIME_FORMAT));
            linePageVOList.add(linePageVO);
        }
        return linePageVOList;
    }

    default List<StationTreeNodeVO> convert2StationTree(List<FactoryModule> factoryStationList, Map<Long, List<EquipmentStation>> stationMap){
        List<StationTreeNodeVO> stationTreeNodeVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryStationList)){
            return stationTreeNodeVOS;
        }
        for (FactoryModule factoryModule : factoryStationList) {
            List<EquipmentStation> stationList = stationMap.get(factoryModule.getId());
            StationTreeNodeVO stationTreeNodeVO = new StationTreeNodeVO();
            stationTreeNodeVO.setParentId(factoryModule.getParentId());
            stationTreeNodeVO.setId(factoryModule.getId());
            stationTreeNodeVO.setCode(factoryModule.getCode());
            stationTreeNodeVO.setName(factoryModule.getName());
            stationTreeNodeVO.setInfoList(convert2StationEasyVO(stationList));
            stationTreeNodeVOS.add(stationTreeNodeVO);
       }
        return stationTreeNodeVOS;
    }

    List<StationEasyVO> convert2StationEasyVO(List<EquipmentStation> stationList);
}
