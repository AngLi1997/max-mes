package com.bmos.mes.service.operate.service;

import com.bmos.mes.service.operate.dto.SaveCategoryDTO;
import com.bmos.mes.service.operate.dto.UpdateCategoryDTO;
import com.bmos.mes.service.operate.vo.OperateRuleCategoryVO;
import com.bmos.mes.service.operate.vo.OperateRuleSopVO;

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
