package com.bmos.platform.service.material.service;

import com.bmos.platform.service.material.dto.MaterialCategorySaveDTO;
import com.bmos.platform.service.material.dto.MaterialCategoryUpdateDTO;
import com.bmos.platform.service.material.dto.UnregisterMaterialCategoryDTO;
import com.bmos.platform.service.material.model.MaterialCategory;
import com.bmos.platform.service.material.vo.IssueTreeNodeVO;
import com.bmos.platform.service.material.vo.MaterialCategoryTreeNodeVO;

import java.util.List;

public interface MaterialCategoryService {
    Long save(MaterialCategorySaveDTO dto);

    List<MaterialCategoryTreeNodeVO> getCategoryTree();

    MaterialCategory getById(Long materialCategoryId);

    void update(MaterialCategoryUpdateDTO dto);

    void deleteById(Long id);

    Boolean checkCategoryExisted(String code, Long id);

    Boolean existedChildCategory(Long parentId);

    List<MaterialCategory> selectByIds(List<Long> materialCategoryIds);

    List<IssueTreeNodeVO> selectByParentId(Long parentId);

    MaterialCategory selectById(Long id);

    List<MaterialCategory> selectList();

    List<Long> getAllChildCategoryIds(Long parentId);

    void updateBatch(List<MaterialCategory> materialCategories);

    void unregisterCategory(UnregisterMaterialCategoryDTO dto);

    List<Long> selectIdsByKeyWord(String keyword);

    List<MaterialCategory> selectListByCategoryCodeList(List<String> categoryCode);
}
