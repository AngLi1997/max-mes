package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.service.plan.document.model.BatchTemplateOperateLog;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 批记录模板信息版本与工艺的绑定关系(BmBatchTemplateOperateLog)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-19 14:11:58
 */
@Mapper
public interface BatchTemplateOperateLogMapper extends BaseMapperX<BatchTemplateOperateLog> {

    default List<BatchTemplateOperateLog> selectByVersionId(Long templateVersionId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateOperateLog>()
                .eq(BatchTemplateOperateLog::getBatchTemplateVersionId, templateVersionId)
                .orderByDesc(BatchTemplateOperateLog::getOperateTime));
    }
}

