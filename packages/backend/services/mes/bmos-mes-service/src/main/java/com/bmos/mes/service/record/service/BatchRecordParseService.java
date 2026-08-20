package com.bmos.mes.service.record.service;

import com.bmos.mes.service.record.model.BatchRecordParse;

import java.util.Arrays;
import java.util.List;

public interface BatchRecordParseService {


    Boolean saveOrUpdateParse(List<BatchRecordParse> parses);


    Boolean saveOrUpdateOne(BatchRecordParse convertToParseDo);

    List<BatchRecordParse> selectByItemId(List<Long> itemIdS);

    void insertBatch(List<BatchRecordParse> parseList);

    void deleteParseById(Long itemId);
}
