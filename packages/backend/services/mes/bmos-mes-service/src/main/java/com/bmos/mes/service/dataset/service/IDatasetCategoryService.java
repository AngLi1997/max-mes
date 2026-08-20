package com.bmos.mes.service.dataset.service;

import com.bmos.mes.service.dataset.dto.DatasetCategoryCreateDTO;
import com.bmos.mes.service.dataset.dto.DatasetCategoryEditDTO;
import com.bmos.mes.service.dataset.vo.DatasetCategoryVO;

import java.util.List;

/**
 * 数据集分类service
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 11:50
 */
public interface IDatasetCategoryService {

    /**
     * 创建数据集分类
     * @param dto
     */
    void createCategory(DatasetCategoryCreateDTO dto);

    /**
     * 编辑数据集分类
     * @param dto
     */
    void editCategory(DatasetCategoryEditDTO dto);

    /**
     * 删除数据集分类
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 查询数据集分类树
     * @return
     */
    List<DatasetCategoryVO> queryCategoryTree();
}
