package com.bmos.platform.service.factory.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.controller.vo.StationPageVO;
import com.bmos.platform.service.factory.controller.vo.StationVO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.service.dto.StationPageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentStationMapper extends BaseMapperX<EquipmentStation> {

    default List<EquipmentStation> queryStationListByModuleId(List<Long> idList) {
        return selectList(new LambdaQueryWrapperX<EquipmentStation>()
                .in(EquipmentStation::getModuleId, idList));
    }

    default List<EquipmentStation> queryStationListByCode(String code) {
        return selectList(new LambdaQueryWrapperX<EquipmentStation>()
                .eq(EquipmentStation::getCode, code));
    }

    default Boolean saveOrUpdateStation(EquipmentStation station) {
        return Db.saveOrUpdate(station);
    }

    default Boolean deleteStation(Long id) {
        return Db.removeById(id, EquipmentStation.class);
    }

    List<StationPageVO> getStationPage(StationPageDTO dto);

    StationVO getStationInfo(@Param("id") Long id);

    default List<EquipmentStation> queryStationListByRoomIds(List<Long> roomIdList, Boolean status) {
        return selectList(new LambdaQueryWrapperX<EquipmentStation>()
                .in(EquipmentStation::getModuleId, roomIdList)
                .eq(EquipmentStation::getEnable, status));
    }

    default List<EquipmentStation> selectStationByIdList(List<Long> stationIdList) {
        return selectList(new LambdaQueryWrapperX<EquipmentStation>()
                .in(EquipmentStation::getId, stationIdList));
    }

    default Boolean updateStationUseCount(List<EquipmentStation> stationList) {
        return Db.saveOrUpdateBatch(stationList);
    }

    default boolean existStation(Long moduleId){
        return exists(new LambdaQueryWrapperX<EquipmentStation>()
                .eq(EquipmentStation::getModuleId, moduleId));
    }

    default List<EquipmentStation> selectByModuleIdList(List<Long> moduleIdList){
        return selectList(new LambdaQueryWrapperX<EquipmentStation>()
                .in(EquipmentStation::getModuleId, moduleIdList)
                .eq(EquipmentStation::getEnable, Boolean.TRUE));
    }
}
