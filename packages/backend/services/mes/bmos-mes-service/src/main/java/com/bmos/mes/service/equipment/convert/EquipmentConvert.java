package com.bmos.mes.service.equipment.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentConvert {

    EquipmentConvert INSTANCE = Mappers.getMapper(EquipmentConvert.class);
}
