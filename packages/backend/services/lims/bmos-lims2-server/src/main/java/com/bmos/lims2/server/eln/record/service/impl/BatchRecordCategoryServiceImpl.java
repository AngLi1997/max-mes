package com.bmos.lims2.server.eln.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.convert.BatchRecordCategoryConvert;
// 产品绑定转换已废弃
import com.bmos.lims2.server.eln.record.convert.BatchRecordConvert;
import com.bmos.lims2.server.eln.record.dto.CategorySaveDTO;
import com.bmos.lims2.server.eln.record.dto.CategoryUpdateDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecord;
import com.bmos.lims2.server.eln.record.entity.BatchRecordCategory;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordCategoryMapper;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordMapper;
import com.bmos.lims2.server.eln.record.service.BatchRecordCategoryService;
import com.bmos.lims2.server.eln.record.vo.CategoryListVO;
import com.bmos.lims2.server.eln.record.vo.ProductRecordTreeVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        checkNameDuplicate(dto.getName(), null);
        Integer sort = ObjectUtil.isNotEmpty(list) ? CollectionUtil.getFirst(list).getSort() +
                RecordConstant.ONE : RecordConstant.ONE;
        dto.setSort(sort);
        Long id = CustomIdGenerator.nextId();
        dto.setId(id);
        BatchRecordCategory firstCategory = CollectionUtils.findFirst(list, item -> item.getId().equals(dto.getParentId()));
        dto.setCode(ObjectUtil.isEmpty(dto.getParentId()) || ObjectUtil.isEmpty(firstCategory) ? String.valueOf(id) :
                firstCategory.getCode() + StrUtil.COMMA + id);
        return categoryMapper.insertCategory(BatchRecordCategoryConvert.INSTANCE.convertToCategory(dto));
    }

    @Override
    public Boolean updateCategory(CategoryUpdateDTO dto) {
        BatchRecordCategory batchRecordCategory = categoryMapper.queryById(dto.getId());
        if (ObjectUtil.isEmpty(batchRecordCategory)) {
            throw new BmosException(LimsResponseCode.RECORD_CATEGORY_UPDATE_ERROR);
        }
        checkNameDuplicate(dto.getName(), batchRecordCategory.getId());
        batchRecordCategory.setName(dto.getName());
        return categoryMapper.updateCategory(batchRecordCategory);
    }

    @Override
    public Boolean deleteCategory(String id) {
        List<BatchRecord> recordList = recordMapper.getRecordList(id);
        List<BatchRecordCategory> list = categoryMapper.queryByParentId(id);
        if (ObjectUtil.isNotEmpty(recordList) || ObjectUtil.isNotEmpty(list)) {
            throw new BmosException(LimsResponseCode.RECORD_BOUND_CATEGORY);
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
        List<CategoryListVO> categoryList = listAllCategory();
        List<ProductRecordTreeVO> records = BatchRecordConvert.INSTANCE.convertToProductRecordTreeVO(batchRecords);
        records.forEach(record -> {
            record.setParentId(record.getCategoryId());
        });
        List<ProductRecordTreeVO> treeList = BatchRecordCategoryConvert.INSTANCE.convertToTreeVo(categoryList);
        treeList.addAll(records);
        List<ProductRecordTreeVO> tree = TreeUtil.buildTree(treeList, false);
        return removeEmptyCategoryNode(tree);
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

    /**
     * 递归移除无子节点的分类节点（仅保留含记录或含有效子分类的节点）
     * @param tree 树结构
     * @return 过滤后的树结构
     */
    private List<ProductRecordTreeVO> removeEmptyCategoryNode(List<ProductRecordTreeVO> tree) {
        if (CollUtil.isEmpty(tree)) {
            return Collections.emptyList();
        }
        return tree.stream()
                .map(node -> {
                    if (CollUtil.isNotEmpty(node.getChildren())) {
                        node.setChildren(removeEmptyCategoryNode(node.getChildren()));
                    }
                    return node;
                })
                .filter(node -> ObjectUtil.isNotNull(node.getCategoryId()) || CollUtil.isNotEmpty(node.getChildren()))
                .collect(Collectors.toList());
    }

    private void checkNameDuplicate(String name, Long excludeId) {
        if (categoryMapper.existsByName(name, excludeId)) {
            throw new BmosException(LimsResponseCode.RECORD_CATEGORY_NAME_ERROR);
        }
    }

}
