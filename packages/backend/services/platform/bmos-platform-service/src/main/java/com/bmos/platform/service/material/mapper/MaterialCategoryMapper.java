package com.bmos.platform.service.material.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.material.dto.MaterialCategoryUpdateDTO;
import com.bmos.platform.service.material.model.MaterialCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialCategoryMapper extends BaseMapperX<MaterialCategory> {

    default boolean existsCode(Long id, String mergeCode) {
        return exists(new LambdaQueryWrapperX<MaterialCategory>()
                .eq(MaterialCategory::getMergeCode, mergeCode)
                .neIfPresent(MaterialCategory::getId, id)
                .last("limit 1"));
    }

    default void updateMaterial(MaterialCategoryUpdateDTO dto) {
        MaterialCategory category = new MaterialCategory();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setCode(dto.getCode());
        updateById(category);
    }

    default Boolean existedChildCategory(Long parentId) {
        return exists(new LambdaQueryWrapperX<MaterialCategory>()
                .eq(MaterialCategory::getParentId, parentId));
    }

    default List<MaterialCategory> selectByIds(List<Long> materialCategoryIds) {
        return selectList(new LambdaQueryWrapperX<MaterialCategory>().in(MaterialCategory::getId, materialCategoryIds));
    }

    default List<MaterialCategory> selectByParentId(Long parentId){
        return selectList(new LambdaQueryWrapperX<MaterialCategory>()
                .eq(MaterialCategory::getParentId,parentId)
                .orderByAsc(MaterialCategory::getMergeCode));
    }

    List<Long> selectIdsByKeyWord(String keyword);

    default List<MaterialCategory> selectListByCategoryCodeList(List<String> categoryCode){
        return selectList(new LambdaQueryWrapperX<MaterialCategory>()
                .in(MaterialCategory::getMergeCode,categoryCode));
    }
}
