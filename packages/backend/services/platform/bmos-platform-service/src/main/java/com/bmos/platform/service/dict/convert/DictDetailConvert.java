package com.bmos.platform.service.dict.convert;

import com.bmos.platform.service.dict.dto.SaveDictDetailDTO;
import com.bmos.platform.service.dict.dto.UpdateDetailDTO;
import com.bmos.platform.service.dict.model.DictDetail;
import com.bmos.platform.service.dict.vo.DictDetailListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DictDetailConvert {

    DictDetailConvert INSTANCE = Mappers.getMapper(DictDetailConvert.class);

    List<DictDetail> converTolist(List<SaveDictDetailDTO> list);

    DictDetail convertToDetail(UpdateDetailDTO dto);

    List<DictDetail> convertToDetailList(List<UpdateDetailDTO> list);

    DictDetailListVO convertToVo(DictDetail detail);

    DictDetail convertToSaveDetail(SaveDictDetailDTO dto);

    List<DictDetailListVO> convertToVoList(List<DictDetail> details);
}
