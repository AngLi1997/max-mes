package com.bmos.platform.service.dict.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.dict.convert.DictDetailConvert;
import com.bmos.platform.service.dict.dto.DictDetailListQueryDTO;
import com.bmos.platform.service.dict.dto.SaveDictDetailDTO;
import com.bmos.platform.service.dict.dto.UpdateDetailDTO;
import com.bmos.platform.service.dict.mapper.DictDetailMapper;
import com.bmos.platform.service.dict.service.DictDetailService;
import com.bmos.platform.service.dict.vo.DictDetailListVO;
import com.bmos.platform.service.system.code.mapper.CodeRuleVersionDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictDetailServiceImpl implements DictDetailService {

    @Autowired
    private DictDetailMapper detailMapper;

    @Autowired
    private CodeRuleVersionDetailMapper versionDetailMapper;

    @Override
    public List<DictDetailListVO> listDictDetail(DictDetailListQueryDTO dto) {
        return detailMapper.listDictDetail(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDictDetail(UpdateDetailDTO dto) {
        try {
            return detailMapper.updateDictDetail(DictDetailConvert.INSTANCE.convertToDetail(dto));
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.DICT_TO_EXIST);
        }

    }

    @Override
    public Boolean deleteDictDetail(Long id) {
        detailMapper.deleteDictDetail(id,SysUserHolder.getUser().getUserId());
        return Boolean.TRUE;
    }

    @Override
    public DictDetailListVO watchDictDetail(Long id) {
        return DictDetailConvert.INSTANCE.convertToVo(detailMapper.watchDictDetail(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDictDetail(SaveDictDetailDTO dto) {
        try {
            return detailMapper.saveDictDetailOne(DictDetailConvert.INSTANCE.convertToSaveDetail(dto));
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.DICT_TO_EXIST);
        }

    }
}
