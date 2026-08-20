package com.bmos.mes.service.plan.document.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.document.controller.vo.TemplateCategoryTreeVO;
import com.bmos.mes.service.plan.document.convert.BatchTemplateCategoryConverter;
import com.bmos.mes.service.plan.document.mapper.BatchTemplateCategoryMapper;
import com.bmos.mes.service.plan.document.mapper.BatchTemplateInfoMapper;
import com.bmos.mes.service.plan.document.model.BatchTemplateCategory;
import com.bmos.mes.service.plan.document.service.BatchTemplateCategoryService;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategorySaveDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategoryUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 批记录模板分类
 */
@Service
public class BatchTemplateCategoryServiceImpl implements BatchTemplateCategoryService {

    @Autowired
    private BatchTemplateCategoryMapper batchTemplateCategoryMapper;
    @Autowired
    private BatchTemplateInfoMapper batchTemplateInfoMapper;


    @Override
    public void saveCategory(TemplateCategorySaveDTO dto) {
        if (Objects.isNull(dto.getParentId())){
            dto.setParentId(TreeUtil.parentId);
        }
        // 分类名称不能相同
        BatchTemplateCategory batchTemplateCategory = batchTemplateCategoryMapper.selectByName(dto.getName());
        if (Objects.nonNull(batchTemplateCategory)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_CATEGORY_NAME_EXIST, dto.getName());
        }
        batchTemplateCategoryMapper.insert(BatchTemplateCategoryConverter.INSTANCE.convert2DO(dto));
    }

    @Override
    public void updateCategory(TemplateCategoryUpdateDTO dto) {
        BatchTemplateCategory batchTemplateCategory = batchTemplateCategoryMapper.selectById(dto.getId());
        if (Objects.isNull(batchTemplateCategory)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_CATEGORY_NOT_EXIST);
        }
        BatchTemplateCategory curBatchTemplateCategory = batchTemplateCategoryMapper.selectByName(dto.getName());
        if (Objects.nonNull(curBatchTemplateCategory) && !Objects.equals(curBatchTemplateCategory.getId(), dto.getId())){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_CATEGORY_NAME_EXIST, dto.getName());
        }
        batchTemplateCategory.setName(dto.getName());
        batchTemplateCategoryMapper.updateById(batchTemplateCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        // 当前分类下若有子分类则不能删除
        if (batchTemplateCategoryMapper.existsByParentId(id)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_CATEGORY_HAS_CHILD);
        }
        // 当前分类下若有模板则不能删除
        if (batchTemplateInfoMapper.existByCategoryId(id)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_CATEGORY_HAS_TEMPLATE);
        }
        batchTemplateCategoryMapper.deleteById(id);
    }

    @Override
    public List<TemplateCategoryTreeVO> categoryTree() {
        List<BatchTemplateCategory> batchTemplateCategories = batchTemplateCategoryMapper.selectAll();
        List<TemplateCategoryTreeVO> templateCategoryTreeVOS = BatchTemplateCategoryConverter.INSTANCE.convert2TreeVO(batchTemplateCategories);
        return TreeUtil.buildTree(templateCategoryTreeVOS, true);
    }

}
