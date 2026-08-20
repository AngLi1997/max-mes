package com.bmos.platform.service.system.expression.service;

import com.bmos.common.exception.BmosException;
import com.bmos.platform.service.system.expression.dto.ExpressionCategorySaveDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionCategoryUpdateDTO;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import com.bmos.platform.service.system.expression.vo.ExpressionCategoryTreeNodeVO;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;

import java.util.List;

public interface ExpressionCategoryService {
    /**
     * 物料分类树
     *
     * @return List<ExpressionCategoryTreeNodeVO>
     */
    List<ExpressionCategoryTreeNodeVO> getCategoryTree();

    List<ExpressionCategory> selectChildrenById(Long id);

    /**
     * 分类保存
     *
     * @param dto dto
     */
    void save(ExpressionCategorySaveDTO dto);

    /**
     * 校验分类id是否存在
     * @param id id
     * @throws BmosException
     */
    void existsCategory(Long id);

    /**
     * 分类更新
     *
     * @param dto dto
     */
    void update(ExpressionCategoryUpdateDTO dto);

    /**
     * 分类删除
     *
     * @param id id
     */
    void delete(Long id);

    List<ExpressionTreeNodeVO> getFullTreeNodeVOList();
}
