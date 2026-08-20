package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.service.plan.document.model.BatchTemplateCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 批记录模板分类(BmBatchTemplateCategory)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-19 11:05:34
 */
@Mapper
public interface BatchTemplateCategoryMapper extends BaseMapperX<BatchTemplateCategory> {

    /**
     * 查询所有分类
     * @return
     */
    default List<BatchTemplateCategory> selectAll(){
        return selectList(new LambdaQueryWrapperX<>());
    }

    default List<BatchTemplateCategory> selectByParentId(Long parentId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateCategory>()
                .eq(BatchTemplateCategory::getParentId, parentId));
    }

    default boolean existsByParentId(Long parentId){
        return exists(new LambdaQueryWrapperX<BatchTemplateCategory>()
                .eq(BatchTemplateCategory::getParentId, parentId));
    }

    /**
     * 根据名称查询分类
     * @param name
     * @return
     */
    default BatchTemplateCategory selectByName(String name){
        return selectOne(new LambdaQueryWrapperX<BatchTemplateCategory>().
                eq(BatchTemplateCategory::getName, name)
                .last(" limit 1"));
    }
}

