package com.bmos.mes.service.record.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.record.model.BatchRecordParse;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface BatchRecordParseMapper extends BaseMapperX<BatchRecordParse> {


    default Boolean saveOrUpdateParse(List<BatchRecordParse> parses){
        return Db.saveOrUpdateBatch(parses);
    }

    void updateParseList(@Param("list") List<Long> itemIds);

    default List<BatchRecordParse> selectByItemId(List<Long> itemIds){
        if (CollUtil.isEmpty(itemIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<BatchRecordParse>().in(BatchRecordParse::getId,itemIds));
    }

    default Boolean saveOrUpdateOne(BatchRecordParse parse){
        return Db.saveOrUpdate(parse);
    }


    default Boolean deleteParse(BatchRecordParse parse){
        return Db.removeById(parse);
    }
}
