package com.bmos.wms.service.cargo.convert;

import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.cargo.vo.CargoTreeVO;
import com.bmos.wms.service.cargo.vo.CargoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/23 10:36
 */
@Mapper
public interface CargoConvert {

    CargoConvert INSTANCE = Mappers.getMapper(CargoConvert.class);

    @Mapping(target = "unit", expression = "java(cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.unit.service.UnitCache.class).getGlobalUnitName(cargo.getUnitId()))")
    CargoVO convertToVO(Cargo cargo);

    List<CargoVO> convertToVO(List<Cargo> cargos);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "cargoCategoryId", target = "parentId")
    @Mapping(source = "cargoName", target = "name")
    @Mapping(source = "mergeCode", target = "mergeCode")
    @Mapping(target = "isCategory", expression = "java(false)")
    CargoTreeVO convertToTreeVO(Cargo cargo);

    List<CargoTreeVO> convertToTreeVO(List<Cargo> list);
}
