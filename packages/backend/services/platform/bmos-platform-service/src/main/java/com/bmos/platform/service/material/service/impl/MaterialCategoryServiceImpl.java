package com.bmos.platform.service.material.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.config.swagger.BusinessesConfig;
import com.bmos.platform.service.material.convert.MaterialConverter;
import com.bmos.platform.service.material.dto.MaterialCategorySaveDTO;
import com.bmos.platform.service.material.dto.MaterialCategoryUpdateDTO;
import com.bmos.platform.service.material.dto.UnregisterMaterialCategoryDTO;
import com.bmos.platform.service.material.mapper.MaterialCategoryMapper;
import com.bmos.platform.service.material.mapper.MaterialMapper;
import com.bmos.platform.service.material.model.MaterialCategory;
import com.bmos.platform.service.material.service.MaterialCategoryService;
import com.bmos.platform.service.material.service.MaterialService;
import com.bmos.platform.service.material.vo.ChildBusinessVO;
import com.bmos.platform.service.material.vo.IssueBusinessVO;
import com.bmos.platform.service.material.vo.IssueTreeNodeVO;
import com.bmos.platform.service.material.vo.MaterialCategoryTreeNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class MaterialCategoryServiceImpl implements MaterialCategoryService {
    @Autowired
    private MaterialCategoryMapper materialCategoryMapper;

    @Autowired
    @Lazy
    private MaterialService materialService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BusinessesConfig businessesConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public Long save(MaterialCategorySaveDTO dto) {
        Long parentId = dto.getParentId();
        MaterialCategory parentCategory = materialCategoryMapper.selectById(dto.getParentId());
        boolean insertRoot = ObjectUtil.equal(parentId, TreeUtil.parentId);
        // 校验父级id是否存在 排除新增根级分类的情况
        if (!insertRoot && ObjectUtil.isNull(parentCategory)) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CATEGORY_ID_NOT_EXIST);
        }
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
//        String parentMergeCode = insertRoot ? StrUtil.EMPTY : parentCategory.getMergeCode();
//        String mergeCode = parentMergeCode + dto.getCode();
        String mergeCode = dto.getCode();
        if (materialService.checkMergeCodeExisted(mergeCode, null, null)){
            throw new BmosException(PlatformResponseCode.MERGE_CODE_EXISTED);
        }
        MaterialCategory materialCategory = MaterialConverter.INSTANCE.convertCategory(dto);
        materialCategory.setMergeCode(mergeCode);
        if (BooleanUtil.isTrue(dto.isBusinessRegister())) {
            materialCategory.setDispenseRecord(dto.getBusinessName());
        }
        materialCategoryMapper.insert(materialCategory);
        return materialCategory.getId();
    }

    @Override
    public List<MaterialCategoryTreeNodeVO> getCategoryTree() {
        List<MaterialCategory> categories = materialCategoryMapper.selectList();
        List<MaterialCategoryTreeNodeVO> nodes = MaterialConverter.INSTANCE.convertCategoryTreeNode(categories);
        nodes.forEach(node -> node.setShowName(node.getMergeCode() + StrUtil.DASHED + node.getName()));
        return TreeUtil.buildTree(nodes, false);
    }

    @Override
    public MaterialCategory getById(Long materialCategoryId) {
        return materialCategoryMapper.selectById(materialCategoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void update(MaterialCategoryUpdateDTO dto) {
        Long id = dto.getId();
        MaterialCategory dbCategory = materialCategoryMapper.selectById(id);
        if(StrUtil.isNotEmpty(dbCategory.getDispenseRecord())){
            throw new BmosException(PlatformResponseCode.CATEGORY_ISSUED_CANT_UPDATE, dbCategory.getDispenseRecord());
        }
        MaterialCategory parentCategory = materialCategoryMapper.selectById(dbCategory.getParentId());
        String parentMergeCode = ObjectUtil.isNull(parentCategory) ? StrUtil.EMPTY : parentCategory.getMergeCode();
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
//        String mergeCode = parentMergeCode + dto.getCode();
        String mergeCode = dto.getCode();
        if (materialService.checkMergeCodeExisted(mergeCode, null, dbCategory.getId())) {
            throw new BmosException(PlatformResponseCode.MERGE_CODE_EXISTED);
        }
        MaterialCategory materialCategory = MaterialConverter.INSTANCE.convertCategory(dto);
        materialCategory.setMergeCode(mergeCode);
        materialCategoryMapper.updateById(materialCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void deleteById(Long id) {
        if (materialCategoryMapper.existedChildCategory(id)) {
            throw new BmosException(PlatformResponseCode.CATEGORY_EXISTS_CHILD);
        }
        if (materialMapper.existsCategoryMaterial(id)) {
            throw new BmosException(PlatformResponseCode.CATEGORY_EXISTS_MATERIAL);
        }
        MaterialCategory materialCategory = materialCategoryMapper.selectById(id);
        if (StrUtil.isNotEmpty(materialCategory.getDispenseRecord())) {
            throw new BmosException(PlatformResponseCode.CATEGORY_ISSUED, materialCategory.getDispenseRecord());
        }
        materialCategoryMapper.deleteById(id);
    }

    @Override
    public Boolean checkCategoryExisted(String code, Long id) {
        return materialCategoryMapper.existsCode(id, code);
    }

    @Override
    public Boolean existedChildCategory(Long parentId) {
        return materialCategoryMapper.existedChildCategory(parentId);
    }

    @Override
    public List<MaterialCategory> selectByIds(List<Long> materialCategoryIds) {
        return materialCategoryMapper.selectByIds(materialCategoryIds);
    }

    @Override
    public List<IssueTreeNodeVO> selectByParentId(Long parentId) {
        List<MaterialCategory> materialCategories = materialCategoryMapper.selectByParentId(parentId);
        return MaterialConverter.INSTANCE.convertCategoryIssueTreeNode(materialCategories);
    }

    @Override
    public MaterialCategory selectById(Long id) {
        return materialCategoryMapper.selectById(id);
    }

    @Override
    public List<MaterialCategory> selectList() {
        return materialCategoryMapper.selectList();
    }

    @Override
    public List<Long> getAllChildCategoryIds(Long parentId) {
        if (ObjectUtil.isNull(parentId)) {
            return new ArrayList<>();
        }
        List<MaterialCategory> materialCategories = materialCategoryMapper.selectList();
        HashSet<Long> longs = new HashSet<>();
        longs.add(parentId);
        materialCategories.forEach(category -> {
            if (longs.contains(category.getParentId())) {
                longs.add(category.getId());
            }
        });
        return new ArrayList<>(longs);
    }

    @Override
    public void updateBatch(List<MaterialCategory> materialCategories) {
        materialCategoryMapper.updateBatch(materialCategories);
    }

    @Override
    public void unregisterCategory(UnregisterMaterialCategoryDTO dto) {
        MaterialCategory materialCategory = materialCategoryMapper.selectById(dto.getCategoryId());
        Map<String, IssueBusinessVO> platformMap = businessesConfig.getPlatformMap();
        IssueBusinessVO platform = platformMap.get(dto.getPlatformName());
        Map<Integer, ChildBusinessVO> childMap = CollectionUtils.convertMap(platform.getChildren(), ChildBusinessVO::getChildCode);
        String childName = childMap.get(dto.getChildCode()).getChildName();
        List<String> split = StrUtil.split(materialCategory.getDispenseRecord(), StrUtil.SLASH);
        split.remove(childName);
        materialCategory.setDispenseRecord(StrUtil.join(StrUtil.SLASH, split));
        materialCategoryMapper.updateById(materialCategory);
    }

    @Override
    public List<Long> selectIdsByKeyWord(String keyword) {
        return materialCategoryMapper.selectIdsByKeyWord(keyword);
    }

    @Override
    public List<MaterialCategory> selectListByCategoryCodeList(List<String> categoryCode) {
        if (CollUtil.isEmpty(categoryCode)){
            return new ArrayList<>();
        }
        return materialCategoryMapper.selectListByCategoryCodeList(categoryCode);
    }
}
