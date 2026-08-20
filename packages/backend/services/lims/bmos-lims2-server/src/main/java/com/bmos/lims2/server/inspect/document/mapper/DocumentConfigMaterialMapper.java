package com.bmos.lims2.server.inspect.document.mapper;

import com.bmos.lims2.server.inspect.document.entity.DocumentConfigMaterial;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DocumentConfigMaterialMapper extends BaseMapperX<DocumentConfigMaterial> {


    default void deleteByConfigId(Long configId) {
        delete(new LambdaQueryWrapperX<DocumentConfigMaterial>()
                .eq(DocumentConfigMaterial::getConfigId, configId));
    }

    default void deleteByProductIds(List<Long> productIdList) {
        delete(new LambdaQueryWrapperX<DocumentConfigMaterial>()
                .in(DocumentConfigMaterial::getProductId, productIdList));
    }

    default List<DocumentConfigMaterial> selectByConfigId(Long configId) {
        return selectList(new LambdaQueryWrapperX<DocumentConfigMaterial>()
                .eq(DocumentConfigMaterial::getConfigId, configId));
    }

    default List<DocumentConfigMaterial> selectConfigsByProductId(Long productId) {
        return selectList(new LambdaQueryWrapperX<DocumentConfigMaterial>()
                .eq(DocumentConfigMaterial::getProductId, productId));
    }
}

