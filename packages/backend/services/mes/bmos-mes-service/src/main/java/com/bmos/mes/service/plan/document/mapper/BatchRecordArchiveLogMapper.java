package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.service.plan.document.model.BatchRecordArchiveLog;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 归档生成的批记录的操作日志(BmBatchRecordArchiveLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-21 11:28:37
 */
@Mapper
public interface BatchRecordArchiveLogMapper extends BaseMapperX<BatchRecordArchiveLog> {

    default List<BatchRecordArchiveLog> selectByArchiveId(Long archiveId){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchiveLog>()
                .eq(BatchRecordArchiveLog::getBatchRecordArchiveId, archiveId)
                .orderByDesc(BatchRecordArchiveLog::getOperateTime));
    }
}

