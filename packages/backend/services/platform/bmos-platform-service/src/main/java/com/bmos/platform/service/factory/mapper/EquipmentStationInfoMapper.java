package com.bmos.platform.service.factory.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.equipment.service.dto.EquipmentStationPageDTO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.model.EquipmentStationInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface EquipmentStationInfoMapper extends BaseMapperX<EquipmentStationInfo> {


    List<String> getStationInfoNameListByStationId(@Param("id") Long id);

    default Boolean bindEquipment(List<EquipmentStationInfo> stationUserList) {
        return Db.saveOrUpdateBatch(stationUserList);
    }

    default List<EquipmentStationInfo> queryStationInfoByEquipmentId(Long id) {
        return selectList(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .eq(EquipmentStationInfo::getEquipmentId, id));
    }

    List<Long> queryInfoIdListByUserId(@Param("userId") String userId);

    default List<EquipmentStationInfo> getPageByStationIdList(EquipmentStationPageDTO stationPageDTO){
        return selectList(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .in(EquipmentStationInfo::getStationId, stationPageDTO.getStationIdList()));
    }

    default List<EquipmentStationInfo> queryEquipmentByStationId(Long stationId){
        return selectList(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .eq(EquipmentStationInfo::getStationId, stationId));
    }

    default Boolean deletcByIdList(List<Long> idList){
        return Db.removeByIds(idList,EquipmentStationInfo.class);
    }

    default List<EquipmentStationInfo> queryStationInfoByStationIdList(List<Long> stationIdList){
        return selectList(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .in(EquipmentStationInfo::getStationId, stationIdList));
    }

    List<EquipmentStation> queryStationNameByEquipmentId(@Param("id") Long id);

    default List<EquipmentStationInfo> selectByEquipmentId(Long equipmentId){
        return selectList(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .eq(EquipmentStationInfo::getEquipmentId, equipmentId));
    }

    /**
     * 判断当前工位是否绑定设备
     * @param stationId
     * @return
     */
    default boolean existByStationId(Long stationId){
        return exists(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .eq(EquipmentStationInfo::getStationId, stationId));
    }

    default void deleteRelationByStationId(Long stationId){
        delete(new LambdaQueryWrapperX<EquipmentStationInfo>()
                .eq(EquipmentStationInfo::getStationId, stationId));
    }
}
