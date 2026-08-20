package com.bmos.lims2.server.material.service;




import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.platform.material.dto.CategoryIssueDTO;

import java.util.List;

/**
 * 缉拿品分类业务接口
 */
public interface MaterialCategoryService {

    /**
     * 保存分类信息
     *
     * @param reqVO
     */
    void saveCategory(MaterialCategoryCreateDTO reqVO);

    /**
     * 删除分类信息
     *
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 更新分类信息
     *
     * @param reqVO
     */
    void updateCategory(MaterialCategoryUpdateDTO reqVO);

    /**
     * 查询当前已同步到当前系统的分类树
     *
     * @param reqVO
     * @return
     */
    List<MaterialCategoryTreeNodeDTO> queryCategoryTree(MaterialCategoryTreeQueryDTO reqVO);

    /**
     * 获取同步分类物料树（包含物料）
     *
     * @param reqVO
     * @return
     */
    List<MaterialCategoryTreeNodeDTO> getSyncTree(MaterialCategoryTreeQueryDTO reqVO);

    /**
     * 获取需要同步到当前系统的所有分类树 （筛除掉已同步到当前系统的分类树）
     *
     * @return
     */
    List<MaterialCategoryTreeNodeDTO> getSyncTreeAll();

    /**
     * 同步平台的物料分类到系统中
     *
     * @param categoryList
     * @param categoryType
     */
    void issueCategory(List<CategoryIssueDTO> categoryList, Integer categoryType);

    /**
     * 根据平台物料id以及业务分类查询同步到本系统的分类信息
     *
     * @param platformCategorieList
     * @return
     */
    List<MaterialCategoryDTO> selectByPlatformCategories(List<Long> platformCategorieList, Integer categoryType);

    /**
     * 查询当前分类下的所有子分类
     *
     * @param categoryId
     * @return
     */
    List<Long> selectChildCategoryId(Long categoryId);

    /**
     * 根据id查询分类
     *
     * @param categoryId
     * @return
     */
    MaterialCategoryDTO getById(Long categoryId);

    /**
     * 获取所有检品分类
     *
     * @return
     */
    List<MaterialCategoryDTO> getAllCategory();
}
