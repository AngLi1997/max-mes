package com.bmos.lims2.server.inspect.document.mapper;

import com.bmos.lims2.server.inspect.document.entity.DocumentConfigField;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DocumentConfigFieldMapper extends BaseMapperX<DocumentConfigField> {

    default void deleteByConfigId(Long configId) {
        delete(new LambdaQueryWrapperX<DocumentConfigField>()
                .eq(DocumentConfigField::getConfigId, configId));
    }

    default List<DocumentConfigField> selectByConfigId(Long configData) {
        return selectList(new LambdaQueryWrapperX<DocumentConfigField>()
                .eq(DocumentConfigField::getConfigId, configData)
                .orderByAsc(DocumentConfigField::getSort)
        );
    }
}

