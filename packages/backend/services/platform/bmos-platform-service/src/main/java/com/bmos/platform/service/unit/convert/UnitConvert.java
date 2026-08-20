package com.bmos.platform.service.unit.convert;

import com.bmos.platform.service.unit.dto.SaveUnitDTO;
import com.bmos.platform.service.unit.dto.UpdateUnitDTO;
import com.bmos.platform.service.unit.model.Unit;
import com.bmos.platform.service.unit.vo.CommonUnitVO;
import com.bmos.platform.service.unit.vo.UnitVO;
import com.bmos.unit.vo.CacheUnit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UnitConvert {

    UnitConvert INSTANCE = Mappers.getMapper(UnitConvert.class);

    Unit convertToUnit(SaveUnitDTO dto);

    UnitVO converToUnitVo(Unit unit);
    List<UnitVO> convertToUnitVo(List<Unit> unit);

    Unit convertToUpdateUnit(UpdateUnitDTO dto);

    CommonUnitVO convertToCommonUnitVO(CacheUnit globalUnit);
}
