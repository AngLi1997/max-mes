package com.bmos.mes.service.plan.document.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.plan.document.controller.vo.TemplateCategoryTreeVO;
import com.bmos.mes.service.plan.document.model.BatchTemplateCategory;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategorySaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface BatchTemplateCategoryConverter {
    
    BatchTemplateCategoryConverter INSTANCE = Mappers.getMapper(BatchTemplateCategoryConverter.class);

    BatchTemplateCategory convert2DO(TemplateCategorySaveDTO dto);

   default List<TemplateCategoryTreeVO> convert2TreeVO(List<BatchTemplateCategory> batchTemplateCategories){
       List<TemplateCategoryTreeVO> treeVOS = new ArrayList<>();
       if (CollUtil.isEmpty(batchTemplateCategories)){
           return treeVOS;
       }
       for (BatchTemplateCategory batchTemplateCategory : batchTemplateCategories) {
           TemplateCategoryTreeVO treeVO = new TemplateCategoryTreeVO();
           treeVO.setId(batchTemplateCategory.getId());
           treeVO.setName(batchTemplateCategory.getName());
           treeVO.setParentId(batchTemplateCategory.getParentId());
           treeVO.setCreateTime(batchTemplateCategory.getCreateTime());
           treeVOS.add(treeVO);
       }
       return treeVOS;
   }
}
