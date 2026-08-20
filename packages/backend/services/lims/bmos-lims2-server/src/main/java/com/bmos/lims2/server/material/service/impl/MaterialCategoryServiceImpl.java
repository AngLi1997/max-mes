package com.bmos.lims2.server.material.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.response.ResponseItem;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.constants.MaterialConstants;
import com.bmos.lims2.common.enums.CategoryInfoTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.material.converter.MaterialConvert;
import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.material.entity.MaterialCategory;
import com.bmos.lims2.server.material.mapper.MaterialCategoryMapper;
import com.bmos.lims2.server.platform.material.PlatformMaterialFeignClient;
import com.bmos.lims2.server.platform.material.dto.CategoryIssueDTO;
import com.bmos.lims2.server.platform.material.dto.ProductMaterialCategorySaveDTO;
import com.bmos.lims2.server.platform.material.dto.SyncTreeNodeDTO;
import com.bmos.lims2.server.material.service.MaterialCategoryService;
import com.bmos.lims2.server.material.service.MaterialService;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.mybatis.CustomIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialCategoryServiceImpl implements MaterialCategoryService {

    @Autowired
    MaterialCategoryMapper materialCategoryMapper;

    @Autowired
    PlatformMaterialFeignClient platformMaterialFeignClient;

    @Autowired
    MaterialService materialService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(MaterialCategoryCreateDTO inspectionCategoryCreateReqDTO) {
        Long parentId = inspectionCategoryCreateReqDTO.getParentId();
        boolean insertRoot = ObjectUtil.equal(parentId, TreeUtil.parentId);
        MaterialCategory parentCategory = materialCategoryMapper.selectById(inspectionCategoryCreateReqDTO.getParentId());
        if (!insertRoot && ObjectUtil.isNull(parentCategory)) {
            throw new BmosException(LimsResponseCode.MATERIAL_CATEGORY_NOT_EXISTED);
        }
        Long platformCategoryId = 0L;
        String mergeCode = inspectionCategoryCreateReqDTO.getCode();
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
        if (ObjectUtil.isNotNull(parentCategory)) {
            platformCategoryId = parentCategory.getPlatformCategoryId();
//            mergeCode = parentCategory.getMergeCode() + dto.getCode();
        }
        checkPlatformMaterialCodeExisted(mergeCode);
        ProductMaterialCategorySaveDTO remoteDTO = getRemoteSaveCategoryDTO(inspectionCategoryCreateReqDTO, platformCategoryId);
        doSave(inspectionCategoryCreateReqDTO, remoteDTO, mergeCode);
    }
    private ProductMaterialCategorySaveDTO getRemoteSaveCategoryDTO(MaterialCategoryCreateDTO dto, Long platformParentId) {
        ProductMaterialCategorySaveDTO remoteDTO = new ProductMaterialCategorySaveDTO();
        remoteDTO.setParentId(platformParentId);
        remoteDTO.setCode(dto.getCode());
        remoteDTO.setName(dto.getName());
        remoteDTO.setBusinessRegister(true);
        remoteDTO.setBusinessName(CategoryInfoTypeEnum.INSPECTION.getName());
        return remoteDTO;
    }

    /**
     * 校验code在平台是否存在
     *
     * @param code
     */
    private void checkPlatformMaterialCodeExisted(String code) {
        ResponseInfo<Boolean> checkRes = FeignUtils.handleRequest((data) -> platformMaterialFeignClient.checkMergeCodeExisted(code, null), null);
        if (checkRes.isError()) {
            throw new BmosException(LimsResponseCode.PLATFORM_CHECK_CODE_ERROR, checkRes.getMessage());
        }
        if (checkRes.getData()) {
            throw new BmosException(LimsResponseCode.PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED);
        }
    }

    private void doSave(MaterialCategoryCreateDTO dto, ProductMaterialCategorySaveDTO remoteDto, String mergeCode) {
        // platform save
        ResponseInfo<Long> saveRes = FeignUtils.handleRequest(data -> platformMaterialFeignClient.saveMaterialCategory(data), remoteDto);
        if (ObjectUtil.isNotNull(saveRes.getData())) {
            MaterialCategory productMaterialCategory = MaterialConvert.INSTANCE.convertMaterialCategory(dto);
            productMaterialCategory.setId(CustomIdGenerator.nextId());
            productMaterialCategory.setPlatformCategoryId(saveRes.getData());
            productMaterialCategory.setMergeCode(mergeCode);
            productMaterialCategory.setCategoryType(CategoryInfoTypeEnum.INSPECTION.getValue());
            // mes save
            materialCategoryMapper.insert(productMaterialCategory);
            return;
        }
        throw new BmosException(LimsResponseCode.PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED);
    }

    @Override
    public void deleteCategory(Long id) {
        // 分类下是否有子分类
        if (materialCategoryMapper.existsChild(id)){
            throw new BmosException(LimsResponseCode.CATEGORY_EXIST_CHILD);
        }
        // 分类下是否有检品信息
        if (materialService.existsProducts(id)){
            throw new BmosException(LimsResponseCode.CATEGORY_EXIST_PRODUCTS);
        }
        materialCategoryMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(MaterialCategoryUpdateDTO materialCategoryUpdateDTO) {
        // 校验分类是否存在
        MaterialCategory materialCategory = materialCategoryMapper.selectById(materialCategoryUpdateDTO.getId());
        if (Objects.isNull(materialCategory)){
            throw new BmosException(LimsResponseCode.CATEGORY_NOT_EXISTS);
        }
        doUpdateCategory(materialCategoryUpdateDTO, materialCategory);
    }

    @Override
    public List<MaterialCategoryTreeNodeDTO> queryCategoryTree(MaterialCategoryTreeQueryDTO materialCategoryQueryDTO) {
        MaterialCategoryParamDTO param = MaterialConvert.INSTANCE.convert2CategoryParam(materialCategoryQueryDTO);
        param.setOrder(MaterialConstants.DEFAULT_CATEGORY_QUERY_SORT);
        List<MaterialCategory> basicCategoryList = materialCategoryMapper.selectByParam(param);
        List<MaterialCategoryTreeNodeDTO> inspectionTreeNodeVOList = MaterialConvert.INSTANCE.convertDO2TreeNodeVO(basicCategoryList);
        inspectionTreeNodeVOList.forEach(node -> {
            node.setShowName(node.getMergeCode() + MaterialConstants.CATEGORY_SHOW_SPLIT + node.getName());
            node.setCategoryFlag(Boolean.TRUE);
        });
        return TreeUtil.buildTree(inspectionTreeNodeVOList, false);
    }

    @Override
    public List<MaterialCategoryTreeNodeDTO> getSyncTree(MaterialCategoryTreeQueryDTO materialCategoryTreeQueryDTO) {
        ResponseInfo<List<SyncTreeNodeDTO>> syncTree = null;
        try{
            syncTree = platformMaterialFeignClient.getMaterialTree();
            log.info("请求物料树调用平台feign接口 sync={}", JSON.toJSONString(syncTree));
        } catch (Exception e){
            log.error("请求物料树调用平台feign接口失败 inspectionTreeQueryDTO={}", JSON.toJSONString(materialCategoryTreeQueryDTO), e);
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR, e.getMessage());
        }
        if (Objects.isNull(syncTree) || !syncTree.isSuccess()) {
            // 平台返回失败
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR, syncTree.getMessage());
        }
        List<SyncTreeNodeDTO> data = syncTree.getData();
        // 所有分类节点
        List<Long> platformIds = data.stream()
                .filter(vo -> !vo.isCategoryFlag()).map(SyncTreeNodeDTO::getId).collect(Collectors.toList());
        // 查询获取的平台物料是否已经有同步到本系统的
        MaterialParamDTO param = new MaterialParamDTO().setPlatformMaterialIdList(CollectionUtil.isNotEmpty(platformIds) ? platformIds : null).setCategoryType(materialCategoryTreeQueryDTO.getCategoryType());
        List<MaterialDTO> existed = materialService.selectByParam(param);
        Set<Long> longs = CollectionUtils.convertSet(existed, MaterialDTO::getPlatformMaterialId);
        // 过滤已存在的物料
        recRemoveIfExisted(data, longs);
        // 关键字搜索过滤空节点
        if (StrUtil.isNotBlank(materialCategoryTreeQueryDTO.getKeyword())) {
            data.forEach(this::cleanTree);
            data.removeIf(node -> node.isCategoryFlag() && CollUtil.isEmpty(node.getChildren()));
        }
        return MaterialConvert.INSTANCE.convert2TreeNodeDTO(data, null);
    }

    @Override
    public List<MaterialCategoryTreeNodeDTO> getSyncTreeAll() {
        ResponseInfo<List<SyncTreeNodeDTO>> res;
        try {
            res = platformMaterialFeignClient.getSyncTreeAll();
            log.info("请求全量物料分类树调用平台feign接口 res={}", JSON.toJSONString(res));
        } catch (Exception e) {
            log.error("从平台获取同步全量树feign调用异常", e);
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        if (Objects.isNull(res) || !res.isSuccess()) {
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        List<MaterialCategoryTreeNodeDTO> inspectionTreeNodeVOS = MaterialConvert.INSTANCE.convert2TreeNodDTO(res.getData(), null);
        inspectionTreeNodeVOS.forEach(this::fillCategoryFlag);
        return inspectionTreeNodeVOS;
    }

    @Override
    public void issueCategory(List<CategoryIssueDTO> categoryList, Integer categoryType) {
        if (CollectionUtil.isEmpty(categoryList)){
            return ;
        }
        List<Long> platformIdList = categoryList.stream().map(CategoryIssueDTO::getId).collect(Collectors.toList());
        MaterialCategoryParamDTO param = new MaterialCategoryParamDTO().setPlatformCategoryIdList(platformIdList).setCategoryType(categoryType);
        // 查询是否已同步的信息
        List<MaterialCategory> existed = materialCategoryMapper.selectByParam(param);
        Map<Long, MaterialCategory> platformCategoryIdMap = CollectionUtil.isEmpty(existed) ? new HashMap<>() : existed.stream().collect(Collectors.toMap(MaterialCategory::getPlatformCategoryId, Function.identity()));
        List<MaterialCategory> needInsertList = findNeedInsertCategory(categoryList, platformCategoryIdMap, categoryType);

        if (!CollectionUtil.isNotEmpty(needInsertList)){
            // 不需要进行新增
            return;
        }
        materialCategoryMapper.insertBatch(needInsertList);
        // 将新增的分类且带有本系统的id放入map中 方便进行parentId填充
        for (MaterialCategory category : needInsertList) {
            platformCategoryIdMap.put(category.getPlatformCategoryId(), category);
        }
        List<MaterialCategory> needUpdateList = findNeedUpdateCategory(categoryList, platformCategoryIdMap);
        if (CollectionUtil.isNotEmpty(needUpdateList)){
            materialCategoryMapper.updateBatch(needUpdateList);
        }
    }

    /**
     * 更新物料分类 只能更改名称
     * @param materialCategoryUpdateDTO
     */
    private void doUpdateCategory(MaterialCategoryUpdateDTO materialCategoryUpdateDTO, MaterialCategory basicCategory) {
        basicCategory.setName(materialCategoryUpdateDTO.getName());
        materialCategoryMapper.updateById(basicCategory);
    }

    /**
     * 寻找需要更新的分类信息
     * @param categoryList
     * @param platformCategoryIdMap
     * @return
     */
    private List<MaterialCategory> findNeedUpdateCategory(List<CategoryIssueDTO> categoryList, Map<Long, MaterialCategory> platformCategoryIdMap) {
        List<MaterialCategory> needUpdateList = new ArrayList<>();
        for (CategoryIssueDTO categoryIssueDTO : categoryList) {
            MaterialCategory curCategory = platformCategoryIdMap.get(categoryIssueDTO.getId());
            if (Objects.nonNull(curCategory.getParentId())){
                continue ;
            }
            MaterialCategory parentCategory = platformCategoryIdMap.get(categoryIssueDTO.getParentId());
            curCategory.setParentId(parentCategory.getId());
            needUpdateList.add(curCategory);
        }
        return needUpdateList;
    }

    /**
     * 需要新增的分类信息
     * @param categoryList
     * @param platformCategoryIdMap
     * @param categoryType
     * @return
     */
    private List<MaterialCategory> findNeedInsertCategory(List<CategoryIssueDTO> categoryList, Map<Long, MaterialCategory> platformCategoryIdMap, Integer categoryType) {
        List<MaterialCategory> needInsertList = new ArrayList<>();
        for (CategoryIssueDTO categoryIssueDTO : categoryList) {
            if (platformCategoryIdMap.containsKey(categoryIssueDTO.getId())){
                continue;
            }
            // 判断是否需要进行parent_id填充
            MaterialCategory materialCategory = convert2InspectCategoryDO(categoryIssueDTO, platformCategoryIdMap, categoryType);
            materialCategory.setId(CustomIdGenerator.nextId());
            needInsertList.add(materialCategory);
        }
        return needInsertList;
    }

    /**
     * 转换为分类DO
     * @param categoryIssueDTO
     * @return
     */
    private MaterialCategory convert2InspectCategoryDO(CategoryIssueDTO categoryIssueDTO,
                                                    Map<Long, MaterialCategory> platformCategoryIdMap,
                                                    Integer categoryType) {
        MaterialCategory basicCategory = MaterialConvert.INSTANCE.convert2CategoryDO(categoryIssueDTO);
        // 当前获取的对象的父级id是平台的id，故需要设为null
        basicCategory.setParentId(null);
        basicCategory.setCategoryType(categoryType);
        basicCategory.setPlatformCategoryId(categoryIssueDTO.getId());
        // 填充父级id
        if (TreeUtil.parentId.equals(categoryIssueDTO.getParentId())){
            basicCategory.setParentId(TreeUtil.parentId);
        } else if (platformCategoryIdMap.containsKey(categoryIssueDTO.getParentId())){
            MaterialCategory parentCategory = platformCategoryIdMap.get(categoryIssueDTO.getParentId());
            basicCategory.setParentId(parentCategory.getId());
        }
        return basicCategory;
    }


    @Override
    public List<MaterialCategoryDTO> selectByPlatformCategories(List<Long> platformCategorieList, Integer categoryType) {
        MaterialCategoryParamDTO param = new MaterialCategoryParamDTO().setPlatformCategoryIdList(platformCategorieList).setCategoryType(categoryType);
        return BeanUtil.copyToList(materialCategoryMapper.selectByParam(param), MaterialCategoryDTO.class);
    }

    @Override
    public List<Long> selectChildCategoryId(Long categoryId) {
        List<MaterialCategory> basicCategories = materialCategoryMapper.selectChildCategoryId(categoryId);
        if (CollUtil.isEmpty(basicCategories)){
            return new ArrayList<>();
        }
        return basicCategories.stream().map(MaterialCategory::getId).collect(Collectors.toList());
    }

    @Override
    public MaterialCategoryDTO getById(Long categoryId) {
        return BeanUtil.copyProperties(materialCategoryMapper.selectById(categoryId), MaterialCategoryDTO.class);
    }

    @Override
    public List<MaterialCategoryDTO> getAllCategory() {
        return BeanUtil.copyToList(materialCategoryMapper.selectList(), MaterialCategoryDTO.class);
    }

    /**
     * 过滤孩子节点中的空节点信息
     * @param node: 需要过滤的节点以及其子节点信息
     * @return
     */
    private SyncTreeNodeDTO cleanTree(SyncTreeNodeDTO node) {
        if (CollUtil.isEmpty(node.getChildren())) {
            return node;
        }
        List<SyncTreeNodeDTO> cleanedChildList = new ArrayList<>();
        for (SyncTreeNodeDTO child : node.getChildren()) {
            if (child.isCategoryFlag()) {
                SyncTreeNodeDTO cleanedChild = cleanTree(child);
                if (CollUtil.isNotEmpty(cleanedChild.getChildren())) {
                    cleanedChildList.add(cleanedChild);
                }
            } else {
                cleanedChildList.add(child);
            }
        }
        node.setChildren(cleanedChildList);
        return node;
    }

    /**
     * 移除treeNodes中包含existedIds的节点
     * @param treeNodes
     * @param existedIds
     */
    private void recRemoveIfExisted(List<SyncTreeNodeDTO> treeNodes, Set<Long> existedIds) {
        treeNodes.removeIf(node -> existedIds.contains(node.getId()));
        treeNodes.forEach(node -> {
            if (node.isCategoryFlag() && CollUtil.isNotEmpty(node.getChildren())) {
                recRemoveIfExisted(node.getChildren(), existedIds);
            }
        });
    }

    /**
     * 保存分类 具体实现
     * @param categoryCreateDTO
     * @param productMaterialCategorySaveDTO
     * @param mergeCode
     */
    private void doSaveCategory(MaterialCategoryCreateDTO categoryCreateDTO, ProductMaterialCategorySaveDTO productMaterialCategorySaveDTO, String mergeCode) {
        ResponseInfo<Long> responseInfo = null;
        try {
            responseInfo = platformMaterialFeignClient.saveMaterialCategory(productMaterialCategorySaveDTO);
            log.info("调用保存物料分类feign接口成功 response={}", JSON.toJSONString(responseInfo));
        } catch (Exception e) {
            log.error("调用保存物料分类feign接口失败 productMaterialCategorySaveDTO={}", JSON.toJSONString(productMaterialCategorySaveDTO), e);
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        if (Objects.isNull(responseInfo) || responseInfo.isError()) {
            log.error(JsonUtils.toJsonString(responseInfo));
            throw new BmosException(new ResponseItem(responseInfo.getCode(), responseInfo.getMessage(), "调用"));
        }
        if (Objects.isNull(responseInfo.getData())){
            throw new BmosException(LimsResponseCode.CODE_EXISTED);
        }
        MaterialCategory basicCategory =  MaterialConvert.INSTANCE.convert2ProductsDO(categoryCreateDTO);
        basicCategory.setId(CustomIdGenerator.nextId());
        basicCategory.setPlatformCategoryId(responseInfo.getData());
        basicCategory.setMergeCode(mergeCode);
        basicCategory.setCategoryType(CategoryInfoTypeEnum.INSPECTION.getValue());
        materialCategoryMapper.insert(basicCategory);
    }

    /**
     * 构建检品信息分类DTO
     * @param materialCategoryCreateDTO
     * @param parentPlatformCategoryId
     * @return
     */
    private ProductMaterialCategorySaveDTO buildPlatformCategoryDTO(MaterialCategoryCreateDTO materialCategoryCreateDTO, Long parentPlatformCategoryId) {
        ProductMaterialCategorySaveDTO remoteDTO = new ProductMaterialCategorySaveDTO();
        remoteDTO.setParentId(parentPlatformCategoryId);
        remoteDTO.setCode(materialCategoryCreateDTO.getCode());
        remoteDTO.setName(materialCategoryCreateDTO.getName());
        remoteDTO.setBusinessRegister(true);
        remoteDTO.setBusinessName(CategoryInfoTypeEnum.getNameByValue(CategoryInfoTypeEnum.INSPECTION.getValue()));
        return remoteDTO;
    }

    /**
     * 校验新增分类参数是否合规
     * @param inspectionCategoryCreateReqDTO
     */
    private MaterialCategory judge(MaterialCategoryCreateDTO inspectionCategoryCreateReqDTO) {
        MaterialCategory basicCategory = null;
        if (!TreeUtil.parentId.equals(inspectionCategoryCreateReqDTO.getParentId()) && Objects.nonNull(inspectionCategoryCreateReqDTO.getParentId())){
            basicCategory = materialCategoryMapper.selectById(inspectionCategoryCreateReqDTO.getParentId());
            if (Objects.isNull(basicCategory)){
                throw new BmosException(LimsResponseCode.INSPECTION_CATEGORY_NOT_EXISTED);
            }
        }
        return basicCategory;
    }

    /**
     * 填充是否为物料分类节点
     * @param inspectionTreeNodeVO
     */
    private void fillCategoryFlag(MaterialCategoryTreeNodeDTO inspectionTreeNodeVO) {
        if (CollectionUtil.isNotEmpty(inspectionTreeNodeVO.getChildren())){
            inspectionTreeNodeVO.getChildren().forEach(this::fillCategoryFlag);
        }
        inspectionTreeNodeVO.setCategoryFlag(Boolean.TRUE);
    }
}
