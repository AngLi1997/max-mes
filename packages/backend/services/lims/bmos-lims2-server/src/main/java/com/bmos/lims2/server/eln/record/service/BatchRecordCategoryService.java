package com.bmos.lims2.server.eln.record.service;


import com.bmos.lims2.server.eln.record.dto.CategorySaveDTO;
import com.bmos.lims2.server.eln.record.dto.CategoryUpdateDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordCategory;
import com.bmos.lims2.server.eln.record.vo.CategoryListVO;
import com.bmos.lims2.server.eln.record.vo.ProductRecordTreeVO;

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
