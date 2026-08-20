package com.bmos.platform.service.factory.convert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.facade.factory.dto.ChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.MobileChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.RoomMobilePageFeignDTO;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum;
import com.bmos.platform.facade.factory.vo.*;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.facade.factory.vo.RoomInfoMobileFeignVO;
import com.bmos.platform.service.factory.mapper.param.RoomParam;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.service.data.RoomStatusOperateData;
import com.bmos.platform.service.factory.service.dto.*;
import com.bmos.platform.service.system.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Mapper
public interface FactoryRoomConverter {

    FactoryRoomConverter INSTANCE = Mappers.getMapper(FactoryRoomConverter.class);

    default FactoryRoom convert2DO(RoomSaveDTO dto) {
        FactoryRoom factoryRoom = new FactoryRoom();
        factoryRoom.setCode(dto.getCode());
        factoryRoom.setName(dto.getName());
        factoryRoom.setTimeLimit((long) (Double.parseDouble(dto.getTimeLimit()) * GlobalConstants.HOUR_OF_SECOND));
        factoryRoom.setModuleId(dto.getModuleId());
        factoryRoom.setFloorId(dto.getFloorId());
        factoryRoom.setTenementId(dto.getTenementId());
        factoryRoom.setCleanLevel(dto.getCleanLevel());
        factoryRoom.setDescription(dto.getDescription());
        return factoryRoom;
    }

