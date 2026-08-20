package com.bmos.mes.service.plan.document.mapper;

import com.bmos.audit.engine.core.db.repository.base.LambdaQueryWrapperX;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveOperateTypeEnum;
import com.bmos.mes.service.plan.document.model.BatchRecordArchiveGenerate;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (BmBatchRecordArchiveGenerate)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-27 18:51:40
 */
@Mapper
public interface BatchRecordArchiveGenerateMapper extends BaseMapperX<BatchRecordArchiveGenerate> {

    default List<BatchRecordArchiveGenerate> selectVerifyArchive(){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchiveGenerate>()
                .eq(BatchRecordArchiveGenerate::getComplete, true)
                .eq(BatchRecordArchiveGenerate::getOperateType, BatchRecordArchiveOperateTypeEnum.VERIFIER.getValue())
                .eq(BatchRecordArchiveGenerate::getDeleteFileFlag, false)
                .ne(BatchRecordArchiveGenerate::getPath, RecordConstant.ERROR_PATH)
                .isNotNull(BatchRecordArchiveGenerate::getPath));
    }

    default List<BatchRecordArchiveGenerate> selectNotVerifyArchive(){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchiveGenerate>()
                .eq(BatchRecordArchiveGenerate::getComplete, true)
                .ne(BatchRecordArchiveGenerate::getOperateType, BatchRecordArchiveOperateTypeEnum.VERIFIER.getValue())
                .ne(BatchRecordArchiveGenerate::getPath, RecordConstant.ERROR_PATH)
                .isNotNull(BatchRecordArchiveGenerate::getPath));
    }
}

