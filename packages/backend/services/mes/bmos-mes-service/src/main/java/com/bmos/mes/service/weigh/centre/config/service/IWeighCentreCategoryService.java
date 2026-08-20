package com.bmos.mes.service.weigh.centre.config.service;

import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCategoryCreateDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCategoryEditDTO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryVO;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:36
 */
public interface IWeighCentreCategoryService {

    /**
     * 查询全量称量中心分类树
     * @return
     */
    List<WeighCentreCategoryVO> categoryTree();

    /**
     * 创建称量中心分类
     * @param createDTO dto
     */
    void createCategory(WeighCentreCategoryCreateDTO createDTO);

    /**
     * 编辑称量中心分类
     * @param editDTO dto
     */
    void editCategory(WeighCentreCategoryEditDTO editDTO);

    /**
     * 删除称量中心分类
     * @param id 称量中心分类
     */
    void deleteCategory(Long id);
}
