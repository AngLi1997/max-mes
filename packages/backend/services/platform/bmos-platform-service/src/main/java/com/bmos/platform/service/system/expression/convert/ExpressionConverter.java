package com.bmos.platform.service.system.expression.convert;

import com.bmos.platform.service.system.expression.dto.ExpressionCategorySaveDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionCategoryUpdateDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionSaveDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionUpdateDTO;
import com.bmos.platform.service.system.expression.model.Expression;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import com.bmos.platform.service.system.expression.vo.ExpressionCategoryTreeNodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ExpressionConverter {
    ExpressionConverter INSTANCE = Mappers.getMapper(ExpressionConverter.class);
    List<ExpressionCategoryTreeNodeVO> convertCategoryTreeNode(List<ExpressionCategory> categories);

    ExpressionCategory convetDO(ExpressionCategorySaveDTO dto);

    ExpressionCategory convetDO(ExpressionCategoryUpdateDTO dto);

    Expression convetDO(ExpressionSaveDTO dto);

    Expression convetDO(ExpressionUpdateDTO dto);
}
