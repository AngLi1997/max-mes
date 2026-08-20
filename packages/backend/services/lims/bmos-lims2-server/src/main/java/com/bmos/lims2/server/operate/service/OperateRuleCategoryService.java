package com.bmos.lims2.server.operate.service;


import com.bmos.lims2.server.operate.dto.SaveCategoryDTO;
import com.bmos.lims2.server.operate.dto.UpdateCategoryDTO;
import com.bmos.lims2.server.operate.vo.OperateRuleCategoryVO;
import com.bmos.lims2.server.operate.vo.OperateRuleSopVO;

import java.util.List;

/**
 * @author renjinguang
 */
public interface OperateRuleCategoryService {

    List<OperateRuleCategoryVO> getListCategory();

    void saveCategory(SaveCategoryDTO dto);

    void updateCategory(UpdateCategoryDTO dto);

    void deleteCategory(Long id);

    List<OperateRuleSopVO> listSop();
}
