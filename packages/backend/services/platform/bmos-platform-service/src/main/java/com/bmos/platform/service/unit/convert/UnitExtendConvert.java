package com.bmos.platform.service.unit.convert;

import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;
import com.bmos.platform.service.unit.dto.SaveUnitExtendDTO;
import com.bmos.platform.service.unit.dto.UpdateUnitExtendDTO;
import com.bmos.platform.service.unit.model.UnitExtend;
import com.bmos.platform.service.unit.vo.UnitExtendListVO;
import com.bmos.platform.service.unit.vo.UnitExtendVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface UnitExtendConvert {

    UnitExtendConvert INSTANCE = Mappers.getMapper(UnitExtendConvert.class);

    List<UnitExtendVO> convertToUnitVoList(List<UnitExtend> list);

    UnitExtend convertToExtendDto(SaveUnitExtendDTO dto);

    UnitExtendVO convertToExtendVo(UnitExtend extend);

    UnitExtend convertToUpdateDto(UpdateUnitExtendDTO dto);

    List<UnitExtendListVO> convertToUnitExtendListVO(List<MaterialBoundExtendUnitListVO> list);
}
