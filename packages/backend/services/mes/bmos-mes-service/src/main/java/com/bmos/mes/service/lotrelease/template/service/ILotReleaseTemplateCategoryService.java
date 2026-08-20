package com.bmos.mes.service.lotrelease.template.service;

import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateCategoryCreateDTO;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateCategoryEditDTO;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateCategoryVO;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:29
 */
public interface ILotReleaseTemplateCategoryService {


    void createCategory(LotReleaseTemplateCategoryCreateDTO dto);


    void editCategory(LotReleaseTemplateCategoryEditDTO dto);


    void deleteCategory(Long id);


    List<LotReleaseTemplateCategoryVO> queryCategoryTree();
}
