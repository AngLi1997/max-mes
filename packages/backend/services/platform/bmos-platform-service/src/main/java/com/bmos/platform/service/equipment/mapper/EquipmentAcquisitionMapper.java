package com.bmos.platform.service.equipment.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.equipment.service.dto.DataPointDTO;
import com.bmos.platform.service.equipment.model.EquipmentAcquisition;
import com.bmos.platform.service.equipment.service.dto.EquipmentAcquisitionDataPointDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备-点位关联信息(BpEquipmentAcquisition)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-22 11:36:43
 */
@Mapper
public interface EquipmentAcquisitionMapper extends BaseMapperX<EquipmentAcquisition> {
    List<EquipmentAcquisitionDataPointDTO> selectAcquisitionByEquipmentId(@Param("equipmentId") Long equipmentId);

    EquipmentAcquisitionDataPointDTO selectAcquisitionByEquipmentIdAndAcquisitionId(@Param("equipmentId") Long equipmentId, @Param("acquisitionId") Long acquisitionId);

    List<EquipmentAcquisitionDataPointDTO> selectAcquisitionByEquipmentIdAndAcquisitionIds(@Param("dataPoints") List<DataPointDTO> dataPointDTOs);
}

