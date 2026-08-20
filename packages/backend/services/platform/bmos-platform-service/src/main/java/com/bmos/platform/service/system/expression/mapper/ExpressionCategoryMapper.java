package com.bmos.platform.service.system.expression.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Objects;

@Mapper
public interface ExpressionCategoryMapper extends BaseMapperX<ExpressionCategory> {
    List<ExpressionCategory> selectChildrenById(@Param("id") Long id);

    default boolean existsNameInParentId(Long id, Long parentId, String name) {
        return exists(new LambdaQueryWrapperX<ExpressionCategory>()
            .eq(ExpressionCategory::getParentId, parentId)
            .eq(ExpressionCategory::getName, name)
            .neIfPresent(ExpressionCategory::getId, id)
            .last("limit 1"));
    }

    default boolean existsId(Long id) {
        return exists(new LambdaQueryWrapperX<ExpressionCategory>()
            .eq(ExpressionCategory::getId, id)
            .last("limit 1"));
    }

    default void updateExpressionCategory(ExpressionCategory dto) {
        update(null,
            new LambdaUpdateWrapper<ExpressionCategory>()
                .set(ExpressionCategory::getName, dto.getName())
                .set(ExpressionCategory::getParentId, Objects.isNull(dto.getParentId()) ? 0L : dto.getParentId())
                .eq(ExpressionCategory::getId, dto.getId())
        );
    }

    default boolean existsRelationParentId(Long parentId) {
        return exists(new LambdaQueryWrapperX<ExpressionCategory>()
            .eq(ExpressionCategory::getParentId, parentId)
            .last("limit 1"));
    }

    void delete(@Param("id") Long id, @Param("userId") String userId);

    List<ExpressionTreeNodeVO> selectFullTreeNodeVOList();
}
