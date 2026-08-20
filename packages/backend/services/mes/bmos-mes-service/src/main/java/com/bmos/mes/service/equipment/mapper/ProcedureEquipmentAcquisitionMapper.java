package com.bmos.mes.service.equipment.mapper;

import com.bmos.mes.service.equipment.mapper.entity.ProcedureEquipmentAcquisition;
import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionComponentDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工序步骤设备数据采集数据(BmProcedureEquipmentAcquisition)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-23 14:07:03
 */
@Mapper
public interface ProcedureEquipmentAcquisitionMapper extends BaseMapperX<ProcedureEquipmentAcquisition> {
    default List<ProcedureEquipmentAcquisition> selectComponentAcquisitionDataList(EquipmentAcquisitionComponentDTO dto){
        return selectList(new LambdaQueryWrapperX<ProcedureEquipmentAcquisition>()
                .eq(ProcedureEquipmentAcquisition::getProductPlanId, dto.getProductPlanId())
                .eq(ProcedureEquipmentAcquisition::getComponentId, dto.getComponentId())
                .eq(ProcedureEquipmentAcquisition::getCopyVersion, dto.getCopyVersion())
                .eq(ProcedureEquipmentAcquisition::getReuse, dto.getReuse())
                .eq(!dto.getReuse(), ProcedureEquipmentAcquisition::getProcedureStepModelId, dto.getProcedureStepModelId())
                .eq(ProcedureEquipmentAcquisition::getGroupComponentId, dto.getEquipmentAcquisitionGroupComponentId()));
    }
}

