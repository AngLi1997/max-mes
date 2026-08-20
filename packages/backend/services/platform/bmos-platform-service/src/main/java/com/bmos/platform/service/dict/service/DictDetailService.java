package com.bmos.platform.service.dict.service;

import com.bmos.platform.service.dict.dto.DictDetailListQueryDTO;
import com.bmos.platform.service.dict.dto.SaveDictDetailDTO;
import com.bmos.platform.service.dict.dto.UpdateDetailDTO;
import com.bmos.platform.service.dict.vo.DictDetailListVO;

import java.util.List;

public interface DictDetailService {


    List<DictDetailListVO> listDictDetail(DictDetailListQueryDTO dto);

    Boolean updateDictDetail(UpdateDetailDTO dto);

    Boolean deleteDictDetail(Long id);

    DictDetailListVO watchDictDetail(Long id);

    Boolean saveDictDetail(SaveDictDetailDTO dto);
}
