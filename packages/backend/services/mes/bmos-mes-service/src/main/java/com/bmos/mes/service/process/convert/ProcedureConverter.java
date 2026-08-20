package com.bmos.mes.service.process.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.tree.TreeUtil;
import com.bmos.mes.service.process.dto.ProcedureCopyDTO;
import com.bmos.mes.service.process.dto.ProcedureDTO;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.vo.*;
import com.bmos.platform.facade.factory.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProcedureConverter {
    ProcedureConverter INSTANCE = Mappers.getMapper(ProcedureConverter.class);

    default List<Procedure> convertList(ProcessVersion processVersion, List<ProcedureDTO> procedures) {
        return procedures.stream()
                .map(e -> {
                    Procedure procedure = new Procedure();
                    procedure.setName(e.getHistoricalName());
                    procedure.setProcessId(processVersion.getProcessId());
                    return procedure;
                })
                .collect(Collectors.toList());
    }

    List<HistoricVO> convertHistoric(List<Procedure> list);

    default List<Procedure> convertList2(ProcessVersion copyVersion, List<ProcedureCopyDTO> procedures) {
        return procedures.stream()
                .map(e -> {
                    Procedure procedure = new Procedure();
                    procedure.setName(e.getName());
                    procedure.setProcessId(copyVersion.getProcessId());
                    return procedure;
                })
                .collect(Collectors.toList());
    }

    default List<ProcedureModelGroup> convertGroupList(List<ProcedureModel> procedureModels) {
        List<ProcedureModelGroup> groups = new ArrayList<>();
        for (ProcedureModel procedureModel : procedureModels) {
            for (Long groupId : procedureModel.getGroupIds()) {
                ProcedureModelGroup group = new ProcedureModelGroup();
                group.setGroupId(groupId);
                group.setProcedureModelId(procedureModel.getId());
                groups.add(group);
            }
        }
        return groups;
    }

    default List<ProcedureModelGroup> convertGroupList(ProcedureModel procedureModel) {
        List<ProcedureModelGroup> groups = new ArrayList<>();
        for (Long groupId : procedureModel.getGroupIds()) {
            ProcedureModelGroup group = new ProcedureModelGroup();
            group.setGroupId(groupId);
            group.setProcedureModelId(procedureModel.getId());
            groups.add(group);
        }
        return groups;
    }

    default List<ProcedureModelMaterial> convertMaterialList(List<ProcedureModel> procedureModels) {
        List<ProcedureModelMaterial> materials = new ArrayList<>();
        for (ProcedureModel procedureModel : procedureModels) {
            List<Long> formulaMaterialIdList = procedureModel.getFormulaMaterialIdList();
            for (Long materialId : formulaMaterialIdList) {
                ProcedureModelMaterial material = new ProcedureModelMaterial();
                material.setProcedureModelId(procedureModel.getId());
                material.setProductFormulaMaterialId(materialId);
                materials.add(material);
            }

        }
        return materials;
    }

    default List<ProcedureModelMaterial> convertMaterialList(ProcedureModel procedureModel) {
        List<ProcedureModelMaterial> materials = new ArrayList<>();
        List<Long> formulaMaterialIdList = procedureModel.getFormulaMaterialIdList();
        for (Long materialId : formulaMaterialIdList) {
            ProcedureModelMaterial material = new ProcedureModelMaterial();
            material.setProcedureModelId(procedureModel.getId());
            material.setProductFormulaMaterialId(materialId);
            materials.add(material);
        }
        return materials;
    }

    default List<ProcedureModelRoom> convertRoomList(List<ProcedureModel> procedureModels){
        List<ProcedureModelRoom> materials = new ArrayList<>();
        for (ProcedureModel procedureModel : procedureModels) {
            List<String> roomIdPathList = procedureModel.getRoomIdList();
            for (String roomPath : roomIdPathList) {
                ProcedureModelRoom procedureModelRoom = new ProcedureModelRoom();
                procedureModelRoom.setProcedureModelId(procedureModel.getId());
                List<String> pathList = StrUtil.split(roomPath, StrUtil.DASHED);
                procedureModelRoom.setRoomId(Long.valueOf(CollUtil.getLast(pathList)));
                procedureModelRoom.setRoomIdPath(roomPath);
                materials.add(procedureModelRoom);
            }

        }
        return materials;
    }

    default List<ProcedureModelRoom> convertRoomList(ProcedureModel procedureModel){
        List<ProcedureModelRoom> rooms = new ArrayList<>();
        List<String> roomIdPathList = procedureModel.getRoomIdList();
        for (String roomIdPath : roomIdPathList) {
            ProcedureModelRoom room = new ProcedureModelRoom();
            room.setProcedureModelId(procedureModel.getId());
            room.setRoomIdPath(roomIdPath);
            List<String> pathList = StrUtil.split(roomIdPath, StrUtil.DASHED);
            room.setRoomId(Long.valueOf(CollUtil.getLast(pathList)));
            rooms.add(room);
        }
        return rooms;
    }

    default List<ProcedureModelRoomOrStationVO> convertToProcedureModelRoomVO(List<RoomInfoFeignVO> data){
        List<ProcedureModelRoomOrStationVO> res = new ArrayList<>();
        if (CollectionUtil.isEmpty(data)){
            return res;
        }
        for (RoomInfoFeignVO roomInfoFeignVO : data) {
            ProcedureModelRoomOrStationVO procedureModelRoomOrStationVO = new ProcedureModelRoomOrStationVO();
            if (roomInfoFeignVO.judgeVirtualRoom()){
                res.addAll(convertToProcedureModelStationVO(roomInfoFeignVO.getStationFeignVOList()));
                continue;
            }
            procedureModelRoomOrStationVO.setId(roomInfoFeignVO.getId());
            procedureModelRoomOrStationVO.setName(roomInfoFeignVO.getName());
            procedureModelRoomOrStationVO.setCode(roomInfoFeignVO.getCode());
            procedureModelRoomOrStationVO.setRoomFlag(Boolean.TRUE);
            procedureModelRoomOrStationVO.setShowName(roomInfoFeignVO.getCode() + StrUtil.DASHED + roomInfoFeignVO.getName());
            procedureModelRoomOrStationVO.setChildren(convertToProcedureModelStationVO(roomInfoFeignVO.getStationFeignVOList()));
            res.add(procedureModelRoomOrStationVO);
        }
        return res;
    }

    default List<ProcedureModelRoomOrStationVO> convertToProcedureModelStationVO(List<FactoryStationFeignVO> data){
        List<ProcedureModelRoomOrStationVO> res = new ArrayList<>();
        if (CollectionUtil.isEmpty(data)){
            return res;
        }
        for (FactoryStationFeignVO factoryStationFeignVO : data) {
            ProcedureModelRoomOrStationVO procedureModelRoomOrStationVO = new ProcedureModelRoomOrStationVO();
            procedureModelRoomOrStationVO.setId(factoryStationFeignVO.getId());
            procedureModelRoomOrStationVO.setName(factoryStationFeignVO.getName());
            procedureModelRoomOrStationVO.setCode(factoryStationFeignVO.getCode());
            procedureModelRoomOrStationVO.setShowName(factoryStationFeignVO.getCode() + StrUtil.DASHED + factoryStationFeignVO.getName());
            procedureModelRoomOrStationVO.setRoomFlag(Boolean.FALSE);
            res.add(procedureModelRoomOrStationVO);
        }
        return res;
    }


    List<ProductLineVO> convert2ProductLineVO(List<FactoryLineFeignVO> data);

    List<ProductLineRoomVO> convert2ProductLineRoomVO(List<RoomInfoFeignVO> data);

    default List<ProcedureModelRoomOrStationVO> convertToProcedureModelRoomVO2(List<FactoryLineDetailFeignVO> list){
        List<ProcedureModelRoomOrStationVO> result = new ArrayList<>();
        if(CollUtil.isEmpty(list)){
            return result;
        }
        for (FactoryLineDetailFeignVO e : list) {
            ProcedureModelRoomOrStationVO vo = new ProcedureModelRoomOrStationVO();
            vo.setId(e.getId());
            vo.setCode(e.getCode());
            vo.setName(e.getName());
            vo.setShowName(e.getCode() + StrUtil.DASHED + e.getName());
            vo.setRoomIdPath(String.valueOf(e.getId()));
            List<FactoryStationFeignVO> stationFeignVOList = e.getStationFeignVOList();
            List<RoomInfoFeignVO> roomInfoFeignVOList = e.getRoomInfoFeignVOList();
            ArrayList<ProcedureModelRoomOrStationVO> children = new ArrayList<>();
            handleProductionLineStations(stationFeignVOList, vo, children);
            handleProductionLineRooms(roomInfoFeignVOList, vo, children);
            vo.setChildren(children);
            result.add(vo);
        }
        return result;
    }

    static void handleProductionLineRooms(List<RoomInfoFeignVO> roomInfoFeignVOList, ProcedureModelRoomOrStationVO vo, ArrayList<ProcedureModelRoomOrStationVO> children) {
        if(CollUtil.isNotEmpty(roomInfoFeignVOList)){
            for (RoomInfoFeignVO roomInfoFeignVO : roomInfoFeignVOList) {
                ProcedureModelRoomOrStationVO child = new ProcedureModelRoomOrStationVO();
                child.setId(roomInfoFeignVO.getId());
                child.setCode(roomInfoFeignVO.getCode());
                child.setName(roomInfoFeignVO.getName());
                child.setShowName(roomInfoFeignVO.getCode() + StrUtil.DASHED + roomInfoFeignVO.getName());
                child.setRoomIdPath(vo.getRoomIdPath() + StrUtil.DASHED + roomInfoFeignVO.getId());
                List<FactoryStationFeignVO> stationList = roomInfoFeignVO.getStationFeignVOList();
                if(CollUtil.isNotEmpty(stationList)){
                    child.setChildren(stationList.stream().map(c->{
                        ProcedureModelRoomOrStationVO station = new ProcedureModelRoomOrStationVO();
                        station.setId(c.getId());
                        station.setCode(c.getCode());
                        station.setName(c.getName());
                        station.setStationFlag(true);
                        station.setShowName(c.getCode() + StrUtil.DASHED + c.getName());
                        station.setRoomIdPath(child.getRoomIdPath() + StrUtil.DASHED + c.getId());
                        return station;
                    }).collect(Collectors.toList()));
                }
                children.add(child);
            }
        }
    }

    static void handleProductionLineStations(List<FactoryStationFeignVO> stationFeignVOList, ProcedureModelRoomOrStationVO vo, ArrayList<ProcedureModelRoomOrStationVO> children) {
        if(CollUtil.isNotEmpty(stationFeignVOList)){
            List<ProcedureModelRoomOrStationVO> collect = stationFeignVOList.stream().map(s -> {
                ProcedureModelRoomOrStationVO child = new ProcedureModelRoomOrStationVO();
                child.setStationFlag(true);
                child.setId(s.getId());
                child.setCode(s.getCode());
                child.setName(s.getName());
                child.setShowName(s.getCode() + StrUtil.DASHED + s.getName());
                child.setRoomIdPath(vo.getRoomIdPath() + StrUtil.DASHED + s.getId());
                return child;
            }).collect(Collectors.toList());
            children.addAll(collect);
        }
    }

    default List<ProductLineModuleTreeNodeVO> convert2ProductLineTreeVOList(List<LineModuleTreeNodeFeignVO> data){
        List<ProductLineModuleTreeNodeVO> res = new ArrayList<>();
        if (CollectionUtil.isEmpty(data)){
            return res;
        }
        for (LineModuleTreeNodeFeignVO lineModuleTreeNodeFeignVO : data) {
            ProductLineModuleTreeNodeVO treeNodeVO = new ProductLineModuleTreeNodeVO();
            treeNodeVO.setId(lineModuleTreeNodeFeignVO.getId());
            treeNodeVO.setParentId(lineModuleTreeNodeFeignVO.getParentId());
            treeNodeVO.setName(lineModuleTreeNodeFeignVO.getName());
            treeNodeVO.setCode(lineModuleTreeNodeFeignVO.getCode());
            treeNodeVO.setInfoList(convert2ProductLineVO(lineModuleTreeNodeFeignVO.getInfoList()));
            treeNodeVO.setChildren(convert2ProductLineTreeVOList(lineModuleTreeNodeFeignVO.getChildren()));
            res.add(treeNodeVO);
        }
        return res;
    }
}
