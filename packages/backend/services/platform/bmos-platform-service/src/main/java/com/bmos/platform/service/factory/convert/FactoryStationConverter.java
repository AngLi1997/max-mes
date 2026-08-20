package com.bmos.platform.service.factory.convert;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.facade.equipment.vo.EquipmentEasyInfoFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentFeignStationVO;
import com.bmos.platform.facade.equipment.vo.EquipmentModuleTreeNodeFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentInfoStationVO;
import com.bmos.platform.service.equipment.controller.vo.StationEquipmentInfoTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.FactoryTreeNodeVO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.EquipmentStationUser;
import com.bmos.platform.service.factory.service.dto.StationSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface FactoryStationConverter {

    FactoryStationConverter INSTANCE = Mappers.getMapper(FactoryStationConverter.class);

    EquipmentStation convertToStation(StationSaveDTO dto);

    List<FactoryStationFeignVO> convert2FeignVO(List<EquipmentStation> stationList);

    List<EquipmentFeignStationVO> convert2FeignStationVO(List<EquipmentStation> stationList);

    default List<EquipmentModuleTreeNodeFeignVO> convert2EquipmentTree(List<StationEquipmentInfoTreeNodeVO> stationEquipmentInfoTreeNodeVOS){
        List<EquipmentModuleTreeNodeFeignVO> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(stationEquipmentInfoTreeNodeVOS)){
            return list;
        }
        for (StationEquipmentInfoTreeNodeVO treeNodeVO : stationEquipmentInfoTreeNodeVOS) {
            EquipmentModuleTreeNodeFeignVO feignVO = new EquipmentModuleTreeNodeFeignVO();
            feignVO.setId(treeNodeVO.getId());
            feignVO.setParentId(treeNodeVO.getParentId());
            feignVO.setName(treeNodeVO.getName());
            feignVO.setCode(treeNodeVO.getCode());
            feignVO.setChildren(convert2EquipmentTree(treeNodeVO.getChildren()));
            feignVO.setInfoList(convert2EquipmentEasyFignVO(treeNodeVO.getInfoList()));
            list.add(feignVO);
        }
        return list;
    }

    List<EquipmentEasyInfoFeignVO> convert2EquipmentEasyFignVO(List<EquipmentInfoStationVO> infoList);

    FactoryStationFeignVO convertToFeignVO(EquipmentStation equipmentStation);

    default List<EquipmentStationUser> convertToStationUserList(String userId, List<Long> stationIdList){
        List<EquipmentStationUser> equipmentStationUsers = new ArrayList<>();
        if (CollUtil.isEmpty(stationIdList)){
            return equipmentStationUsers;
        }
        for (Long stationId : stationIdList) {
            EquipmentStationUser equipmentStationUser = new EquipmentStationUser();
            equipmentStationUser.setUserId(userId);
            equipmentStationUser.setStationId(stationId);
            equipmentStationUsers.add(equipmentStationUser);
        }
        return equipmentStationUsers;
    }

    default FactoryTreeNodeVO convert2StationTreeNodeVO(EquipmentStation equipmentStation){
        FactoryTreeNodeVO factoryTreeNodeVO = new FactoryTreeNodeVO();
        factoryTreeNodeVO.setId(equipmentStation.getId());
        factoryTreeNodeVO.setName(equipmentStation.getName());
        factoryTreeNodeVO.setCode(equipmentStation.getCode());
        factoryTreeNodeVO.setType(FactoryModuleEnum.STATION.getType());
        return factoryTreeNodeVO;
    }
}
