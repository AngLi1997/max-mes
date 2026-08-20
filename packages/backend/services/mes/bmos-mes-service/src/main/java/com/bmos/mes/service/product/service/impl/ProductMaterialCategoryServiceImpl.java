package com.bmos.mes.service.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.product.PlatformMaterialFeignClient;
import com.bmos.mes.service.product.convert.ProductMaterialCategoryConverter;
import com.bmos.mes.service.product.dto.*;
import com.bmos.mes.service.product.mapper.ProductMaterialCategoryMapper;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.product.service.ProductMaterialCategoryService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.ProductListVO;
import com.bmos.mes.service.product.vo.ProductMaterialCategoryTreeNodeVO;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductMaterialCategoryServiceImpl implements ProductMaterialCategoryService {

    @Autowired
    private ProductMaterialCategoryMapper materialCategoryMapper;

    @Autowired
    private PlatformMaterialFeignClient platformMaterialFeignClient;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Value("${spring.application.name}")
    private String platformName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void save(ProductMaterialCategorySaveDTO dto) {
        Long parentId = dto.getParentId();
        boolean insertRoot = ObjectUtil.equal(parentId, TreeUtil.parentId);
        ProductMaterialCategory parentCategory = materialCategoryMapper.selectById(dto.getParentId());
        if (!insertRoot && ObjectUtil.isNull(parentCategory)) {
            throw new BmosException(MesResponseCode.MATERIAL_CATEGORY_NOT_EXISTED);
        }
        Long platformCategoryId = 0L;
        String mergeCode = dto.getCode();
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
        if (ObjectUtil.isNotNull(parentCategory)) {
            platformCategoryId = parentCategory.getPlatformCategoryId();
//            mergeCode = parentCategory.getMergeCode() + dto.getCode();
        }
        checkPlatformMaterialCodeExisted(mergeCode);
        ProductMaterialCategorySaveDTO remoteDTO = getRemoteSaveCategoryDTO(dto, platformCategoryId);
        doSave(dto, remoteDTO, mergeCode);
    }

    /**
     * 校验code在平台是否存在
     *
     * @param code
     */
    private void checkPlatformMaterialCodeExisted(String code) {
        ResponseInfo<Boolean> checkRes = FeignUtils.handleRequest((data) -> platformMaterialFeignClient.checkMergeCodeExisted(code, null), null);
        if (checkRes.isError()) {
            throw new BmosException(MesResponseCode.PLATFORM_CHECK_CODE_ERROR, checkRes.getMessage());
        }
        if (checkRes.getData()) {
            throw new BmosException(MesResponseCode.PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED);
        }
    }

    private void doSave(ProductMaterialCategorySaveDTO dto, ProductMaterialCategorySaveDTO remoteDto, String mergeCode) {
        // platform save
        ResponseInfo<Long> saveRes = FeignUtils.handleRequest(data -> platformMaterialFeignClient.saveMaterialCategory(data), remoteDto);
        if (ObjectUtil.isNotNull(saveRes.getData())) {
            ProductMaterialCategory productMaterialCategory = ProductMaterialCategoryConverter.INSTANCE.convertMaterialCategory(dto);
            productMaterialCategory.setId(CustomIdGenerator.nextId());
            productMaterialCategory.setPlatformCategoryId(saveRes.getData());
            productMaterialCategory.setMergeCode(mergeCode);
            // mes save
            materialCategoryMapper.insert(productMaterialCategory);
            return;
        }
        throw new BmosException(MesResponseCode.PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED);
    }

    private ProductMaterialCategorySaveDTO getRemoteSaveCategoryDTO(ProductMaterialCategorySaveDTO dto, Long platformParentId) {
        ProductMaterialCategorySaveDTO remoteDTO = new ProductMaterialCategorySaveDTO();
        remoteDTO.setParentId(platformParentId);
        remoteDTO.setCode(dto.getCode());
        remoteDTO.setName(dto.getName());
        remoteDTO.setBusinessRegister(true);
        remoteDTO.setBusinessName(CategoryInfoTypeEnum.getNameByValue(dto.getCategoryType()));
        return remoteDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void delete(Long id) {
        // 校验子级
        if (materialCategoryMapper.existsChild(id)) {
            throw new BmosException(MesResponseCode.MATERIAL_CATEGORY_EXISTS_CHILD);
        }
        // 校验是否有产品关联
        if (productMaterialService.existsCategoryMaterial(id)) {
            throw new BmosException(MesResponseCode.MATERIAL_CATEGORY_LINKED_PRODUCT);
        }
        // 向平台取消注册
        ProductMaterialCategory category = materialCategoryMapper.selectById(id);
        UnregisterMaterialCategoryDTO unregisterDTO = new UnregisterMaterialCategoryDTO(platformName,
                category.getCategoryType(), category.getPlatformCategoryId());
        FeignUtils.handleRequest(data -> platformMaterialFeignClient.unregisterCategory(data), unregisterDTO);
        materialCategoryMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void update(ProductMaterialCategoryUpdateDTO dto) {
        // 更新前校验 只能修改名字
        validatedBeforeUpdate(dto);
        // 更新本地名称
        doUpdate(dto);
    }

    private void doUpdate(ProductMaterialCategoryUpdateDTO dto) {
        // 更新本地
        materialCategoryMapper.updateById(ProductMaterialCategoryConverter.INSTANCE.convertMaterialCategory(dto));
    }

    private void validatedBeforeUpdate(ProductMaterialCategoryUpdateDTO dto) {
        // 校验该id记录是否存在
        if (!materialCategoryMapper.existedId(dto.getId())) {
            throw new BmosException(MesResponseCode.MATERIAL_CATEGORY_NOT_EXISTED);
        }
    }

    @Override
    public List<ProductMaterialCategoryTreeNodeVO> queryCategoryTree(ProductMaterialCategoryQueryDTO queryDto) {
        List<ProductMaterialCategory> productMaterialCategories = materialCategoryMapper.selectListByQuery(queryDto);
        List<ProductMaterialCategoryTreeNodeVO> nodes = ProductMaterialCategoryConverter.INSTANCE
                .convertCategoryTreeNode(productMaterialCategories);
        nodes.forEach(node -> node.setShowName(node.getMergeCode() + StrUtil.DASHED + node.getName()));
        return TreeUtil.buildTree(nodes, false);
    }

    /**
     * @param categoryList 下发的分类
     * @param businesses   下发的子业务列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueCategory(List<CategoryIssueDTO> categoryList, List<Integer> businesses) {
        // 查询出存在的全量数据
        List<ProductMaterialCategory> categories = materialCategoryMapper.selectList();
        // 根据categoryType分组 获取平台关联id列表
        Map<Integer, Set<Long>> dbIds = categories.stream()
                .collect(Collectors.groupingBy(ProductMaterialCategory::getCategoryType,
                        Collectors.mapping(ProductMaterialCategory::getPlatformCategoryId, Collectors.toSet())));
        // 根据categoryType分组 获取全量分类列表
        Map<Integer, List<ProductMaterialCategory>> bussinessMap = categories.stream()
                .collect(Collectors.groupingBy(ProductMaterialCategory::getCategoryType));
        // 需要新增的列表
        List<ProductMaterialCategory> allNewCategories = new ArrayList<>();
        for (Integer business : businesses) {
            List<ProductMaterialCategory> currentCategories = bussinessMap.get(business);
            // 过滤需要新增的分类
            List<ProductMaterialCategory> needInsert = filterNeedInsert(categoryList, business, dbIds);
            // 处理父级id
            handleParentId(currentCategories, needInsert);
            allNewCategories.addAll(needInsert);
        }
        materialCategoryMapper.insertBatch(allNewCategories);
    }

    /**
     * 处理
     *
     * @param currentCategories 当前业务所有分类
     * @param needInsert        需要新增
     */
    private static void handleParentId(List<ProductMaterialCategory> currentCategories, List<ProductMaterialCategory> needInsert) {
        Map<Long, ProductMaterialCategory> dbMap = CollectionUtils.convertMap(currentCategories, ProductMaterialCategory::getPlatformCategoryId);
        Map<Long, ProductMaterialCategory> newMap = CollectionUtils.convertMap(needInsert, ProductMaterialCategory::getPlatformCategoryId);
        for (ProductMaterialCategory insert : needInsert) {
            if (!ObjectUtil.equal(insert.getParentId(), 0L)) {
                Long platformParentId = insert.getParentId();
                ProductMaterialCategory dbParent = dbMap.get(platformParentId);
                if (ObjectUtil.isNull(dbParent) && ObjectUtil.isNull(newMap.get(platformParentId))) {
                    throw new BmosException(MesResponseCode.MATERIAL_SYNC_ERROR_CHOSE_NOT_PARENT);
                }
                insert.setParentId(ObjectUtil.isNull(dbParent) ? newMap.get(platformParentId).getId() : dbParent.getId());
            }
        }
    }


    private static List<ProductMaterialCategory> filterNeedInsert(List<CategoryIssueDTO> categoryList, Integer business, Map<Integer, Set<Long>> dbIds) {
        List<CategoryIssueDTO> collect;
        Set<Long> longs = dbIds.get(business);
        collect = categoryList;
        if (CollUtil.isNotEmpty(longs)) {
            collect = collect.stream()
                    .filter(c -> !longs.contains(c.getId())).collect(Collectors.toList());
        }
        return collect.stream().map(platformCategory -> {
            ProductMaterialCategory newCategory = ProductMaterialCategoryConverter.INSTANCE.convertMaterialCategory(platformCategory);
            newCategory.setPlatformCategoryId(platformCategory.getId());
            newCategory.setId(CustomIdGenerator.nextId());
            newCategory.setCategoryType(business);
            return newCategory;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProductMaterialCategory> listAll() {
        return materialCategoryMapper.selectList();
    }

    @Override
    public List<ProductMaterialCategory> selectListByType(Integer business) {
        return materialCategoryMapper.selectListByType(business);
    }

    @Override
    public List<ProductMaterialCategory> selectList() {
        return materialCategoryMapper.selectList();
    }

    @Override
    public ProductMaterialCategory selectById(Long materialCategoryId) {
        return materialCategoryMapper.selectById(materialCategoryId);
    }

    @Override
    public List<Long> getAllChildCategory(Long parentId) {
        if (ObjectUtil.isNull(parentId)) {
            return new ArrayList<>();
        }
        List<ProductMaterialCategory> categories = materialCategoryMapper.selectList();
        return getChildrenIdList(parentId, categories);
    }

    @Override
    public List<Long> getAllProductIds(Long parentCategoryId) {
        if (ObjectUtil.isNull(parentCategoryId)) {
            return productMaterialService.getProductList(CategoryInfoTypeEnum.PRODUCTION.getValue())
                    .stream()
                    .map(ProductListVO::getId)
                    .collect(Collectors.toList());
        }
        List<ProductMaterialCategory> categories = materialCategoryMapper.selectList();
        List<Long> categoryIds = getChildrenIdList(parentCategoryId, categories);
        if (categoryIds.isEmpty()){
            return new ArrayList<>();
        }
        return productMaterialService.getIdListByCategoryIdList(categoryIds);
    }


    @Override
    public List<ProductMaterialCategory> selectListByTypes(List<Integer> types) {
        return materialCategoryMapper.selectListByTypes(types);
    }

    @Override
    public List<Long> getAllChildCategory(CategoryInfoTypeEnum categoryInfoType, Long parentId) {
        List<ProductMaterialCategory> categories = materialCategoryMapper.selectListByType(categoryInfoType.getValue());
        if (ObjectUtil.isNull(parentId)) {
            return new ArrayList<>();
        }
        return getChildrenIdList(parentId, categories);
    }

    @Override
    public List<Long> getIdListByCategoryIdList(List<Long> categoryIdList) {
        return productMaterialService.getIdListByCategoryIdList(categoryIdList);
    }

    private List<Long> getChildrenIdList(Long parentId, List<ProductMaterialCategory> categories) {
        categories.sort(Comparator.comparing(BaseDO::getCreateTime));
        Set<Long> ids = new HashSet<>();
        ids.add(parentId);
        categories.forEach(category -> {
            if (ids.contains(category.getParentId())) {
                ids.add(category.getId());
            }
        });
        return new ArrayList<>(ids);
    }
}
