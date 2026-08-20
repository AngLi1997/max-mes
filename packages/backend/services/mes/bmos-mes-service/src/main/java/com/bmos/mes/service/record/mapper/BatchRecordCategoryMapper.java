package com.bmos.mes.service.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.record.model.BatchRecordCategory;
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

    List<BatchRecordCategory> listCategory();

    default Boolean updateCategory(BatchRecordCategory batchRecordCategory) {
        return Db.saveOrUpdate(batchRecordCategory);
    }

    void deleteCategory(@Param("id") String id,@Param("userId") String userId);

    default Boolean insertCategory(BatchRecordCategory category) {
        return Db.saveOrUpdate(category);
    }
}
