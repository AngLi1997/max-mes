package com.bmos.mes.service.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.StateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.product.PlatformMaterialFeignClient;
import com.bmos.mes.service.platform.unit.PlatformUnitFeignClient;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.product.convert.ProductMaterialConverter;
import com.bmos.mes.service.product.dto.*;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.product.service.MaterialFieldService;
import com.bmos.mes.service.product.service.ProductMaterialCategoryService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.*;
import com.bmos.mes.service.record.service.BatchRecordProductService;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.unit.dto.RemoteQueryDTO;
import com.bmos.mes.service.unit.dto.UnitAndExtendDTO;
import com.bmos.mes.service.unit.vo.UnitExtendVO;
import com.bmos.mes.service.unit.vo.UnitVO;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import com.bmos.platform.facade.material.feign.PlatformMaterialFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductMaterialServiceImpl implements ProductMaterialService {

    @Autowired
    private ProductMaterialMapper materialMapper;

    @Autowired
    private PlatformMaterialFeignClient platformOpenFeign;

    @Resource
    private PlatformMaterialFeign materialFeign;

    @Autowired
    @Lazy
    private ProductMaterialCategoryService productMaterialCategoryService;

    @Autowired
    private PlatformUnitFeignClient platformUnitFeignClient;

    @Autowired
    private BatchRecordProductService batchRecordProductService;

    @Value("${spring.application.name}")
    private String platformName;

    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Autowired
    @Lazy
    private ProductFormulaConfigureService productFormulaService;

    @Autowired
    private MaterialFieldService materialFieldService;

    @Resource
    @Lazy
    private ProcessService processService;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void save(ProductMaterialSaveDTO dto) {
        Long materialCategoryId = dto.getMaterialCategoryId();
        ProductMaterialCategory category = productMaterialCategoryService.selectById(materialCategoryId);
        if (ObjectUtil.isNull(category)) {
            throw new BmosException(MesResponseCode.MATERIAL_CATEGORY_NOT_EXISTED);
        }
        if (BooleanUtil.isTrue(dto.getSubMaterial()) && ObjectUtil.isNull(dto.getPrincipalMaterialId())) {
            throw new BmosException(MesResponseCode.SUB_MATERIAL_MUST_HAS_PRINCIPAL);
        }
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
//        String categoryMergeCode = materialCategory.getMergeCode();
//        String mergeCode = categoryMergeCode + dto.getCode();
        String mergeCode = dto.getCode();
        // 平台校验 如果平台有则需要从平台同步
        checkPlatformMaterialCodeExisted(mergeCode, null);
        // 平台保存
        ProductMaterialSaveDTO remoteMaterialDto = getRemoteMaterialDto(dto, category);
        ResponseInfo<Long> saveRes = FeignUtils.handleRequest(data -> platformOpenFeign.saveMaterial(data), remoteMaterialDto);
        if (saveRes.isSuccess()) {
            // 本地保存
            ProductMaterial productMaterial = ProductMaterialConverter.INSTANCE.convertToProductMaterial(dto);
            productMaterial.setPlatformMaterialId(saveRes.getData());
            productMaterial.setMergeCode(mergeCode);
            materialMapper.insert(productMaterial);
            materialFieldService.saveMaterialFields(productMaterial.getId(), dto.getFieldSaveDTOList());
            return;
        }
        throw new BmosException(MesResponseCode.PLATFORM_MATERIAL_REGISTER_ERROR, saveRes.getMessage());
    }

    private ProductMaterialSaveDTO getRemoteMaterialDto(ProductMaterialSaveDTO dto, ProductMaterialCategory category) {
        ProductMaterialSaveDTO remoteDTO = ProductMaterialConverter.INSTANCE.convertToRemoteSaveDTO(dto);
        remoteDTO.setMaterialCategoryId(category.getPlatformCategoryId());
        remoteDTO.setBusinessRegister(true);
        // 传入业务信息
        String businessName = CategoryInfoTypeEnum.getNameByValue(category.getCategoryType());
        remoteDTO.setBusinessName(businessName);
        if (ObjectUtil.isNotNull(dto.getPrincipalMaterialId())) {
            ProductMaterial productMaterial = materialMapper.selectById(dto.getPrincipalMaterialId());
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
        ResponseInfo<Boolean> checkRes = FeignUtils.handleRequest((data) -> platformOpenFeign.checkMergeCodeExisted(code, platformMaterialId), null);
        if (checkRes.isError()) {
            throw new BmosException(MesResponseCode.PLATFORM_CHECK_CODE_ERROR, checkRes.getMessage());
        }
        if (checkRes.getData()) {
            throw new BmosException(MesResponseCode.MATERIAL_EXISTED_IN_PLATFORM);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void update(ProductMaterialUpdateDTO dto) {
        ProductMaterial productMaterial = materialMapper.selectById(dto.getId());
        if (ObjectUtil.isNull(productMaterial)) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        materialMapper.updateById(ProductMaterialConverter.INSTANCE.convertToProductMaterial(dto));
        // 先删除之前的绑定关系
        materialFieldService.deleteByMaterialId(productMaterial.getId());
        // 重新进行物料自定义字段绑定
        materialFieldService.saveMaterialFields(productMaterial.getId(), dto.getFieldSaveDTOList());
        // 如果修改了临期日期，重置批次的临期提醒标志
        if (ObjectUtil.notEqual(dto.getDyingPeriod(),productMaterial.getDyingPeriod())){
            storageMaterialBatchMapper.resetExpireWarningFlag(productMaterial.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void delete(Long id) {
        // 校验启停状态
        ProductMaterial productMaterial = materialMapper.selectAllInfoById(id);
        if (ObjectUtil.isNull(productMaterial)) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        if (productMaterial.getStatus()) {
            throw new BmosException(MesResponseCode.MATERIAL_ENABLED);
        }
        // 校验是否存在过物料批次
        Boolean existedBatch = storageMaterialBatchService.checkExistedBatchByMaterialId(productMaterial.getId());
        if (existedBatch) {
            if (ObjectUtil.equal(productMaterial.getCategoryType(), CategoryInfoTypeEnum.RAW_MATERIAL.getValue())) {
                throw new BmosException(MesResponseCode.ORIGINAL_MATERIAL_EXISTED_BATCH);
            }
            throw new BmosException(MesResponseCode.MIDDLE_MATERIAL_EXISTED_BATCH);
        }
        // 删除后取消向平台注册
        ProductMaterialCategory category = productMaterialCategoryService.selectById(productMaterial.getMaterialCategoryId());
        Long platformMaterialId = productMaterial.getPlatformMaterialId();
        UnregisterMaterialDTO dto = new UnregisterMaterialDTO(platformName, category.getCategoryType(), platformMaterialId);
        try {
            platformOpenFeign.unregisterMaterial(dto);
        } catch (Exception e) {
            throw new BmosException(BaseResponseCode.FEIGN_REMOTE_CALL_ERROR);
        }
        // 校验是否产生货位日志
        materialMapper.deleteById(id);
        // 删除生产物料的自定义字段
        materialFieldService.deleteByMaterialId(id);
    }

    @Override
    public CommonPage<ProductMaterialPageVO> getPage(ProductMaterialPageQueryDTO dto) {
        // 分页展示分类及所有子分类的物料数据
        List<Long> categoryIds = productMaterialCategoryService.getAllChildCategory(dto.getMaterialCategoryId());
        dto.setMaterialCategoryIds(categoryIds);
        List<ProductMaterialPageVO> productMaterialPageVOS = materialMapper.selectPageList(dto);
        RemoteQueryDTO remoteQueryDTO = getRemoteQueryDTO(productMaterialPageVOS);
        ResponseInfo<UnitAndExtendDTO> res;
        try {
            res = platformUnitFeignClient.getUnitAndExtend(remoteQueryDTO);
        } catch (Exception e) {
            log.error("从平台获取单位feign调用异常:" + e.getCause() + e.getMessage());
            throw new BmosException(MesResponseCode.PLATFORM_GET_UNIT_ERROR);
        }
        if (res.isSuccess()) {
            UnitAndExtendDTO data = res.getData();
            handleMaterialUnits(data, productMaterialPageVOS);
            return CommonPage.convertPage(productMaterialPageVOS);
        }
        throw new BmosException(MesResponseCode.PLATFORM_GET_UNIT_ERROR, res.getMessage());
    }

    private void handleMaterialUnits(UnitAndExtendDTO data, List<ProductMaterialPageVO> productMaterialPageVOS) {
        List<UnitVO> units = data.getUnits();
        Map<Long, UnitVO> unitMap = CollectionUtils.convertMap(units, UnitVO::getId);
        List<UnitExtendVO> unitExtends = data.getUnitExtends();
        Map<Long, UnitExtendVO> extendMap = CollectionUtils.convertMap(unitExtends, UnitExtendVO::getId);
        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectList();
        Map<Long, ProductMaterialCategory> categoryIdMap = CollectionUtils.convertMap(categories, ProductMaterialCategory::getId);
        buildCategoryFullName(categories, categoryIdMap);
        for (ProductMaterialPageVO productMaterialPageVO : productMaterialPageVOS) {
            UnitVO unitVO = unitMap.get(productMaterialPageVO.getUnitId());
            String unitName = ObjectUtil.isNull(unitVO) ? StrUtil.EMPTY : unitVO.getUnitName();
            productMaterialPageVO.setUnitName(unitName);
            UnitExtendVO unitExtendVO = extendMap.get(productMaterialPageVO.getUnitExtendId());
            String extendName = ObjectUtil.isNull(unitExtendVO) ? StrUtil.EMPTY : unitExtendVO.getExtendUnitName();
            productMaterialPageVO.setUnitExtendName(extendName);
            productMaterialPageVO.setFullCategoryName(categoryIdMap.get(productMaterialPageVO.getMaterialCategoryId()).getName());
        }
    }

    private void buildCategoryFullName(List<ProductMaterialCategory> categories, Map<Long, ProductMaterialCategory> categoryIdMap) {
        CollUtil.sort(categories, Comparator.comparing(BaseDO::getCreateTime));
        for (ProductMaterialCategory category : categories) {
            Long parentId = category.getParentId();
            if (!ObjectUtil.equal(parentId, TreeUtil.parentId)) {
                ProductMaterialCategory parent = categoryIdMap.get(parentId);
                category.setName(parent.getName() + "/" + category.getName());
            }
        }
    }

    private static RemoteQueryDTO getRemoteQueryDTO(List<ProductMaterialPageVO> productMaterialPageVOS) {
        List<Long> unitIds = CollectionUtils.convertList(productMaterialPageVOS, ProductMaterialPageVO::getUnitId);
        List<Long> extendUnitIds = CollectionUtils.convertList(productMaterialPageVOS, ProductMaterialPageVO::getUnitExtendId);
        RemoteQueryDTO remoteQueryDTO = new RemoteQueryDTO();
        remoteQueryDTO.setUnitIds(unitIds);
        remoteQueryDTO.setUnitExtendIds(extendUnitIds);
        return remoteQueryDTO;
    }

    @Override
    public ProductMaterialDetailVO getDetail(Long id) {
        ProductMaterial productMaterial = materialMapper.selectById(id);
        ProductMaterialDetailVO detailVO = ProductMaterialConverter.INSTANCE.ConvertToDetail(productMaterial);
        MaterialExpandInfo expandInfo = productMaterial.getExpandInfo();
        MaterialExpandInfoVO expandInfoVO = ProductMaterialConverter.INSTANCE.convertToExpandVO(expandInfo);
        if (ObjectUtil.isNotNull(expandInfoVO)) {
            expandInfoVO.setPresetTareWeight(ObjectUtil.isNotNull(expandInfo.getPresetTareWeight()) ? expandInfo.getPresetTareWeight().toPlainString() : StrUtil.EMPTY);
            detailVO.setExpandInfo(expandInfoVO);
        }
        RemoteQueryDTO remoteQueryDTO = new RemoteQueryDTO();
        remoteQueryDTO.setUnitIds(Collections.singletonList(detailVO.getUnitId()));
        remoteQueryDTO.setUnitExtendIds(Collections.singletonList(detailVO.getUnitExtendId()));
        ResponseInfo<UnitAndExtendDTO> res = FeignUtils.handleRequest(data -> platformUnitFeignClient.getUnitAndExtend(data), remoteQueryDTO);
        if (res.isSuccess()) {
            UnitAndExtendDTO data = res.getData();
            List<UnitVO> units = data.getUnits();
            UnitVO unit = CollectionUtils.getFirst(units);
            List<UnitExtendVO> unitExtends = data.getUnitExtends();
            UnitExtendVO extend = CollectionUtils.getFirst(unitExtends);
            detailVO.setUnitName(ObjectUtil.isNull(unit) ? StrUtil.EMPTY : unit.getUnitName());
            detailVO.setUnitExtendName(ObjectUtil.isNull(extend) ? StrUtil.EMPTY : extend.getExtendUnitName());
            return detailVO;
        }
        throw new BmosException(MesResponseCode.PLATFORM_GET_UNIT_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void changeStatus(ProductMaterialChangeStatusDTO dto) {
        if (BooleanUtil.isFalse(dto.getStatus())) {
            ProductMaterial productMaterial = materialMapper.selectAllInfoById(dto.getId());
            // 是否关联成员物料
            if (materialMapper.existedMemberMaterial(dto.getId())) {
                if (ObjectUtil.equal(productMaterial.getCategoryType(), CategoryInfoTypeEnum.PRODUCTION.getValue())) {
                    throw new BmosException(MesResponseCode.MATERIAL_EXISTED_MEMBER_MATERIAL);
                }
                throw new BmosException(MesResponseCode.MATERIAL_EXISTED_MEMBER_MATERIAL);
            }
            if (ObjectUtil.equal(productMaterial.getCategoryType(), CategoryInfoTypeEnum.PRODUCTION.getValue())) {
                // 产品校验是否已有配方
                if (productFormulaService.existedProductFormula(productMaterial.getId())) {
                    throw new BmosException(MesResponseCode.PRODUCT_HAS_BOUND_FORMULA);
                }
            } else {
                // 物料校验是否已在配方中使用
                if (productFormulaService.existedFormulaMaterial(productMaterial.getId())) {
                    throw new BmosException(MesResponseCode.MATERIAL_BOUND_PRODUCT_FORMULA,
                            CategoryInfoTypeEnum.getNameByValue(productMaterial.getCategoryType()));
                }
            }

        }
        materialMapper.updateStatus(dto);
    }

    @Override
    public boolean existsCategoryMaterial(Long id) {
        return materialMapper.existsCategory(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueMaterialAndCategory(RemoteIssueDTO dto) {
        List<Integer> businesses = dto.getBusinesses();
        // 处理分类下发
        productMaterialCategoryService.issueCategory(dto.getCategoryList(), businesses);
        if (CollUtil.isEmpty(dto.getMaterialList())) {
            return;
        }
        // 物料下发前置处理 获取需要新增的物料
        List<ProductMaterial> neededIssueMaterials = getNeededIssueMaterials(dto.getMaterialList(), dto.getBusinesses());
        materialMapper.insertBatch(neededIssueMaterials);
    }

    @Override
    public List<MaterialTreeNodeVO> getSyncTree(SyncTreeQueryDTO queryDTO) {
        ResponseInfo<List<MaterialTreeNodeVO>> responseInfo = FeignUtils.handleRequest((d) -> materialFeign.getMaterialTree(), null);
        List<MaterialTreeNodeVO> data = responseInfo.getData();
        List<Long> platformIds = data.stream()
                .filter(vo -> !vo.getCategoryFlag()).map(MaterialTreeNodeVO::getId).collect(Collectors.toList());
        List<ProductMaterial> existed = materialMapper.selectByPlatformMaterialIdsAndType(platformIds, queryDTO.getCategoryType());
        Set<Long> longs = CollectionUtils.convertSet(existed, ProductMaterial::getPlatformMaterialId);
        // 过滤已存在的物料
        recRemoveIfExisted(data, longs);
        return data;
    }

    private void recRemoveIfExisted(List<MaterialTreeNodeVO> treeNodes, Set<Long> existedIds) {
        treeNodes.removeIf(node -> existedIds.contains(node.getId()));
        treeNodes.forEach(node -> {
            if (node.getCategoryFlag() && CollUtil.isNotEmpty(node.getChildren())) {
                recRemoveIfExisted(node.getChildren(), existedIds);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void syncMaterialAndCategory(SyncMaterialInfoDTO dto) {
        MaterialIssueRequestDTO remoteDTO = new MaterialIssueRequestDTO();
        remoteDTO.setMaterialIds(dto.getMaterialIds());
        remoteDTO.setMaterialCategoryIds(dto.getMaterialCategoryIds());
        MaterialIssueBusinessDTO materialIssueBusinessDTO = new MaterialIssueBusinessDTO();
        materialIssueBusinessDTO.setChildCodeList(Collections.singletonList(dto.getCategoryType()));
        materialIssueBusinessDTO.setPlatformName(platformName);
        remoteDTO.setBusinesses(Collections.singletonList(materialIssueBusinessDTO));
        FeignUtils.handleRequest(data -> platformOpenFeign.issueMaterialAndCategory(data), remoteDTO);

        // todo 修改为业务新增后向平台注册
//        ResponseInfo<RemoteSyncDTO> res = FeignUtils.handleRequest(data -> platformOpenFeign.getSyncMaterialInfo(data), dto);
//        RemoteSyncDTO data = res.getData();
//        Integer categoryType = dto.getCategoryType();
//        // 分类同步
//        List<CategoryIssueDTO> categoryList = data.getCategoryList();
//        productMaterialCategoryService.issueCategory(categoryList, Collections.singletonList(categoryType));
//        // 物料同步
//        if (CollectionUtils.isAnyEmpty(data.getMaterialList()))
//            return;
//        List<ProductMaterial> neededIssueMaterials = getNeededIssueMaterials(data.getMaterialList(), Collections.singletonList(dto.getCategoryType()));
//        materialMapper.insertBatch(neededIssueMaterials);
    }

    @Override
    public List<PrincipalMaterialVO> getPrincipalList(MaterialPrincipalQueryDTO dto) {
        return ProductMaterialConverter.INSTANCE.convertPrincipalList(materialMapper.selectPrincipalList(dto));
    }

    @Override
    public List<ProductListVO> getProductList(Integer categoryType) {
        return materialMapper.selectEnabledByType(categoryType);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getProductTree(Integer categoryType) {
        List<ProductCategoryTreeNodeVO> productVOS = materialMapper.selectEnabledTreeNodeByType(categoryType);
        if (CollUtil.isNotEmpty(productVOS)){
            productVOS.forEach(node -> {
                node.setCategoryFlag(false);
            });
        }
//        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectList();
        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectListByType(categoryType);
        List<ProductCategoryTreeNodeVO> categoryNodes = ProductMaterialConverter.INSTANCE.convertToTreeNodeVO(categories);
        categoryNodes.forEach(categoryNode -> {
            categoryNode.setCategoryFlag(true);
        });
        categoryNodes.addAll(productVOS);
        categoryNodes.forEach(node -> {
            node.setShowName(node.getMergeCode() + StrUtil.DASHED + node.getName());
        });
        return TreeUtil.buildTree(categoryNodes, false);
    }

    @Override
    public List<SyncTreeNodeVO> getSyncTreeAll() {
        ResponseInfo<List<SyncTreeNodeVO>> res;
        try {
            res = platformOpenFeign.getSyncTreeAll();
        } catch (Exception e) {
            log.error("从平台获取同步全量树feign调用异常:" + e.getCause() + e.getMessage());
            throw new BmosException(MesResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        if (res.isSuccess()) {
            return res.getData();
        }
        throw new BmosException(MesResponseCode.PLATFORM_GET_SYNC_ERROR);
    }

    @Override
    @OperationLog
    public void bindBatchRecords(RecordSaveDTO dto) {
        batchRecordProductService.bindBatchRecords(dto);
    }

    @Override
    public List<Long> getProductBindRecordIds(Long productId) {
        return batchRecordProductService.getProductBindRecordIds(productId);
    }

    @Override
    public List<Long> getAllChildCategory(Long parentId) {
        return productMaterialCategoryService.getAllChildCategory(parentId);
    }

    @Override
    public List<ProductMaterial> getListByTypeAndIds(CategoryInfoTypeEnum category, Set<Long> ids) {
        return materialMapper.selectByIdsAndType(ids, category.getValue());
    }

    @Override
    public List<ProductListVO> getFinishProductList(Integer categoryType) {
        return materialMapper.getFinishProductListVO(categoryType);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getaLLProductTree(List<Integer> types) {
        List<ProductCategoryTreeNodeVO> productVOS = materialMapper.selectEnabledTreeNodeByTypes(types);
        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectListByTypes(types);
        List<ProductCategoryTreeNodeVO> categoryNodes = ProductMaterialConverter.INSTANCE.convertToTreeNodeVO(categories);
        categoryNodes.forEach(categoryNode -> {
            categoryNode.setCategoryFlag(true);
        });
        categoryNodes.addAll(productVOS);
        categoryNodes.forEach(node -> {
            node.setShowName(node.getMergeCode() + StrUtil.DASHED + node.getName());
        });
        return TreeUtil.buildTree(categoryNodes, false);

    }

    @Override
    public List<Long> getProductIdList(CategoryInfoTypeEnum categoryInfoType, Long categoryId, Boolean finished) {
        List<Long> allChildCategory = productMaterialCategoryService.getAllChildCategory(categoryInfoType, categoryId);
        return materialMapper.selectEnabledIdListByCategoryIds(allChildCategory, finished);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getFinishProductTree(FinishProductTreeQueryDTO dto) {
        List<ProductCategoryTreeNodeVO> finishList = materialMapper.selectEnabledFinishProductTreeNodeByType(dto);
        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectListByType(dto.getCategoryType());
        List<ProductCategoryTreeNodeVO> categoryTreeNodes = ProductMaterialConverter.INSTANCE.convertToTreeNodeVO(categories);
        categoryTreeNodes.forEach(node -> {
            node.setCategoryFlag(true);
        });
        categoryTreeNodes.addAll(finishList);
        categoryTreeNodes.forEach(node -> {
            node.setShowName(node.getMergeCode() + StrUtil.DASHED + node.getName());
        });
        return TreeUtil.buildTree(categoryTreeNodes, false);
    }

    @Override
    public ProductMaterial selectById(Long productId) {
        return materialMapper.selectAllInfoById(productId);
    }

    @Override
    public List<ProductMaterial> getByIds(Collection<Long> ids) {
        return materialMapper.selectListByBatchIds(ids);
    }

    @Override
    public List<Long> getIdListByCategoryIdList(List<Long> categoryIdList) {
        List<ProductMaterial> list = materialMapper.getIdListByCategoryIdList(categoryIdList);
        if (CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return list.stream().map(ProductMaterial::getId).collect(Collectors.toList());
    }

    @Override
    public List<ProductMaterial> getSubMaterial(Long materialId) {
        return materialMapper.selectSubMaterialById(materialId);
    }

    /**
     * 获取物料的成员物料列表
     * @param materialIds
     * @return
     */
    @Override
    public List<ProductMaterial> getSubMaterialByIdList(Collection<Long> materialIds) {
        return materialMapper.selectSubMaterialByIds(materialIds);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> queryTreeNodeByCategoryTypeAndProcessId(Integer categoryType, List<Long> processIds) {
        if (CollUtil.isEmpty(processIds)){
            return new ArrayList<>();
        }
        // 查询工艺下绑定的所有产品Id
        Set<Long> productIdSet = processService.getByIdList(processIds);
        if (CollUtil.isEmpty(productIdSet)){
            return new ArrayList<>();
        }
        List<ProductCategoryTreeNodeVO> productTree = this.getProductTree(categoryType);
        if (CollUtil.isEmpty(productTree)){
            return new ArrayList<>();
        }
        // 先设置一个顶层节点
        ProductCategoryTreeNodeVO root = new ProductCategoryTreeNodeVO();
        root.setChildren(productTree);
        // 剔除没有产品的分类
        weedOutProductTree(root, productIdSet);
        return root.getChildren();
    }

    @Override
    public List<ProcessProductVO> getByProcessIds(Collection<Long> processIds) {
        if (CollUtil.isEmpty(processIds)) {
            return new ArrayList<>();
        }
        return materialMapper.selectByProcessIdList(processIds);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getProductListCondition(Long productId, Long productCategoryId) {
        List<ProductCategoryTreeNodeVO> productTree = this.getProductTree(CategoryInfoTypeEnum.PRODUCTION.getValue());
        List<ProductCategoryTreeNodeVO> productList = new ArrayList<>();
        if (productId != null) {
            ProductCategoryTreeNodeVO product = findTree(productTree, productId);
            productList.add(product);
        } else if (productCategoryId != null) {
            ProductCategoryTreeNodeVO categoryTree = findTree(productTree, productCategoryId);
            productList = getAllChildren(Collections.singletonList(categoryTree));
        } else {
            return null;
        }
        return productList;
    }

    private ProductCategoryTreeNodeVO findTree(List<ProductCategoryTreeNodeVO> tree, Long id) {
        for (ProductCategoryTreeNodeVO node : tree) {
            if (Objects.equals(node.getId(), id)) {
                return node;
            }
            ProductCategoryTreeNodeVO findTree = findTree(node.getChildren(), id);
            if (Objects.nonNull(findTree)) {
                return findTree;
            }
        }
        return null;
    }

    private List<ProductCategoryTreeNodeVO> getAllChildren(List<ProductCategoryTreeNodeVO> tree) {
        List<ProductCategoryTreeNodeVO> children = tree.stream()
                .flatMap(node -> CollectionUtil.isNotEmpty(node.getChildren())
                        ? getAllChildren(node.getChildren()).stream()
                        : node.getChildren().stream())
                .collect(Collectors.toList());
        children.addAll(tree);
        return children;
    }

    /**
     * 获取需要新增的物料列表
     */
    private List<ProductMaterial> getNeededIssueMaterials(List<MaterialIssueDTO> materialList, List<Integer> businesses) {
        List<Long> materialsIds = CollectionUtils.convertList(materialList, MaterialIssueDTO::getId);
        List<ProductMaterial> dbMaterials = materialMapper.selectByPlatformMaterialIdsAndTypes(materialsIds, businesses);
        Map<Integer, List<ProductMaterial>> businessMaterialMap = dbMaterials.stream()
                .collect(Collectors.groupingBy(ProductMaterial::getCategoryType));
        List<ProductMaterial> allNewMaterials = new ArrayList<>();
        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectList();
        Map<Integer, List<ProductMaterialCategory>> typeMap = categories.stream().collect(Collectors.groupingBy(ProductMaterialCategory::getCategoryType));
        for (Integer business : businesses) {
            List<ProductMaterial> currentDbMaterials = businessMaterialMap.get(business);
            List<ProductMaterialCategory> businessCategory = typeMap.get(business);
            Set<Long> longs = CollectionUtils.convertSet(currentDbMaterials, ProductMaterial::getPlatformMaterialId);
            // 获取需要新增的物料
            List<ProductMaterial> newMaterials = filterNeedInsert(longs, materialList, businessCategory);
            // 处理所属物料关联关系
            // 本地已有的物料 id与平台关联id map
            Map<Long, Long> platformIdMap = CollectionUtils.convertMap(currentDbMaterials, ProductMaterial::getPlatformMaterialId, ProductMaterial::getId);
            // 新增的物料 id与平台关联id map
            Map<Long, Long> newPlatformIdMap = CollectionUtils.convertMap(newMaterials, ProductMaterial::getPlatformMaterialId, ProductMaterial::getId);
            for (ProductMaterial productMaterial : newMaterials) {
                if (productMaterial.getSubMaterial()) {
                    Long platformPrincipalId = productMaterial.getPrincipalMaterialId();
                    Long newPrincipalId = ObjectUtil.isNull(platformIdMap.get(platformPrincipalId)) ? newPlatformIdMap.get(platformPrincipalId) : platformIdMap.get(platformPrincipalId);
                    productMaterial.setPrincipalMaterialId(newPrincipalId);
                }
            }
            allNewMaterials.addAll(newMaterials);
        }
        return allNewMaterials;
    }

    /**
     * 过滤出需要新增的物料
     *
     * @param longs            已有的id
     * @param materialList     下发的物料列表
     * @param businessCategory 当前业务类型下的分类列表
     * @return
     */
    private static List<ProductMaterial> filterNeedInsert(Set<Long> longs, List<MaterialIssueDTO> materialList, List<ProductMaterialCategory> businessCategory) {
        List<MaterialIssueDTO> collect;
        collect = materialList;
        if (CollUtil.isNotEmpty(longs)) {
            collect = materialList.stream().filter(m -> !longs.contains(m.getId())).collect(Collectors.toList());
        }
        Map<Long, Long> idMap = CollectionUtils.convertMap(businessCategory, ProductMaterialCategory::getPlatformCategoryId, ProductMaterialCategory::getId);
        List<ProductMaterial> newMaterials = collect.stream().map(m -> {
            ProductMaterial productMaterial = ProductMaterialConverter.INSTANCE.convertToProductMaterial(m);
            productMaterial.setId(CustomIdGenerator.nextId());
            productMaterial.setPlatformMaterialId(m.getId());
            if (!CategoryInfoTypeEnum.PRODUCTION.getValue().equals(m.getCategoryType())){
                productMaterial.setDyingPeriod(m.getDyingPeriod());
            }
            productMaterial.setStatus(StateEnum.OFF.getValue());
            productMaterial.setMaterialCategoryId(idMap.get(productMaterial.getMaterialCategoryId()));
            return productMaterial;
        }).collect(Collectors.toList());
        return newMaterials;
    }

    /**
     * 去除没有产品的分类
     * @param root 虚拟的顶层节点
     * @param productIdSet
     */
    private void weedOutProductTree(ProductCategoryTreeNodeVO root, Set<Long> productIdSet) {
        List<ProductCategoryTreeNodeVO> children = new ArrayList<>();
        for (ProductCategoryTreeNodeVO productCategoryTreeNodeVO : root.getChildren()) {
            if (CollUtil.isNotEmpty(productCategoryTreeNodeVO.getChildren())){
                weedOutProductTree(productCategoryTreeNodeVO, productIdSet);
            }
            // 没有孩子节点
            if (CollUtil.isNotEmpty(productCategoryTreeNodeVO.getChildren())){
                children.add(productCategoryTreeNodeVO);
            }
            if (productIdSet.contains(productCategoryTreeNodeVO.getId()) && !productCategoryTreeNodeVO.getCategoryFlag()){
                children.add(productCategoryTreeNodeVO);
            }
        }
        root.setChildren(children);
    }
}
