package com.bmos.mes.service.product.mapper;

import com.bmos.mes.service.product.dto.ProductMaterialCategoryQueryDTO;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMaterialCategoryMapper extends BaseMapperX<ProductMaterialCategory> {

    default boolean codeExisted(Long id, String code) {
        return exists(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .eq(ProductMaterialCategory::getCode, code)
                .neIfPresent(ProductMaterialCategory::getId, id));
    }

    default boolean existsChild(Long id) {
        return exists(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .eq(ProductMaterialCategory::getParentId, id));
    }

    default List<ProductMaterialCategory> selectListByQuery(ProductMaterialCategoryQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .eq(ProductMaterialCategory::getCategoryType, dto.getCategoryType())
                .likeIfPresent(ProductMaterialCategory::getName, dto.getKeyword()).orderByAsc(ProductMaterialCategory::getMergeCode));

    }

    default boolean existedId(Long id) {
        return exists(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .eq(ProductMaterialCategory::getId, id));
    }

    default List<ProductMaterialCategory> selectListByType(Integer business) {
        return selectList(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .eq(ProductMaterialCategory::getCategoryType, business)
                .orderByDesc(ProductMaterialCategory::getCode));
    }

    default List<ProductMaterialCategory> selectByTypeAndPlatformIds(List<Long> issueIds, Integer business) {
        return selectList(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .eq(ProductMaterialCategory::getCategoryType, business)
                .in(ProductMaterialCategory::getPlatformCategoryId, issueIds));
    }

    default List<ProductMaterialCategory> selectByTypesAndPlatformIds(List<Long> issueIds, List<Integer> businesses) {
        return selectList(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .in(ProductMaterialCategory::getCategoryType, businesses)
                .in(ProductMaterialCategory::getPlatformCategoryId, issueIds));
    }

    default List<ProductMaterialCategory> selectListByTypes(List<Integer> types) {
        return selectList(new LambdaQueryWrapperX<ProductMaterialCategory>()
                .inIfPresent(ProductMaterialCategory::getCategoryType, types));
    }
}
