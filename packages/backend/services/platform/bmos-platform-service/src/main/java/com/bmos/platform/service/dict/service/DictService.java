package com.bmos.platform.service.dict.service;

import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.facade.dict.enums.DictCategoryEnum;
import com.bmos.platform.service.dict.dto.DictListQueryDTO;
import com.bmos.platform.service.dict.dto.SaveDictDTO;
import com.bmos.platform.service.dict.dto.UpdateDictDTO;
import com.bmos.platform.service.dict.vo.DictListVO;
import com.bmos.platform.service.dict.vo.DictVO;
import com.bmos.platform.service.dict.vo.DictWatchVO;

import java.util.List;

public interface DictService {


    List<DictListVO> listDict(DictListQueryDTO dto);

    Boolean saveDict(SaveDictDTO dto);

    Boolean deleteDict(Long id);

    Boolean updateDict(UpdateDictDTO dto);

    DictWatchVO watchDict(Long id);

    List<DictVO> listDictDown(Long dictId);

    List<DictVO> queryDictDetailByCode(String code);

    /**
     * 根据code查询字典详情信息
     * @param code
     * @return
     */
    DictDetailFeignVO selectDictDetailByCode(String code);

    /**
     * 根据字典详情查询字典分类
     * @param dictTypeList
     * @return
     */
    List<DictDetailFeignVO> selectDictByCategory(List<String> dictTypeList);
}
