package com.bmos.mes.service.record.mapper;

import com.bmos.mes.service.record.model.BatchRecordExpression;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BatchRecordExpressionMapper extends BaseMapperX<BatchRecordExpression> {


    default List<BatchRecordExpression> selectByRecordId(Long id) {
        return selectList(new LambdaQueryWrapperX<BatchRecordExpression>()
                .eq(BatchRecordExpression::getRecordId, id));
    }

    default void deleteByRecordId(Long id){
        delete(new LambdaQueryWrapperX<BatchRecordExpression>()
                .eq(BatchRecordExpression::getRecordId, id));
    }

    default List<BatchRecordExpression> selectByExpressionId(Long id) {
        return selectList(new LambdaQueryWrapperX<BatchRecordExpression>()
                .eq(BatchRecordExpression::getExpressionId, id));
    }

    default void deleteByExpressionId(Long id){
        delete(new LambdaQueryWrapperX<BatchRecordExpression>()
                .eq(BatchRecordExpression::getExpressionId, id));
    }
}
