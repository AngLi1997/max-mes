package com.bmos.platform.service.equipment.service;

import com.bmos.platform.service.equipment.controller.vo.CategoryTreeNodeVO;
import com.bmos.platform.service.equipment.controller.vo.CategoryVO;
import com.bmos.platform.service.equipment.service.dto.CategorySaveDTO;
import com.bmos.platform.service.equipment.service.dto.CategoryUpdateDTO;

import java.util.List;

public interface EquipmentCategoryService {

    /**
     * 保存设备分类
     * @param dto
     */
    void saveCategory(CategorySaveDTO dto);

    /**
     * 修改设备分类
     * @param dto
     */
    void updateCategory(CategoryUpdateDTO dto);

    /**
     * 删除设备分类
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 获取设备分类树
     * @return
     */
    List<CategoryTreeNodeVO> getCategoryTree();

    /**
     * 获取设备分类基础信息
     * @param id
     * @return
     */
    CategoryVO getCategoryTreeInfo(Long id);
}
