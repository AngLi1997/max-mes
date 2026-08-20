package com.bmos.platform.service.dict.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.dict.enums.DictCodeConstants;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.facade.dict.enums.DictCategoryEnum;
import com.bmos.platform.service.dict.convert.DictConvert;
import com.bmos.platform.service.dict.convert.DictDetailConvert;
import com.bmos.platform.service.dict.dto.DictListQueryDTO;
import com.bmos.platform.service.dict.dto.SaveDictDTO;
import com.bmos.platform.service.dict.dto.UpdateDictDTO;
import com.bmos.platform.service.dict.mapper.DictDetailMapper;
import com.bmos.platform.service.dict.mapper.DictMapper;
import com.bmos.platform.service.dict.model.Dict;
import com.bmos.platform.service.dict.model.DictDetail;
import com.bmos.platform.service.dict.service.DictService;
import com.bmos.platform.service.dict.vo.DictListVO;
import com.bmos.platform.service.dict.vo.DictVO;
import com.bmos.platform.service.dict.vo.DictWatchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private DictDetailMapper dictDetailMapper;


    @Override
    public List<DictListVO> listDict(DictListQueryDTO dto) {
        return dictMapper.listDict(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDict(SaveDictDTO dto) {
        try {
            Long id = CustomIdGenerator.nextId();
            List<DictDetail> dictDetails = null;
            Dict dict = DictConvert.INSTANCE.converToDict(dto);
            if (CollectionUtil.isNotEmpty(dto.getDetailList())) {
                dict.setId(id);
                dto.getDetailList().forEach(item -> item.setDictId(id));
                dictDetails = DictDetailConvert.INSTANCE.converTolist(dto.getDetailList());
            }
            dictMapper.saveDict(dict);
            dictDetailMapper.saveDictDetail(dictDetails);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.DICT_CODE_TO_EXIST);
        }
    }

    @Override
    public Boolean deleteDict(Long id) {
        Dict dict = dictMapper.selectDict(id);
        if (ObjectUtil.isEmpty(dict) || Boolean.TRUE.equals(dict.getState())) {
            throw new BmosException(PlatformResponseCode.DICT_TO_USE);
        }
        List<DictDetail> dictDetails = dictDetailMapper.queryListById(id);
        if (CollUtil.isNotEmpty(dictDetails)) {
            List<Long> dictDetailIdList = CollectionUtils.convertList(dictDetails, DictDetail::getId);
            dictDetailMapper.deleteByIdList(dictDetailIdList);
        }
        dictMapper.deleteDict(id, SysUserHolder.getUser().getUserId());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDict(UpdateDictDTO dto) {
        try {
            dictMapper.updateDict(DictConvert.INSTANCE.converToUpdateDict(dto));
            if (CollUtil.isNotEmpty(dto.getDictIdList())) {
                dictDetailMapper.deleteByIdList(dto.getDictIdList());
            }
            if (CollUtil.isNotEmpty(dto.getDetailList())) {
                List<DictDetail> dictDetails = DictDetailConvert.INSTANCE.convertToDetailList(dto.getDetailList());
                dictDetails.forEach(item -> item.setDictId(dto.getId()));
                return dictDetailMapper.updateList(dictDetails);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.DICT_CODE_TO_EXIST);
        }
    }

    @Override
    public DictWatchVO watchDict(Long id) {
        Dict dict = dictMapper.watchDict(id);
        DictWatchVO vo = DictConvert.INSTANCE.converToVo(dict);
        vo.setDetailList(DictDetailConvert.INSTANCE.convertToVoList(dictDetailMapper.queryListById(id)));
        return vo;
    }

    @Override
    public List<DictVO> listDictDown(Long dictId) {
        List<DictVO> list;
        if (ObjectUtil.isNotNull(dictId)) {
            list = dictDetailMapper.listDictDetailDown(dictId);
        } else {
            list = dictMapper.listDictDown();
        }
        return list;
    }

    @Override
    public List<DictVO> queryDictDetailByCode(String code) {
        Dict dict = dictMapper.queryDictBycode(code);
        if (ObjectUtil.isEmpty(dict) || !StrUtil.equals(code, dict.getDictCode())) {
            return Collections.emptyList();
        }
        return dictDetailMapper.listDictDetailDown(dict.getId());
    }

    @Override
    public DictDetailFeignVO selectDictDetailByCode(String code) {
        Dict dict = dictMapper.queryDictBycode(code);
        if (ObjectUtil.isNull(dict)){
            throw new BmosException(PlatformResponseCode.DICT_NOT_EXIST);
        }
        List<DictDetail> dictDetails = dictDetailMapper.selectDetailList(dict.getId());
        return DictConvert.INSTANCE.convert2DetailFeignVO(dict, dictDetails);
    }

    @Override
    public List<DictDetailFeignVO> selectDictByCategory(List<String> dictTypeList) {
        List<Dict> dicts = dictMapper.selectByCodeList(dictTypeList);
        if (CollectionUtil.isEmpty(dicts)){
            return new ArrayList<>();
        }
        List<DictDetail> dictDetails = dictDetailMapper.selectDetailByDictIdList(CollectionUtils.convertList(dicts, Dict::getId));
        Map<Long, List<DictDetail>> dictDetailMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(dictDetails)){
            dictDetailMap = dictDetails.stream().collect(Collectors.groupingBy(DictDetail::getDictId));
        }
        return DictConvert.INSTANCE.convert2DictFeignVO(dicts, dictDetailMap);
    }
}
