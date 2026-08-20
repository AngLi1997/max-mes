package com.bmos.mes.service.product.service;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.product.dto.CategoryIssueDTO;
import com.bmos.mes.service.product.dto.ProductMaterialCategoryQueryDTO;
import com.bmos.mes.service.product.dto.ProductMaterialCategorySaveDTO;
import com.bmos.mes.service.product.dto.ProductMaterialCategoryUpdateDTO;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.product.vo.ProductMaterialCategoryTreeNodeVO;

import java.util.List;

public interface ProductMaterialCategoryService {
    void save(ProductMaterialCategorySaveDTO dto);

    void delete(Long id);

    void update(ProductMaterialCategoryUpdateDTO dto);

    List<ProductMaterialCategoryTreeNodeVO> queryCategoryTree(ProductMaterialCategoryQueryDTO queryDto);
    void issueCategory(List<CategoryIssueDTO> categoryList, List<Integer> business);

    List<ProductMaterialCategory> listAll();

    List<ProductMaterialCategory> selectListByType(Integer business);

    List<ProductMaterialCategory> selectList();

    ProductMaterialCategory selectById(Long materialCategoryId);

    List<Long> getAllChildCategory(Long parentId);

    /**
     * 根据分类父级id查询所有物料信息
     * @param parentCategoryId 分类父级id
     * @return
     */
    List<Long> getAllProductIds(Long parentCategoryId);

    List<ProductMaterialCategory> selectListByTypes(List<Integer> types);

    List<Long> getAllChildCategory(CategoryInfoTypeEnum categoryInfoType, Long categoryId);

    List<Long> getIdListByCategoryIdList(List<Long> categoryIdList);
}
