package com.bmos.lims2.server.material.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.feign.material.dto.RemoteIssueFeignDTO;
import com.bmos.lims2.common.constants.BasicConstants;
import com.bmos.lims2.common.enums.CategoryInfoTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.material.converter.MaterialConvert;
import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.entity.MaterialCategory;
import com.bmos.lims2.server.material.mapper.MaterialCategoryMapper;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfig;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfigMaterial;
import com.bmos.lims2.server.inspect.document.mapper.DocumentConfigMapper;
import com.bmos.lims2.server.inspect.document.mapper.DocumentConfigMaterialMapper;
import com.bmos.lims2.server.platform.material.PlatformMaterialFeignClient;
import com.bmos.lims2.server.platform.material.dto.*;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.server.material.service.MaterialCategoryService;
import com.bmos.lims2.server.material.service.MaterialFieldService;
import com.bmos.lims2.server.material.service.MaterialService;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialMapper materialMapper;

    @Autowired
    PlatformMaterialFeignClient platformMaterialFeignClient;

    @Resource
    private UnitCache unitCache;

    @Value("${spring.application.name}")
    private String limsName;

    @Resource
    private MaterialFieldService materialFieldService;

    @Resource
    private MaterialCategoryService categoryService;

    @Autowired
    MaterialCategoryMapper materialCategoryMapper;

    @Autowired
    DocumentConfigMapper documentConfigMapper;

    @Autowired
    DocumentConfigMaterialMapper documentConfigMaterialMapper;

    @Autowired
    InspectionSchemeMapper inspectionSchemeMapper;
    @Value("${spring.application.name}")
    private String serverName;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void syncMaterialAndCategory(MaterialSyncDTO materialSyncDTO) {
        MaterialIssueRequestDTO remoteDTO = new MaterialIssueRequestDTO();
        remoteDTO.setMaterialIds(materialSyncDTO.getMaterialIds());
        remoteDTO.setMaterialCategoryIds(materialSyncDTO.getMaterialCategoryIds());
        MaterialIssueBusinessDTO materialIssueBusinessDTO = new MaterialIssueBusinessDTO();
        materialIssueBusinessDTO.setChildCodeList(Collections.singletonList(materialSyncDTO.getCategoryType()));
        materialIssueBusinessDTO.setPlatformName(serverName);
        remoteDTO.setBusinesses(Collections.singletonList(materialIssueBusinessDTO));
        FeignUtils.handleRequest(data -> platformMaterialFeignClient.issueMaterialAndCategory(data), remoteDTO);

    }

    @Override
    public CommonPage<MaterialDTO> inspectionProductsPage(MaterialPageQueryDTO materialPageQueryDTO) {
        PageHelper.startPage(materialPageQueryDTO.getPageNum(), materialPageQueryDTO.getPageSize(), BasicConstants.MATERIAL_PAGE_ORDER);
        MaterialParamDTO productsParam = MaterialConvert.INSTANCE.convert2ProductsParam(materialPageQueryDTO);
        if (!Objects.isNull(materialPageQueryDTO.getCategoryId()) && !TreeUtil.parentId.equals(materialPageQueryDTO.getCategoryId())) {
            // 若categoryId不为空 且id非0 则查询当前categoryId下的所有子分类
            List<Long> categoryIdList = categoryService.selectChildCategoryId(materialPageQueryDTO.getCategoryId());
            categoryIdList.add(materialPageQueryDTO.getCategoryId());
            productsParam.setCategoryIdList(categoryIdList);
        }
        List<MaterialDTO> dtoList = materialMapper.selectPageWithCategory(productsParam);
        Map<Long, MaterialCategoryDTO> categoryMap = buildCategoryFullName();
        CommonPage<MaterialDTO> result = CommonPage.convertPage(dtoList);
        result.getList().forEach(item -> {
            item.setCategoryName(Optional.ofNullable(categoryMap.get(item.getCategoryId()))
                    .map(MaterialCategoryDTO::getName)
                    .orElse(StrUtil.EMPTY));
        });
        return result;
    }

    private Map<Long, MaterialCategoryDTO> buildCategoryFullName() {
        List<MaterialCategoryDTO> categories = categoryService.getAllCategory();
        Map<Long, MaterialCategoryDTO> categoryIdMap = CollectionUtils.convertMap(categories, MaterialCategoryDTO::getId);
        CollUtil.sort(categories, Comparator.comparing(BaseDO::getCreateTime));
        for (MaterialCategoryDTO category : categories) {
            Long parentId = category.getParentId();
            if (!ObjectUtil.equal(parentId, TreeUtil.parentId)) {
                MaterialCategoryDTO parent = categoryIdMap.get(parentId);
                category.setName(parent.getName() + "/" + category.getName());
            }
        }
        return categoryIdMap;
    }

    @Override
    public boolean existsProducts(Long categoryId) {
        return materialMapper.existsProducts(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void deleteProducts(Long id) {
        validDelete(id);
        // 删除后取消向平台注册
        Material basicProducts = materialMapper.selectById(id);
        if (Objects.isNull(basicProducts)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        UnregisterMaterialDTO dto = new UnregisterMaterialDTO(limsName, CategoryInfoTypeEnum.INSPECTION.getChildCode(), basicProducts.getPlatformMaterialId());
        try {
            platformMaterialFeignClient.unregisterMaterial(dto);
        } catch (Exception e) {
            throw new BmosException(BaseResponseCode.FEIGN_REMOTE_CALL_ERROR);
        }
        materialMapper.deleteById(id);
        materialFieldService.deleteByProductsId(id);
    }

    /**
     * 校验删除参数
     * 1. 是否存在
     * 2. 已生成请验单的检品无法删除
     *
     * @param id
     */
    private void validDelete(Long id) {
        if (!materialMapper.existById(id)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        // 是否生成请验单
        // TODO: 2025/7/14 @yigaohui 请验单判断
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void updateProducts(MaterialUpdateDTO materialUpdateDTO) {
        // 查询当前检品信息
        Material material = materialMapper.selectById(materialUpdateDTO.getId());
        // 校验检品信息是否存在
        if (Objects.isNull(material)) {
            throw new BmosException(LimsResponseCode.PRODUCTS_NOT_EXIST);
        }
        // 更新检品信息
        doUpdate(materialUpdateDTO, material);
    }

    /**
     * 更新检品信息
     *
     * @param materialUpdateDTO
     * @param material
     */
    private void doUpdate(MaterialUpdateDTO materialUpdateDTO, Material material) {
        material.setRemark(materialUpdateDTO.getRemark());
        material.setUnitId(materialUpdateDTO.getUnitId());
        material.setExtendUnitId(materialUpdateDTO.getExtendUnitId());
        materialMapper.updateById(material);
        // 删除原绑定信息
        materialFieldService.deleteByProductsId(materialUpdateDTO.getId());
        // 更新自定义字段信息
        materialFieldService.saveBasicProductsFields(materialUpdateDTO.getId(), materialUpdateDTO.getFieldList());
    }


    @Override
    public MaterialWithFieldDTO productsInfo(Long id) {
        Material material = materialMapper.selectById(id);
        if (Objects.isNull(material)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        MaterialWithFieldDTO materialWithFieldDTO = MaterialConvert.INSTANCE.convert2ProductsInfoVO(material);
        materialWithFieldDTO.setUnit(unitCache.getGlobalUnitName(material.getUnitId()));
        List<MaterialFieldDTO> fieldVOList = materialFieldService.getByProductsId(id);
        materialWithFieldDTO.setFieldList(fieldVOList);
        return materialWithFieldDTO;
    }

    @Override
    public List<MaterialDTO> selectByParam(MaterialParamDTO param) {
        return BeanUtil.copyToList(materialMapper.selectByParam(param), MaterialDTO.class);
    }

    @Override
    public List<MaterialDTO> productsEasyInfo(String keyword) {
        MaterialParamDTO param = new MaterialParamDTO();
        param.setKeyword(keyword);
        param.setSortOrder(BasicConstants.DEFAULT_SORTING_FIELD);
        List<Material> materials = materialMapper.selectByParam(param);
        return MaterialConvert.INSTANCE.convert2ProductsEasyVO(materials);
    }

    @Override
    @OperationLog
    @Transactional(rollbackFor = Exception.class)
    public void issueMaterialAndCategory(RemoteIssueFeignDTO dto) {
        log.info("开始物料洗下发 dto={}", JSON.toJSONString(dto));
        List<CategoryIssueDTO> categoryList = MaterialConvert.INSTANCE.convert2CategoryDTO(dto.getCategoryList());
        // 同步分类
        categoryService.issueCategory(categoryList, CategoryInfoTypeEnum.INSPECTION.getValue());
        List<Long> plateformCategoryIdList = categoryList.stream().map(CategoryIssueDTO::getId).collect(Collectors.toList());
        // 同步检品信息
        List<MaterialIssueDTO> materialList = MaterialConvert.INSTANCE.convert2MaterialIssueDTO(dto.getMaterialList());
        List<Material> neededIssueMaterials = needSyncInspection(materialList, plateformCategoryIdList, CategoryInfoTypeEnum.INSPECTION.getValue());
        if (CollectionUtil.isNotEmpty(neededIssueMaterials)) {
            materialMapper.insertBatch(neededIssueMaterials);
        }
        log.info("开始物料下发完成 dto={}", JSON.toJSONString(dto));
    }

    @Override
    public void enableInspectProduct(Long id) {
        Material basicProducts = materialMapper.selectById(id);
        if (Objects.isNull(basicProducts)) {
            throw new BmosException(LimsResponseCode.PRODUCTS_NOT_EXIST);
        }
        if (basicProducts.getStatus()) {
            throw new BmosException(LimsResponseCode.PRODUCT_STATUS_ALREADY_UPDATED);
        }
        basicProducts.setStatus(Boolean.TRUE);
        materialMapper.updateById(basicProducts);
    }

    @Override
    public void disableInspectProduct(Long id) {
        Material basicProducts = materialMapper.selectById(id);
        if (Objects.isNull(basicProducts)) {
            throw new BmosException(LimsResponseCode.PRODUCTS_NOT_EXIST);
        }
        if (!basicProducts.getStatus()) {
            throw new BmosException(LimsResponseCode.PRODUCT_STATUS_ALREADY_UPDATED);
        }
        // 1) 启用的检品已配置检验方案，不允许停用
        List<InspectionScheme> schemes = inspectionSchemeMapper.selectByMaterialIds(Collections.singletonList(id));
        if (CollectionUtil.isNotEmpty(schemes)) {
            throw new BmosException(LimsResponseCode.MATERIAL_EXISTED_SCHEME);
        }
        // 2) 启用物料作为所属物料被成员物料关联，不允许停用
        if (materialMapper.existsEnabledSubMaterialByPrincipalId(id)) {
            throw new BmosException(LimsResponseCode.MATERIAL_EXISTED_MEMBER_MATERIAL);
        }
        basicProducts.setStatus(Boolean.FALSE);
        materialMapper.updateById(basicProducts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionProduct(MaterialSaveDTO dto) {
        Long materialCategoryId = dto.getCategoryId();
        MaterialCategory category = materialCategoryMapper.selectById(materialCategoryId);
        if (ObjectUtil.isNull(category)) {
            throw new BmosException(LimsResponseCode.MATERIAL_CATEGORY_NOT_EXISTED);
        }
        if (BooleanUtil.isTrue(dto.getSubMaterial()) && ObjectUtil.isNull(dto.getPrincipalMaterialId())) {
            throw new BmosException(LimsResponseCode.SUB_MATERIAL_MUST_HAS_PRINCIPAL);
        }
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
//        String categoryMergeCode = materialCategory.getMergeCode();
//        String mergeCode = categoryMergeCode + dto.getCode();
        String mergeCode = dto.getCode();
        // 平台校验 如果平台有则需要从平台同步
        checkPlatformMaterialCodeExisted(mergeCode, null);
        // 平台保存
        ProductMaterialSaveDTO remoteMaterialDto = getRemoteMaterialDto(dto, category);
        ResponseInfo<Long> saveRes = FeignUtils.handleRequest(data -> platformMaterialFeignClient.saveMaterial(data), remoteMaterialDto);
        if (saveRes.isSuccess()) {
            // 本地保存
            Material productMaterial = MaterialConvert.INSTANCE.convertToProductMaterial(dto);
            productMaterial.setPlatformMaterialId(saveRes.getData());
            productMaterial.setMergeCode(mergeCode);
            materialMapper.insert(productMaterial);
            if (CollectionUtil.isNotEmpty(dto.getFieldList())) {
                materialFieldService.saveBasicProductsFields(productMaterial.getId(), dto.getFieldList());
            }
            return;
        }
        throw new BmosException(LimsResponseCode.PLATFORM_MATERIAL_REGISTER_ERROR, saveRes.getMessage());

    }
    private ProductMaterialSaveDTO getRemoteMaterialDto(MaterialSaveDTO dto, MaterialCategory category) {
        ProductMaterialSaveDTO remoteDTO = MaterialConvert.INSTANCE.convertToRemoteSaveDTO(dto);
        remoteDTO.setMaterialCategoryId(category.getPlatformCategoryId());
        remoteDTO.setBusinessRegister(true);
        // 传入业务信息
        String businessName = CategoryInfoTypeEnum.getNameByValue(category.getCategoryType());
        remoteDTO.setBusinessName(businessName);
        if (ObjectUtil.isNotNull(dto.getPrincipalMaterialId())) {
            Material productMaterial = materialMapper.selectById(dto.getPrincipalMaterialId());
            remoteDTO.setPrincipalMaterialId(productMaterial.getPlatformMaterialId());
        }
        return remoteDTO;
    }


    /**
     * 校验code在平台是否存在
     *
     * @param code
     */
    private void checkPlatformMaterialCodeExisted(String code, Long platformMaterialId) {
        ResponseInfo<Boolean> checkRes = FeignUtils.handleRequest((data) -> platformMaterialFeignClient.checkMergeCodeExisted(code, platformMaterialId), null);
        if (checkRes.isError()) {
            throw new BmosException(LimsResponseCode.PLATFORM_CHECK_CODE_ERROR, checkRes.getMessage());
        }
        if (checkRes.getData()) {
            throw new BmosException(LimsResponseCode.MATERIAL_EXISTED_IN_PLATFORM);
        }
    }

    @Override
    public List<MaterialDTO> getAllByCategoryId(Long productCategoryId) {
        List<Long> categoryIdList = categoryService.selectChildCategoryId(productCategoryId);
        return BeanUtil.copyToList(materialMapper.selectByCategoryIds(categoryIdList), MaterialDTO.class);
    }

    private ProductMaterialSaveDTO getPlatformSaveDTO(MaterialSaveDTO dto, MaterialCategoryDTO category) {
        ProductMaterialSaveDTO remoteDTO = MaterialConvert.INSTANCE.convertToRemoteSaveDTO(dto);
        remoteDTO.setMaterialCategoryId(category.getPlatformCategoryId());
        remoteDTO.setBusinessRegister(true);
        // 传入业务信息
        String businessName = CategoryInfoTypeEnum.getNameByValue(category.getCategoryType());
        remoteDTO.setBusinessName(businessName);
        if (ObjectUtil.isNotNull(dto.getPrincipalMaterialId())) {
            Material basicProducts = materialMapper.selectById(dto.getPrincipalMaterialId());
            remoteDTO.setPrincipalMaterialId(basicProducts.getPlatformMaterialId());
        }
        return remoteDTO;
    }

    /**
     * 需要进行同步的检品信息（剔除出已经在系统内的检品信息）
     *
     * @param materialList: 平台下发的物料信息
     * @param categoryType
     * @return
     */
    private List<Material> needSyncInspection(List<MaterialIssueDTO> materialList, List<Long> plateformCategoryIdList, Integer categoryType) {
        List<Material> basicProductsList = new ArrayList<>();
        if (CollectionUtil.isEmpty(materialList)) {
            return basicProductsList;
        }
        // 1. 查询当前业务分类下且物料信息存在的物料信息
        Map<Long, MaterialIssueDTO> platformMaterialMap = materialList.stream().collect(Collectors.toMap(MaterialIssueDTO::getId, Function.identity()));
        if (CollectionUtil.isEmpty(platformMaterialMap)) {
            return basicProductsList;
        }
        MaterialParamDTO productsParam = new MaterialParamDTO().setPlatformMaterialIdList(new ArrayList<>(platformMaterialMap.keySet()));

        List<Material> existProducts = materialMapper.selectByParam(productsParam);
        // 查询平台物料分类id的所有已同步的分类信息
        List<MaterialCategoryDTO> basicCategoryList = categoryService.selectByPlatformCategories(plateformCategoryIdList, categoryType);
        // key为平台物料分类id value为当前系统分类id
        Map<Long, Long> inspectionCategoryMap = basicCategoryList.stream().collect(Collectors.toMap(MaterialCategoryDTO::getPlatformCategoryId, MaterialCategoryDTO::getId));
        Set<Long> existPlatformMaterialIds = existProducts.stream().map(Material::getPlatformMaterialId).collect(Collectors.toSet());
        for (Long platformMaterialId : platformMaterialMap.keySet()) {
            if (existPlatformMaterialIds.contains(platformMaterialId)) {
                // 代表当前信息已同步
                continue;
            }
            Material products = convert2Products(platformMaterialMap.get(platformMaterialId), inspectionCategoryMap);
            products.setId(CustomIdGenerator.nextId());
            products.setStatus(false);
            basicProductsList.add(products);
        }
        return basicProductsList;
    }

    /**
     * 平台物料信息转换为检品DO
     *
     * @param materialIssueDTO：平台物料信息
     * @return
     */
    private Material convert2Products(MaterialIssueDTO materialIssueDTO, Map<Long, Long> idMap) {
        Material basicProducts = new Material();
        basicProducts.setCategoryId(idMap.get(materialIssueDTO.getMaterialCategoryId()));
        basicProducts.setExpandInfo(JSON.toJSONString(materialIssueDTO.getExpandInfo()));
        basicProducts.setPrincipalMaterialId(materialIssueDTO.getPrincipalMaterialId());
        basicProducts.setName(materialIssueDTO.getName());
        basicProducts.setCode(materialIssueDTO.getCode());
        basicProducts.setSpecification(materialIssueDTO.getSpecification());
        basicProducts.setUnitId(materialIssueDTO.getUnitId());
        basicProducts.setSubMaterial(materialIssueDTO.getSubMaterial());
        basicProducts.setStatus(materialIssueDTO.getStatus());
        basicProducts.setMergeCode(materialIssueDTO.getMergeCode());
        basicProducts.setPlatformMaterialId(materialIssueDTO.getId());
        return basicProducts;
    }

    @Override
    public List<MaterialDTO> getPrincipalList(MaterialPrincipalQueryDTO dto) {
        return materialMapper.selectPrincipalList(dto);
    }

    @Override
    public List<MaterialInspectTreeNodeDTO> getMaterialInspectTree(Integer categoryType) {
        // 1. 查询所有检品分类
        MaterialCategoryParamDTO categoryParam = new MaterialCategoryParamDTO();
        if (categoryType != null) {
            categoryParam.setCategoryType(categoryType);
        }
        categoryParam.setOrder("merge_code ASC");
        List<MaterialCategory> categories = materialCategoryMapper.selectByParam(categoryParam);
        
        // 2. 查询所有检品
        MaterialParamDTO materialParam = new MaterialParamDTO();
        if (categoryType != null) {
            materialParam.setCategoryType(categoryType);
        }
        materialParam.setStatus(Boolean.TRUE); // 只查询启用的检品
        List<Material> materials = materialMapper.selectByParam(materialParam);
        
        // 3. 转换为树节点
        List<MaterialInspectTreeNodeDTO> allNodes = new ArrayList<>();
        
        // 转换分类节点
        List<MaterialInspectTreeNodeDTO> categoryNodes = MaterialConvert.INSTANCE.convertCategoriesToTreeNodes(categories);
        allNodes.addAll(categoryNodes);
        
        // 转换检品节点
        List<MaterialInspectTreeNodeDTO> materialNodes = MaterialConvert.INSTANCE.convertMaterialsToTreeNodes(materials,unitCache);
        materialNodes.forEach(node -> {
            if (node.getUnitId()!=null){
                unitCache.getGlobalUnitName(node.getUnitId());
            }
        });
        allNodes.addAll(materialNodes);
        
        // 4. 构建树形结构
        return TreeUtil.buildTree(allNodes, false);
    }

    @Override
    public List<MaterialInspectTreeWithDocumentDTO> getMaterialInspectTreeWithDocument(Integer categoryType) {
        // 1. 查询所有检品分类
        MaterialCategoryParamDTO categoryParam = new MaterialCategoryParamDTO();
        if (categoryType != null) {
            categoryParam.setCategoryType(categoryType);
        }
        categoryParam.setOrder("merge_code ASC");
        List<MaterialCategory> categories = materialCategoryMapper.selectByParam(categoryParam);
        
        // 2. 查询绑定了启用请验单的检品
        List<Material> materialsWithDocument = getMaterialsWithEnabledDocument(categoryType);
        
        // 3. 转换为树节点
        List<MaterialInspectTreeWithDocumentDTO> allNodes = new ArrayList<>();
        
        // 转换分类节点
        List<MaterialInspectTreeWithDocumentDTO> categoryNodes = MaterialConvert.INSTANCE.convertCategoriesToTreeWithDocumentNodes(categories);
        allNodes.addAll(categoryNodes);
        
        // 转换检品节点（带请验单信息）
        for (Material material : materialsWithDocument) {
            // 获取检品绑定的启用请验单信息
            DocumentConfig documentConfig = getEnabledDocumentConfigByMaterialId(material.getId());
            MaterialInspectTreeWithDocumentDTO node = MaterialConvert.INSTANCE.convertMaterialToTreeWithDocumentNode(material, unitCache, documentConfig);
            if (node.getUnitId() != null) {
                node.setUnitName(unitCache.getGlobalUnitName(node.getUnitId()));
            }
            allNodes.add(node);
        }
        
        // 4. 构建树形结构
        return TreeUtil.buildTree(allNodes, false);
    }

    @Override
    public List<MaterialTreeWithSchemeNodeDTO> getMaterialTreeWithSchemes(Integer categoryType) {
        // 1. 查询分类
        MaterialCategoryParamDTO categoryParam = new MaterialCategoryParamDTO();
        if (categoryType != null) {
            categoryParam.setCategoryType(categoryType);
        }
        categoryParam.setOrder("merge_code ASC");
        List<MaterialCategory> categories = materialCategoryMapper.selectByParam(categoryParam);

        // 2. 查询启用的物料
        MaterialParamDTO materialParam = new MaterialParamDTO();
        if (categoryType != null) {
            materialParam.setCategoryType(categoryType);
        }
        materialParam.setStatus(Boolean.TRUE);
        List<Material> materials = materialMapper.selectByParam(materialParam);

        // 3. 批量查询物料关联的方案
        List<Long> materialIds = materials.stream().map(Material::getId).collect(Collectors.toList());
        Map<Long, List<InspectionScheme>> materialIdToSchemes = new HashMap<>();
        if (CollectionUtil.isNotEmpty(materialIds)) {
            List<InspectionScheme> schemes = inspectionSchemeMapper.selectByMaterialIds(materialIds);
            materialIdToSchemes = schemes.stream().collect(Collectors.groupingBy(InspectionScheme::getMaterialId));
        }

        // 4. 组装树节点
        List<MaterialTreeWithSchemeNodeDTO> nodes = new ArrayList<>();

        // 分类节点
        for (MaterialCategory category : categories) {
            MaterialTreeWithSchemeNodeDTO node = new MaterialTreeWithSchemeNodeDTO();
            node.setId(category.getId());
            node.setParentId(category.getParentId());
            node.setName(category.getName());
            node.setCode(category.getCode());
            node.setMergeCode(category.getMergeCode());
            node.setShowName(category.getMergeCode()+"-"+category.getName());
            node.setCategoryFlag(true);
            node.setNodeType(MaterialTreeWithSchemeNodeDTO.NodeType.CATEGORY);
            node.setLeaf(false);
            nodes.add(node);
        }

        // 物料节点 + 子方案
        for (Material material : materials) {
            MaterialTreeWithSchemeNodeDTO materialNode = new MaterialTreeWithSchemeNodeDTO();
            materialNode.setId(material.getId());
            materialNode.setParentId(material.getCategoryId());
            materialNode.setName(material.getName());
            materialNode.setCode(material.getCode());
            materialNode.setMergeCode(material.getMergeCode());
            materialNode.setShowName(material.getMergeCode()+"-"+material.getName());
            materialNode.setCategoryFlag(false);
            materialNode.setNodeType(MaterialTreeWithSchemeNodeDTO.NodeType.MATERIAL);
            materialNode.setMaterialCode(material.getCode());

            List<InspectionScheme> children = materialIdToSchemes.getOrDefault(material.getId(), Collections.emptyList());
            if (CollectionUtil.isNotEmpty(children)) {
                List<MaterialTreeWithSchemeNodeDTO> schemeNodes = new ArrayList<>();
                for (InspectionScheme scheme : children) {
                    MaterialTreeWithSchemeNodeDTO schemeNode = new MaterialTreeWithSchemeNodeDTO();
                    schemeNode.setId(scheme.getId());
                    schemeNode.setSchemeId(scheme.getId());
                    schemeNode.setParentId(material.getId());
                    schemeNode.setName(scheme.getName());
                    schemeNode.setNodeType(MaterialTreeWithSchemeNodeDTO.NodeType.SCHEME);
                    schemeNode.setCategoryFlag(false);
                    schemeNode.setLeaf(true);
                    schemeNode.setShowName(scheme.getName());
                    schemeNode.setSchemeVersion(scheme.getActiveVersionNo());
                    schemeNodes.add(schemeNode);
                }
                materialNode.setChildren(schemeNodes);
                materialNode.setLeaf(false);
            } else {
                materialNode.setLeaf(true);
            }
            nodes.add(materialNode);
        }

        // 5. 构建树
        return TreeUtil.buildTree(nodes, false);
    }
    
    /**
     * 查询绑定了启用请验单的检品列表
     */
    private List<Material> getMaterialsWithEnabledDocument(Integer categoryType) {
        // 通过SQL查询绑定了启用请验单的检品
        MaterialParamDTO materialParam = new MaterialParamDTO();
        if (categoryType != null) {
            materialParam.setCategoryType(categoryType);
        }
        materialParam.setStatus(Boolean.TRUE); // 只查询启用的检品
        materialParam.setHasEnabledDocument(Boolean.TRUE); // 只查询绑定了启用请验单的检品
        
        return materialMapper.selectByParam(materialParam);
    }
    
    /**
     * 根据检品ID获取其绑定的启用请验单配置
     */
    private DocumentConfig getEnabledDocumentConfigByMaterialId(Long materialId) {
        // 查询检品绑定的请验单配置
        List<DocumentConfigMaterial> bindings = documentConfigMaterialMapper.selectConfigsByProductId(materialId);
        if (CollUtil.isEmpty(bindings)) {
            return null;
        }
        
        // 获取请验单配置ID
        Long configId = bindings.get(0).getConfigId();
        DocumentConfig documentConfig = documentConfigMapper.selectById(configId);
        
        // 只返回启用状态的请验单配置
        if (documentConfig != null && Boolean.TRUE.equals(documentConfig.getStatus())) {
            return documentConfig;
        }
        
        return null;
    }
}
