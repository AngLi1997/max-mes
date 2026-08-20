package com.bmos.wms.service.cargo.convert;

import com.bmos.wms.service.cargo.model.CargoCategory;
import com.bmos.wms.service.cargo.vo.CargoCategoryVO;
import com.bmos.wms.service.cargo.vo.CargoTreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/23 10:36
 */
@Mapper
public interface CargoCategoryConvert {

    CargoCategoryConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(CargoCategoryConvert.class);

    CargoCategoryVO convertToVO(CargoCategory cargoCategory);

    List<CargoCategoryVO> convertToVO(List<CargoCategory> list);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parentId", target = "parentId")
    @Mapping(source = "cargoCategoryName", target = "name")
    @Mapping(source = "cargoCategoryMergeCode", target = "mergeCode")
    @Mapping(target = "isCategory", expression = "java(true)")
    CargoTreeVO convertToTreeVO(CargoCategory cargoCategory);

    List<CargoTreeVO> convertToTreeVO(List<CargoCategory> list);
}
