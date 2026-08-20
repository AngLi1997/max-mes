package com.bmos.platform.service.system.expression.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.enums.expression.ExpressionStatusEnum;
import com.bmos.platform.service.system.expression.dto.ExpressionPageDTO;
import com.bmos.platform.service.system.expression.model.Expression;
import com.bmos.platform.service.system.expression.vo.ExpressionPageVO;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExpressionMapper extends BaseMapperX<Expression> {
    /**
     * 分页查询
     *
     * @param dto dto
     * @return List<ExpressionPageVO>
     */
    List<ExpressionPageVO> page(ExpressionPageDTO dto);

    List<ExpressionPageVO> list();

    default boolean existsName(Long id, String name) {
        return exists(new LambdaQueryWrapperX<Expression>()
            .eq(Expression::getName, name)
            .neIfPresent(Expression::getId, id)
            .last("limit 1"));
    }

    default boolean existsCategoryId(Long categoryId) {
        return exists(new LambdaQueryWrapperX<Expression>()
            .eq(Expression::getExpressionCategoryId, categoryId)
            .last("limit 1"));
    }

    default void confirm(Long id) {
        updateById(Expression.builder()
            .confirmStatus(ExpressionStatusEnum.CONFIRMED.getValue())
            .id(id)
            .build()
        );
    }

    void delete(@Param("id") Long id, @Param("userId") String userId);

    List<ExpressionTreeNodeVO> selectFullTreeNodeVOList();

    default void verify(Long id){
        updateById(Expression.builder()
                .confirmStatus(ExpressionStatusEnum.VERIFIED.getValue())
                .id(id)
                .build()
        );
    }
}
