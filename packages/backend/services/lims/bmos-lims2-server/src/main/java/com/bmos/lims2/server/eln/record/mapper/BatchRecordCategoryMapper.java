package com.bmos.lims2.server.eln.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.server.eln.record.entity.BatchRecordCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BatchRecordCategoryMapper extends BaseMapperX<BatchRecordCategory> {


    default List<BatchRecordCategory> list() {
        return selectList(new LambdaQueryWrapperX<BatchRecordCategory>()
                .orderByDesc(BatchRecordCategory::getSort));
    }

    default BatchRecordCategory queryById(String id) {
        return selectById(id);
    }

    default List<BatchRecordCategory> queryByParentId(String id) {
        return selectList(new LambdaQueryWrapperX<BatchRecordCategory>()
                .eq(BatchRecordCategory::getParentId, id));
    }

    default boolean existsByName(String name, Long excludeId) {
        return selectCount(new LambdaQueryWrapperX<BatchRecordCategory>()
                .eq(BatchRecordCategory::getName, name)
                .eq(BatchRecordCategory::getDeleted, Boolean.FALSE)
                .ne(excludeId != null, BatchRecordCategory::getId, excludeId)) > 0;
    }

    List<BatchRecordCategory> listCategory();

    default Boolean updateCategory(BatchRecordCategory batchRecordCategory) {
        return Db.saveOrUpdate(batchRecordCategory);
    }

    void deleteCategory(@Param("id") String id,@Param("userId") String userId);

    default Boolean insertCategory(BatchRecordCategory category) {
        return Db.saveOrUpdate(category);
    }
}