    default List<FactoryRoomStation> convert2BindDO(RoomBindStationDTO dto) {
        List<FactoryRoomStation> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(dto.getStationIdList())) {
            return list;
        }
        for (Long stationId : dto.getStationIdList()) {
            FactoryRoomStation factoryRoomStation = new FactoryRoomStation();
            factoryRoomStation.setRoomId(dto.getId());
            factoryRoomStation.setStationId(stationId);
            list.add(factoryRoomStation);
        }
        return list;
    }

    RoomParam convert2Param(RoomPageDTO dto);

    RoomParam convert2Param(RoomMobilePageFeignDTO dto);


    default RoomInfoMobileFeignVO convert2MobileVO(FactoryRoom factoryRoom, FactoryRoomStatusLog factoryRoomStatusLog) {
        if (ObjectUtil.isNull(factoryRoom)) {
            return null;
        }
        RoomInfoMobileFeignVO roomInfoMobileFeignVO = new RoomInfoMobileFeignVO();
        roomInfoMobileFeignVO.setId( factoryRoom.getId() );
        roomInfoMobileFeignVO.setCode( factoryRoom.getCode() );
        roomInfoMobileFeignVO.setName( factoryRoom.getName() );
        roomInfoMobileFeignVO.setStatus(factoryRoom.getStatus());
        roomInfoMobileFeignVO.setExpireTime( factoryRoom.getExpireTime() );
        if (Objects.nonNull(factoryRoomStatusLog)){
            roomInfoMobileFeignVO.setBatchNo(factoryRoomStatusLog.getBatchNo());
            roomInfoMobileFeignVO.setProcedureName(factoryRoomStatusLog.getProcedureName());
            roomInfoMobileFeignVO.setProductName(factoryRoomStatusLog.getProductName());
        }
        return roomInfoMobileFeignVO;
    }

    RoomCleanInfoFeignVO convert2RoomCleanFeignVO(FactoryRoom factoryRoom);

    default RoomStatusOperateData convert2Data(MobileChangeRoomStatusFeignDTO dto, RoomStatusOperateTypeEnum operateTypeEnum) {
        RoomStatusOperateData data = new RoomStatusOperateData();
        data.setId(dto.getId());
        data.setStatus(dto.getStatus());
        data.setExpireTime(Objects.nonNull(dto.getExpireTime()) ? LocalDateTime.parse(dto.getExpireTime(), DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT)) : null);
        data.setBatchNo(dto.getBatchNo());
        data.setProductName(dto.getProductName());
        data.setProcedureId(dto.getProcedureId());
        data.setProductId(dto.getProductId());
        data.setProcedureName(dto.getProcedureName());
        data.setDesc(dto.getDesc());
        data.setOperateId(dto.getOperateId());
        data.setVerifierId(dto.getVerifyId());
        data.setBeginTime(Objects.nonNull(dto.getBeginTime()) ? LocalDateTime.parse(dto.getBeginTime(), DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT)) : null);
        data.setEndTime(Objects.nonNull(dto.getEndTime()) ? LocalDateTime.parse(dto.getEndTime(), DateTimeFormatter.ofPattern(GlobalConstants.DATE_TIME_FORMAT)) : null);
        data.setOperateTypeEnum(operateTypeEnum);
        return data;
    }

    default RoomStatusOperateData convert2Data(ChangeRoomStatusFeignDTO dto, RoomStatusOperateTypeEnum operateTypeEnum) {
        RoomStatusOperateData data = new RoomStatusOperateData();
        data.setId(dto.getId());
        data.setStatus(dto.getStatus());
        data.setExpireTime(dto.getExpireTime());
        data.setBatchNo(dto.getBatchNo());
        data.setProductName(dto.getProductName());
        data.setProcedureId(dto.getProcedureId());
        data.setProcedureName(dto.getProcedureName());
        data.setOperateId(dto.getOperateId());
        data.setVerifierId(dto.getVerifyId());
        data.setBeginTime(dto.getBeginTime());
        data.setEndTime(dto.getEndTime());
        data.setOperateTypeEnum(operateTypeEnum);
        data.setVerifyTime(dto.getVerifyTime());
        return data;
    }

    default FactoryCleanRoomLog convert2Log(FactoryRoom room, RoomStatusOperateData operateData, Map<String, User> userMap) {
        User operateUser = userMap.get(operateData.getOperateId());
        User verifyUser = userMap.get(operateData.getVerifierId());
        FactoryCleanRoomLog log = new FactoryCleanRoomLog();
        log.setRoomId(room.getId());
        log.setRoomCode(room.getCode());
        log.setRoomName(room.getName());
        log.setType(operateData.getOperateTypeEnum().getCode());
        log.setBatchNo(operateData.getBatchNo());
        log.setProductName(operateData.getProductName());
        log.setProcedureId(operateData.getProcedureId());
        log.setProcedureName(operateData.getProcedureName());
        log.setBeginTime(operateData.getBeginTime());
        log.setEndTime(operateData.getEndTime());
        log.setExpireTime(operateData.getExpireTime());
        log.setOperatorId(operateData.getOperateId());
        log.setOperator(operateUser.getLoginName() + "-" + operateUser.getUserName());
        log.setVerifyId(operateData.getVerifierId());
        log.setVerifier(verifyUser.getLoginName() + "-" + verifyUser.getUserName());
        log.setVerifyTime(operateData.getVerifyTime());
        log.setDescription(operateData.getDesc());
        return log;
    }

    List<RoomInfoFeignVO> convert2RoomFeignVOList(List<FactoryRoom> roomList);

    default List<RoomInfoFeignVO> convert2RoomFeignVOList(List<FactoryRoom> roomList, Map<Long, List<EquipmentStation>> roomStationMap) {
        List<RoomInfoFeignVO> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(roomList)) {
            return list;
        }
        for (FactoryRoom room : roomList) {
            RoomInfoFeignVO vo = convert2RoomFeignVO(room);
            List<EquipmentStation> equipmentStations = roomStationMap.get(room.getId());
            vo.setStationFeignVOList(FactoryStationConverter.INSTANCE.convert2FeignVO(equipmentStations));
            list.add(vo);
        }
        return list;
    }

    default List<RoomInfoFeignVO> convert2RoomFeignVOList(List<FactoryRoom> roomList, Map<Long, List<EquipmentStation>> roomStationMap, Map<Long, List<Long>> permissionMap) {
        List<RoomInfoFeignVO> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(roomList)) {
            return list;
        }
        for (FactoryRoom room : roomList) {
            RoomInfoFeignVO vo = convert2RoomFeignVO(room);
            List<EquipmentStation> equipmentStations = roomStationMap.get(room.getId());
            vo.setStationFeignVOList(FactoryStationConverter.INSTANCE.convert2FeignVO(equipmentStations));
            vo.setPermisionIdList(permissionMap.get(room.getId()));
            list.add(vo);
        }
        return list;
    }

    default RoomInfoFeignVO convert2RoomFeignVO(FactoryRoom room){
        RoomInfoFeignVO vo = new RoomInfoFeignVO();
        vo.setId(room.getId());
        vo.setCode(room.getCode());
        vo.setName(room.getName());
        vo.setTimeLimit(StrUtil.toString(room.getTimeLimit()));
        vo.setStatus(room.getStatus());
        vo.setExpireTime(room.getExpireTime());
        return vo;
    }

    default RoomPrintVO convert2PrintVO(FactoryRoom factoryRoom) {
        RoomPrintVO vo = new RoomPrintVO();
        if (Objects.isNull(factoryRoom)) {
            return vo;
        }
        vo.setRoomCode(factoryRoom.getCode());
        vo.setRoomName(factoryRoom.getName());
        vo.setRoomId(factoryRoom.getId());
        vo.setTimeLimit(factoryRoom.getTimeLimit() / (double) GlobalConstants.HOUR_OF_SECOND + "小时");
        return vo;
    }

    default List<RoomModuleTreeNodeFeignVO> convert2RoomModeuleTreeNodeFeignVO(List<RoomTreeNodeVO> roomTreeList) {
        List<RoomModuleTreeNodeFeignVO> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(roomTreeList)) {
            return list;
        }
        for (RoomTreeNodeVO roomTreeNodeVO : roomTreeList) {
            RoomModuleTreeNodeFeignVO vo = new RoomModuleTreeNodeFeignVO();
            vo.setId(roomTreeNodeVO.getId());
            vo.setName(roomTreeNodeVO.getName());
            vo.setChildren(convert2RoomModeuleTreeNodeFeignVO(roomTreeNodeVO.getChildren()));
            vo.setInfoList(convert2RoomEasyFeinVO(roomTreeNodeVO.getInfoList()));
            list.add(vo);
        }
        return list;
    }

    List<RoomEasyInfoFeignVO> convert2RoomEasyFeinVO(List<RoomEasyInfoVO> infoList);

    default FactoryRoomStatusLog convert2StatusLog(FactoryRoom room, RoomStatusOperateData operateData, Map<String, User> userMap,
                                                   Integer preStatus, Integer curStatus) {
        User operateUser = userMap.get(operateData.getOperateId());


        User verifyUser = userMap.get(operateData.getVerifierId());
        FactoryRoomStatusLog log = new FactoryRoomStatusLog();
        log.setRoomId(room.getId());
        log.setRoomCode(room.getCode());
        log.setRoomName(room.getName());
        log.setType(operateData.getOperateTypeEnum().getCode());
        log.setStatus(curStatus);
        log.setPreStatus(preStatus);
        log.setBatchNo(operateData.getBatchNo());
        log.setProductName(operateData.getProductName());
        log.setProductId(operateData.getProductId());
        log.setProcedureId(operateData.getProcedureId());
        log.setProcedureName(operateData.getProcedureName());
        log.setOperatorId(operateData.getOperateId());
        log.setOperator(operateUser.getLoginName() + "-" + operateUser.getUserName());
        log.setVerifyId(operateData.getVerifierId());
        log.setVerifier(verifyUser.getLoginName() + "-" + verifyUser.getUserName());
        log.setDescription(operateData.getDesc());
        return log;
    }

    default List<FactoryTreeNodeVO> convert2UserRoomInfoVOList(List<FactoryRoom> roomList) {
        List<FactoryTreeNodeVO> res = new ArrayList<>();
        if (CollectionUtil.isEmpty(roomList)) {
            return res;
        }
        for (FactoryRoom factoryRoom : roomList) {
            FactoryTreeNodeVO factoryTreeNodeVO = new FactoryTreeNodeVO();
            factoryTreeNodeVO.setId(factoryRoom.getId());
            factoryTreeNodeVO.setCode(factoryRoom.getCode());
            factoryTreeNodeVO.setName(factoryRoom.getName());
            factoryTreeNodeVO.setType(FactoryModuleEnum.ROOM.getType());
            res.add(factoryTreeNodeVO);
        }
        return res;
    }

    CommonPage<RoomAppPageVO> convert2RoomAppPageVO(CommonPage<RoomInfoFeignVO> roomInfoFeignVOCommonPage);

}
