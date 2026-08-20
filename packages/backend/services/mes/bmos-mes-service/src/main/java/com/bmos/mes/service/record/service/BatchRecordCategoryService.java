package com.bmos.mes.service.record.service;

import com.bmos.mes.service.record.dto.CategorySaveDTO;
import com.bmos.mes.service.record.dto.CategoryUpdateDTO;
import com.bmos.mes.service.record.model.BatchRecordCategory;
import com.bmos.mes.service.record.vo.CategoryListVO;
import com.bmos.mes.service.record.vo.ProductRecordTreeVO;

import java.util.List;

public interface BatchRecordCategoryService {

    Boolean saveCategory(CategorySaveDTO dto);

    Boolean updateCategory(CategoryUpdateDTO dto);

    Boolean deleteCategory(String id);

    List<CategoryListVO> listCategory();

    List<BatchRecordCategory> selectCategory();

    List<ProductRecordTreeVO> listRecordTree();

    List<Long> selectCategoryList(Long categoryId);
}
