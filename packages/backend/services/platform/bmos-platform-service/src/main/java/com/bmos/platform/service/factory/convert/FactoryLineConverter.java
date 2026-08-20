package com.bmos.platform.service.factory.convert;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.tree.TreeUtil;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.facade.factory.dto.LineInfoFeignDTO;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.platform.facade.factory.vo.LineModuleTreeNodeFeignVO;
import com.bmos.platform.service.factory.controller.vo.CodeNameVO;
import com.bmos.platform.service.factory.controller.vo.LineInfoVO;
import com.bmos.platform.service.factory.controller.vo.FactoryTreeNodeVO;
import com.bmos.platform.service.factory.mapper.param.LineParam;
import com.bmos.platform.service.factory.model.*;
import com.bmos.platform.service.factory.service.dto.LineBindRoomDTO;
import com.bmos.platform.service.factory.service.dto.LineBindStationDTO;
import com.bmos.platform.service.factory.service.dto.LinePageDTO;
import com.bmos.platform.service.factory.service.dto.LineSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface FactoryLineConverter {

    FactoryLineConverter INSTANCE = Mappers.getMapper(FactoryLineConverter.class);

    FactoryLine convert2DO(LineSaveDTO dto);

    default List<FactoryLineRoom> convert2BindDO(LineBindRoomDTO dto){
        List<FactoryLineRoom> factoryLineRoomList = new ArrayList<>();
        if (CollectionUtil.isEmpty(dto.getRoomIdList())){
            return factoryLineRoomList;
        }
        for (Long roomId : dto.getRoomIdList()) {
            FactoryLineRoom factoryLineRoom = new FactoryLineRoom();
            factoryLineRoom.setLineId(dto.getId());
            factoryLineRoom.setRoomId(roomId);
            factoryLineRoomList.add(factoryLineRoom);
        }
        return factoryLineRoomList;
    }

    default List<FactoryLineStation> convert2BindDO(LineBindStationDTO dto){
        List<FactoryLineStation> factoryLineStationList = new ArrayList<>();
        if (CollectionUtil.isEmpty(dto.getStationIdList())){
            return factoryLineStationList;
        }
        for (Long stationId : dto.getStationIdList()) {
            FactoryLineStation factoryLineStation = new FactoryLineStation();
            factoryLineStation.setLineId(dto.getId());
            factoryLineStation.setStationId(stationId);
            factoryLineStationList.add(factoryLineStation);
        }
        return factoryLineStationList;
    }

    LineInfoVO convert2LineVO(FactoryLine factoryLine);

    default LineInfoVO convert2LineVO(FactoryLine factoryLine, List<CodeNameVO> stationNameList, List<CodeNameVO> roomNameList){
        LineInfoVO lineInfoVO = convert2LineVO(factoryLine);
        if (Objects.isNull(lineInfoVO)){
            return lineInfoVO;
        }
        lineInfoVO.setStationNameList(stationNameList);
        lineInfoVO.setRoomNameList(roomNameList);
        return lineInfoVO;
    }

    LineParam convert2Param(LinePageDTO dto);

    LineParam convert2Param(LineInfoFeignDTO dto);

    List<FactoryLineFeignVO> convert2FeignVO(List<FactoryLine> factoryLineList);

    List<FactoryLineDetailFeignVO> convert2LineDetailVO(List<FactoryLine> factoryLineList);

    default List<FactoryLineDetailFeignVO> convert2LineDetailVO(List<FactoryLine> factoryLineList,
                                                                Map<Long, List<FactoryRoom>> lineRoomMap,
                                                                Map<Long, List<EquipmentStation>> lineStationMap,
                                                                Map<Long, List<EquipmentStation>> roomStationMap){

        List<FactoryLineDetailFeignVO> factoryLineDetailFeignVOS = this.convert2LineDetailVO(factoryLineList);
        if (CollectionUtil.isEmpty(factoryLineDetailFeignVOS)){
            return factoryLineDetailFeignVOS;
        }
        for (FactoryLineDetailFeignVO factoryLineDetailFeignVO : factoryLineDetailFeignVOS) {
            List<FactoryRoom> factoryRoomList = lineRoomMap.get(factoryLineDetailFeignVO.getId());
            factoryLineDetailFeignVO.setRoomInfoFeignVOList(FactoryRoomConverter.INSTANCE.convert2RoomFeignVOList(factoryRoomList, roomStationMap));
            factoryLineDetailFeignVO.setStationFeignVOList(FactoryStationConverter.INSTANCE.convert2FeignVO(lineStationMap.get(factoryLineDetailFeignVO.getId())));
        }
        return factoryLineDetailFeignVOS;
    }

    default List<LineModuleTreeNodeFeignVO> convert2LineModuleTreeFeignVO(List<FactoryModule> factoryModules, Map<Long, List<FactoryLine>> lineMap){
        List<LineModuleTreeNodeFeignVO> lineModuleTreeNodeFeignVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryModules)){
            return lineModuleTreeNodeFeignVOS;
        }
        for (FactoryModule factoryModule : factoryModules) {
            LineModuleTreeNodeFeignVO lineModuleTreeNodeFeignVO = new LineModuleTreeNodeFeignVO();
            lineModuleTreeNodeFeignVO.setId(factoryModule.getId());
            lineModuleTreeNodeFeignVO.setCode(factoryModule.getCode());
            lineModuleTreeNodeFeignVO.setName(factoryModule.getName());
            lineModuleTreeNodeFeignVO.setParentId(factoryModule.getParentId());
            lineModuleTreeNodeFeignVO.setInfoList(FactoryLineConverter.INSTANCE.convert2FeignVO(lineMap.get(factoryModule.getId())));
            lineModuleTreeNodeFeignVOS.add(lineModuleTreeNodeFeignVO);
        }
        return TreeUtil.buildTree(lineModuleTreeNodeFeignVOS, false);
    }

    default List<FactoryTreeNodeVO> convert2UserLineInfoVOList(List<FactoryLine> factoryLines){
        List<FactoryTreeNodeVO> res = new ArrayList<>();
        if (CollectionUtil.isEmpty(factoryLines)){
            return res;
        }
        for (FactoryLine factoryLine : factoryLines) {
            FactoryTreeNodeVO factoryTreeNodeVO = new FactoryTreeNodeVO();
            factoryTreeNodeVO.setId(factoryLine.getId());
            factoryTreeNodeVO.setCode(factoryLine.getCode());
            factoryTreeNodeVO.setName(factoryLine.getName());
            factoryTreeNodeVO.setType(FactoryModuleEnum.LINE.getType());
            res.add(factoryTreeNodeVO);
        }
        return res;
    }
}
