package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.equipment.controller.vo.CategoryTreeNodeVO;
import com.bmos.platform.service.equipment.controller.vo.CategoryVO;
import com.bmos.platform.service.equipment.convert.EquipmentCategoryConvert;
import com.bmos.platform.service.equipment.mapper.EquipmentCategoryMapper;
import com.bmos.platform.service.equipment.model.EquipmentCategory;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.service.EquipmentCategoryService;
import com.bmos.platform.service.equipment.service.EquipmentInfoService;
import com.bmos.platform.service.equipment.service.dto.CategorySaveDTO;
import com.bmos.platform.service.equipment.service.dto.CategoryUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class EquipmentCategoryServiceImpl implements EquipmentCategoryService {

    @Autowired
    private EquipmentCategoryMapper categoryMapper;

    @Autowired
    private EquipmentInfoService infoService;

    @Override
    public void saveCategory(CategorySaveDTO dto) {
        List<EquipmentCategory> list = categoryMapper.selectCategoryList();
        if (CollUtil.isNotEmpty(list) &&
                CollectionUtils.convertList(list, EquipmentCategory::getCode).contains(dto.getCode())) {
            throw new BmosException(PlatformResponseCode.FACTORY_SAVE_ERROR);
        }
        Long id = IdUtils.getSnowflake();
        EquipmentCategory equipmentCategory = EquipmentCategoryConvert.INSTANCE.convertToCategory(dto);
        equipmentCategory.setId(id);
        equipmentCategory.setTreeCode(ObjectUtil.isEmpty(dto.getParentId()) ? String.valueOf(id) : list.stream().filter(item ->
                item.getId().equals(dto.getParentId())).findFirst().get().getTreeCode() + StrUtil.COMMA + id);
        categoryMapper.saveOrUpdateCategory(equipmentCategory);
    }

    @Override
    public void updateCategory(CategoryUpdateDTO dto) {
        EquipmentCategory equipmentCategory = categoryMapper.selectById(dto.getId());
        if (Objects.isNull(equipmentCategory)){
            throw new BmosException(PlatformResponseCode.EQUIPMENT_NOT_EXIST);
        }
        equipmentCategory.setName(dto.getName());
        categoryMapper.saveOrUpdateCategory(equipmentCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        List<EquipmentInfo> equipmentInfoList = infoService.queryInfoListByCategoryIdAndEnable(id, Boolean.TRUE);
        if (CollUtil.isNotEmpty(equipmentInfoList)){
            throw new BmosException(PlatformResponseCode.FACTORY_DELETE_ERROR);
        }
        categoryMapper.deleteCategoryById(id);
    }

    @Override
    public List<CategoryTreeNodeVO> getCategoryTree() {
        List<EquipmentCategory> equipmentCategories = categoryMapper.selectCategoryList();
        if (CollUtil.isEmpty(equipmentCategories)){
            return Collections.emptyList();
        }
        return TreeUtil.buildTree(EquipmentCategoryConvert.INSTANCE.convertToTreeVo(equipmentCategories),false);
    }

    @Override
    public CategoryVO getCategoryTreeInfo(Long id) {
        return null;
    }
}
