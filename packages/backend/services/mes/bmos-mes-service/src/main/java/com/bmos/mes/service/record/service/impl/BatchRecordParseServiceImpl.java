package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.record.mapper.BatchRecordParseMapper;
import com.bmos.mes.service.record.model.BatchRecordParse;
import com.bmos.mes.service.record.service.BatchRecordParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BatchRecordParseServiceImpl implements BatchRecordParseService {

    @Autowired
    private BatchRecordParseMapper parseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateParse(List<BatchRecordParse> parses) {
        if (CollUtil.isEmpty(parses)){
            return true;
        }
        return parseMapper.saveOrUpdateParse(parses);
    }

    @Override
    public Boolean saveOrUpdateOne(BatchRecordParse parse) {
        return parseMapper.saveOrUpdateOne(parse);
    }

    @Override
    public List<BatchRecordParse> selectByItemId(List<Long> itemIdS) {
        return parseMapper.selectByItemId(itemIdS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<BatchRecordParse> parseList) {
        parseMapper.insertBatch(parseList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteParseById(Long itemId) {
        BatchRecordParse parse = parseMapper.selectById(itemId);
        if (ObjectUtil.isEmpty(parse)){
            return;
        }
        parseMapper.deleteParse(parse);
    }
}
