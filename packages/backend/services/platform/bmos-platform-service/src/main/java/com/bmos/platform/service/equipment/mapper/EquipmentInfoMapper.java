package com.bmos.platform.service.equipment.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.facade.equipment.vo.EquipmentVO;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.service.dto.EquipmentAppPageDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentPageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentInfoMapper extends BaseMapperX<EquipmentInfo> {

    default List<EquipmentInfo> queryInfoListByCategoryIdAndEnable(Long id, Boolean enable) {
        return selectList(new LambdaQueryWrapperX<EquipmentInfo>()
                .eq(EquipmentInfo::getCategoryId, id)
                .eq(EquipmentInfo::getEnable, enable));
    }

    default List<EquipmentInfo> selectInfoList() {
        return selectList(new LambdaQueryWrapperX<>());
    }

    default Boolean saveOrUpdateInfo(EquipmentInfo equipmentInfo) {
        return Db.saveOrUpdate(equipmentInfo);
    }

    List<EquipmentInfoVO> getEquipmentPage(EquipmentPageDTO dto);

    default EquipmentInfo queryInfoById(Long id) {
        return selectOne(new LambdaQueryWrapperX<EquipmentInfo>()
                .eq(EquipmentInfo::getId, id));
    }

    default Boolean deleteEquipment(Long id) {
        return Db.removeById(id, EquipmentInfo.class);
    }

    List<EquipmentAppPageVO> getEquipmentAppList(EquipmentAppPageDTO dto);

    List<EquipmentInfoVO> getConfigByStationId(@Param("stationId") List<String> stationId);

    List<EquipmentInfoVO> getEquipmentByTagCode(@Param("tagCode") String tagCode);

    default EquipmentInfo getEquipmentByEquipmentCode(String equipmentCode){
        return selectOne(new LambdaQueryWrapperX<EquipmentInfo>()
                .eq(EquipmentInfo::getCode,equipmentCode));
    }

    /**
     * 查询所有被占用的设备
     * @return
     */
    default List<EquipmentInfo> selectBusinessOccupyEquipment(){
        return selectList(new LambdaQueryWrapperX<EquipmentInfo>()
                .eq(EquipmentInfo::getStatus, EquipmentStatusCodeEnum.OCCUPY.getCode()));
    }

    List<EquipmentInfoVO> getConfigByStationIdList(@Param("stationIdList") List<Long> stationIdList);

    List<EquipmentInfoVO> getDistinctConfigByStationIdList(@Param("stationIdList") List<Long> stationIdList);

    default List<EquipmentInfo> selectListByIdListAndStatus(List<Long> equipmentIdList, List<Integer> statusList){
        return selectList(new LambdaQueryWrapperX<EquipmentInfo>()
                .in(EquipmentInfo::getId,equipmentIdList)
                .in(EquipmentInfo::getStatus,statusList));
    }

    default List<EquipmentInfo> selectInfoListByEnable(Boolean enable){
        return selectList(new LambdaQueryWrapperX<EquipmentInfo>()
                .eq(EquipmentInfo::getEnable, enable));
    }

    List<EquipmentVO> getDeleteEquipment(@Param("equipmentIdList") List<Long> equipmentIdList);
}
