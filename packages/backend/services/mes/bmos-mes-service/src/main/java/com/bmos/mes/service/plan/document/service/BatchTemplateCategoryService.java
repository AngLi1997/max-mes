package com.bmos.mes.service.plan.document.service;

import com.bmos.mes.service.plan.document.controller.vo.TemplateCategoryTreeVO;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategorySaveDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategoryUpdateDTO;

import java.util.List;

/**
 * 批记录模板分类
 */
public interface BatchTemplateCategoryService {

    /**
     * 新增分类
     * @param dto
     */
    void saveCategory(TemplateCategorySaveDTO dto);

    /**
     * 修改分类
     * @param dto
     */
    void updateCategory(TemplateCategoryUpdateDTO dto);

    /**
     * 删除分类
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 查询分类树
     * @return
     */
    List<TemplateCategoryTreeVO> categoryTree();

}
