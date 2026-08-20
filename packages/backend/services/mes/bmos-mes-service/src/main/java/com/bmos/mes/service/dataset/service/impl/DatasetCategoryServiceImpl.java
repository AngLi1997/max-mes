package com.bmos.mes.service.dataset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.dataset.convert.DatasetCategoryConvert;
import com.bmos.mes.service.dataset.dto.DatasetCategoryCreateDTO;
import com.bmos.mes.service.dataset.dto.DatasetCategoryEditDTO;
import com.bmos.mes.service.dataset.mapper.IDatasetCategoryMapper;
import com.bmos.mes.service.dataset.mapper.IDatasetMapper;
import com.bmos.mes.service.dataset.model.DatasetCategory;
import com.bmos.mes.service.dataset.service.IDatasetCategoryService;
import com.bmos.mes.service.dataset.vo.DatasetCategoryVO;
import com.bmos.mes.service.weigh.centre.config.util.BmosTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 数据集分类 service impl
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 11:51
 */
@Service
@Slf4j
public class DatasetCategoryServiceImpl implements IDatasetCategoryService {

    private static final String LOG_PREFIX = "[数据集分类]";

    @Resource
    private IDatasetCategoryMapper datasetCategoryMapper;

    @Resource
    private IDatasetMapper datasetMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(DatasetCategoryCreateDTO dto) {
        log.info("{}创建数据集分类:{}", LOG_PREFIX, dto);
        String parentIdPath = null;
        if (dto.getParentId() != null){
            DatasetCategory parent = datasetCategoryMapper.selectById(dto.getParentId());
            if (parent == null){
                throw new BmosException(MesResponseCode.DATASET_CATEGORY_NOT_EXIST);
            }
            parentIdPath = parent.getIdPath();
        }
        DatasetCategory datasetCategory = new DatasetCategory();
        datasetCategory.setParentId(dto.getParentId());
        datasetCategory.setName(dto.getName());
        List<DatasetCategory> datasetCategories = datasetCategoryMapper.listByNameAndParentId(dto.getParentId(), dto.getName());
        if (CollectionUtil.isNotEmpty(datasetCategories)){
            // 同分类下存在相同名称
            throw new BmosException(MesResponseCode.DATASET_CATEGORY_NAME_EXIST);
        }
        datasetCategoryMapper.insert(datasetCategory);
        // 更新idPath
        datasetCategory.setIdPath(parentIdPath == null ? datasetCategory.getId().toString() : parentIdPath + "," + datasetCategory.getId());
        datasetCategoryMapper.updateById(datasetCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCategory(DatasetCategoryEditDTO dto) {
        log.info("{}编辑数据集分类:{}", LOG_PREFIX, dto);
        DatasetCategory exist = datasetCategoryMapper.selectById(dto.getId());
        if (exist == null){
            throw new BmosException(MesResponseCode.DATASET_CATEGORY_NOT_EXIST);
        }
        if (Objects.equals(dto.getName(), exist.getName())){
            return;
        }
        List<DatasetCategory> datasetCategories = datasetCategoryMapper.listByNameAndParentId(exist.getParentId(), dto.getName());
        if (CollectionUtil.isNotEmpty(datasetCategories)){
            // 同分类下存在相同名称
            throw new BmosException(MesResponseCode.DATASET_CATEGORY_NAME_EXIST);
        }
        exist.setName(dto.getName());
        datasetCategoryMapper.updateById(exist);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        log.info("{}删除数据集分类:{}", LOG_PREFIX, id);
        DatasetCategory exist = datasetCategoryMapper.selectById(id);
        if (exist == null){
            throw new BmosException(MesResponseCode.DATASET_CATEGORY_NOT_EXIST);
        }
        List<DatasetCategory> datasetCategories = datasetCategoryMapper.listByParentId(id);
        if (CollectionUtil.isNotEmpty(datasetCategories)){
            // 提示该分类下存在子级，无法删除
            throw new BmosException(MesResponseCode.DATASET_CATEGORY_NOT_ALLOWED_DELETED_WITH_CHILDREN);
        }
        if (CollectionUtil.isNotEmpty(datasetMapper.listByCategoryId(id))){
            // 提示分类下存在数据集，无法删除
            throw new BmosException(MesResponseCode.DATASET_CATEGORY_NOT_ALLOWED_DELETED_WITH_CENTRE);
        }
        datasetCategoryMapper.deleteById(id);
    }

    @Override
    public List<DatasetCategoryVO> queryCategoryTree() {
        List<DatasetCategory> datasetCategories = datasetCategoryMapper.selectList();
        List<DatasetCategoryVO> list = DatasetCategoryConvert.INSTANCE.convertToVO(datasetCategories);
        BmosTreeUtil.buildTree(list, null);
        return list;
    }
}
