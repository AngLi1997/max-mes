package com.bmos.platform.service.dict.convert;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.facade.dict.vo.DictDataFeignVO;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.service.dict.dto.SaveDictDTO;
import com.bmos.platform.service.dict.dto.UpdateDictDTO;
import com.bmos.platform.service.dict.model.Dict;
import com.bmos.platform.service.dict.model.DictDetail;
import com.bmos.platform.service.dict.vo.DictWatchVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface DictConvert {

    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);

    Dict converToDict(SaveDictDTO dictDTO);

    Dict converToUpdateDict(UpdateDictDTO dictDTO);

    DictWatchVO converToVo(Dict dict);

    default DictDetailFeignVO convert2DetailFeignVO(Dict dict, List<DictDetail> dictDetails){
        DictDetailFeignVO dictDetailFeignVO = convert2BaseDetailFeignVO(dict);
        if (CollectionUtil.isEmpty(dictDetails)){
            return dictDetailFeignVO;
        }
        dictDetailFeignVO.setDictDataList(convert2DictDataFeignVO(dictDetails));
        return dictDetailFeignVO;
    }

    List<DictDataFeignVO> convert2DictDataFeignVO(List<DictDetail> dictDetails);

    DictDetailFeignVO convert2BaseDetailFeignVO(Dict dict);

    default List<DictDetailFeignVO> convert2DictFeignVO(List<Dict> dicts, Map<Long, List<DictDetail>> dictDetailMap){
        List<DictDetailFeignVO> dictDetailFeignVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(dicts)){
            return dictDetailFeignVOS;
        }
        for (Dict dict : dicts) {
            DictDetailFeignVO dictDetailFeignVO = convert2DetailFeignVO(dict, dictDetailMap.get(dict.getId()));
            dictDetailFeignVOS.add(dictDetailFeignVO);
        }
        return dictDetailFeignVOS;
    }
}
