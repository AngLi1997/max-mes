package com.bmos.mes.service.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.record.model.BatchRecordProduct;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BatchRecordProductMapper extends BaseMapperX<BatchRecordProduct> {


    default Boolean saveProduct(List<BatchRecordProduct> list) {
        return Db.saveBatch(list);
    }

    default void deleteByRecordId(Long recordId) {
        delete(new LambdaQueryWrapperX<BatchRecordProduct>().eq(BatchRecordProduct::getRecordId, recordId));
    }

    default void deleteByProductId(Long productId) {
        delete(new LambdaQueryWrapperX<BatchRecordProduct>().eq(BatchRecordProduct::getProductId, productId));
    }

    default List<BatchRecordProduct> selectByProductIds(Long productId) {
        return selectList(new LambdaQueryWrapperX<BatchRecordProduct>().eq(BatchRecordProduct::getProductId, productId));
    }

    default List<BatchRecordProduct> queryProductIdByRecordId(Long recordId){
        return selectList(new LambdaQueryWrapperX<BatchRecordProduct>().eq(BatchRecordProduct::getRecordId, recordId));
    }
}
