package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.service.plan.document.model.BatchTemplateVariable;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 批记录模板中配置的所有变量(BmBatchTemplateVariable)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-19 14:12:41
 */
@Mapper
public interface BatchTemplateVariableMapper extends BaseMapperX<BatchTemplateVariable> {

}

