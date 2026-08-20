package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.record.convert.BatchRecordCategoryConvert;
import com.bmos.mes.service.record.convert.BatchRecordProductConvert;
import com.bmos.mes.service.record.dto.CategorySaveDTO;
import com.bmos.mes.service.record.dto.CategoryUpdateDTO;
import com.bmos.mes.service.record.mapper.BatchRecordCategoryMapper;
import com.bmos.mes.service.record.mapper.BatchRecordMapper;
import com.bmos.mes.service.record.model.BatchRecord;
import com.bmos.mes.service.record.model.BatchRecordCategory;
import com.bmos.mes.service.record.service.BatchRecordCategoryService;
import com.bmos.mes.service.record.vo.CategoryListVO;
import com.bmos.mes.service.record.vo.ProductRecordTreeVO;
import com.bmos.mybatis.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchRecordCategoryServiceImpl implements BatchRecordCategoryService {

    @Autowired
    private BatchRecordCategoryMapper categoryMapper;

    @Autowired
    private BatchRecordMapper recordMapper;


    @Override
    @OperationLog
    public Boolean saveCategory(CategorySaveDTO dto) {
        List<BatchRecordCategory> list = categoryMapper.list();
        Integer sort = ObjectUtil.isNotEmpty(list) ? CollectionUtil.getFirst(list).getSort() +
                RecordConstant.ONE : RecordConstant.ONE;
        dto.setSort(sort);
        Long id = CustomIdGenerator.nextId();
        dto.setId(id);
        BatchRecordCategory firstCategory = CollectionUtils.findFirst(list, item -> item.getId().equals(dto.getParentId()));
        dto.setCode(ObjectUtil.isEmpty(dto.getParentId()) || ObjectUtil.isEmpty(firstCategory) ? String.valueOf(id) :
                firstCategory.getCode() + StrUtil.COMMA + id);
        List<String> categoryNameList = CollectionUtils.convertList(list, BatchRecordCategory::getName,
                category -> category.getParentId().equals(ObjectUtil.isNull(dto.getParentId()) ? 0 : dto.getParentId()));
        if (ObjectUtil.isNotEmpty(categoryNameList) && categoryNameList.contains(dto.getName())){
            throw new BmosException(MesResponseCode.RECORD_CATEGORY_NAME_ERROR);
        }
        return categoryMapper.insertCategory(BatchRecordCategoryConvert.INSTANCE.convertToCategory(dto));
    }

    @Override
    public Boolean updateCategory(CategoryUpdateDTO dto) {
        BatchRecordCategory batchRecordCategory = categoryMapper.queryById(dto.getId());
        if (ObjectUtil.isEmpty(batchRecordCategory)) {
            throw new BmosException(MesResponseCode.RECORD_CATEGORY_UPDATE_ERROR);
        }
        batchRecordCategory.setName(dto.getName());
        return categoryMapper.updateCategory(batchRecordCategory);
    }

    @Override
    public Boolean deleteCategory(String id) {
        List<BatchRecord> recordList = recordMapper.getRecordList(id);
        List<BatchRecordCategory> list = categoryMapper.queryByParentId(id);
        if (ObjectUtil.isNotEmpty(recordList) || ObjectUtil.isNotEmpty(list)) {
            throw new BmosException(MesResponseCode.RECORD_BOUND_CATEGORY);
        }
        categoryMapper.deleteCategory(id, SysUserHolder.getUser().getUserId());
        return Boolean.TRUE;
    }

    @Override
    public List<CategoryListVO> listCategory() {
        List<BatchRecordCategory> list = categoryMapper.listCategory();
        List<CategoryListVO> vos = BatchRecordCategoryConvert.INSTANCE.convertToListCategory(list);
        List<CategoryListVO> newList = vos.stream().filter(parentNode -> parentNode.getParentId().equals(RecordConstant.PARENT_ID))
                .map(node -> getChildren(node, vos)).collect(Collectors.toList());
        return newList;
    }

    @Override
    public List<BatchRecordCategory> selectCategory() {
        return categoryMapper.list();
    }

    @Override
    public List<ProductRecordTreeVO> listRecordTree() {
        List<BatchRecord> batchRecords = recordMapper.selectList();
        if (CollUtil.isEmpty(batchRecords)) {
            return Collections.emptyList();
        }
        List<ProductRecordTreeVO> records = BatchRecordProductConvert.INSTANCE.convertToProductRecordTreeVO(batchRecords);
        records.forEach(record -> {
            record.setParentId(record.getCategoryId());
        });
        List<ProductRecordTreeVO> treeList = BatchRecordCategoryConvert.INSTANCE.convertToTreeVo(listAllCategory());
        treeList.addAll(records);
        return TreeUtil.buildTree(treeList, false);
    }

    @Override
    public List<Long> selectCategoryList(Long categoryId) {
        List<BatchRecordCategory> categories = categoryMapper.list();
        if (CollUtil.isEmpty(categories)) {
            return Collections.emptyList();
        }
        List<BatchRecordCategory> categoryList = CollectionUtils.filterList(categories,
                item -> item.getCode().contains(String.valueOf(categoryId)));
        return CollectionUtils.convertList(categoryList,BatchRecordCategory::getId);
    }

    public List<CategoryListVO> listAllCategory() {
        List<BatchRecordCategory> list = categoryMapper.listCategory();
        List<CategoryListVO> vos = BatchRecordCategoryConvert.INSTANCE.convertToListCategory(list);
        return vos;
    }

    public CategoryListVO getChildren(CategoryListVO vo, List<CategoryListVO> allList) {
        List<CategoryListVO> treeList = allList.stream()
                .filter(subNode -> vo.getId().equals(subNode.getParentId()))
                .map(subNode -> getChildren(subNode, allList)).collect(Collectors.toList());
        vo.setItemList(treeList);
        return vo;
    }

}
