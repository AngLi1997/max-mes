package com.bmos.wms.service.cargo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.service.cargo.convert.CargoConvert;
import com.bmos.wms.service.cargo.dto.*;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.cargo.model.CargoCategory;
import com.bmos.wms.service.cargo.service.ICargoCategoryService;
import com.bmos.wms.service.cargo.service.ICargoService;
import com.bmos.wms.service.cargo.vo.CargoPageVO;
import com.bmos.wms.service.cargo.vo.CargoVO;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.platform.material.dto.*;
import com.bmos.wms.service.platform.material.feign.PlatformMaterialFeignClient;
import com.bmos.wms.service.platform.material.vo.SyncTreeNodeVO;
import com.bmos.wms.service.platform.user.FeignUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.message.DescribeProducersRequestData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bmos.wms.common.enums.inventory.CategoryInfoTypeEnum.CARGO;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 15:06
 */
@Service
@Slf4j
public class CargoServiceImpl implements ICargoService {

    private static final String LOG_PREFIX = "[货品管理]";

    @Resource
    private PlatformMaterialFeignClient platformMaterialFeignClient;

    @Resource
    private ICargoCategoryService cargoCategoryService;

    @Resource
    private ICargoMapper cargoMapper;

    @Resource
    private UnitCache unitCache;

    @Value("${spring.application.name}")
    private String platformName;

    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CargoCreateDTO dto) {
        log.info("{}创建货品:{}", LOG_PREFIX, dto);
        CargoCategory cargoCategory = cargoCategoryService.selectById(dto.getCargoCategoryId());
        if (cargoCategory == null) {
            throw new BmosException(WmsResponseCode.CARGO_CATEGORY_NOT_EXIST);
        }
        if (dto.getIsMember()) {
            if (dto.getSubMaterialId() == null) {
                throw new ValidationException("所属物料id不能为空");
            } else {
                Cargo cargo = cargoMapper.selectById(dto.getSubMaterialId());
                if (cargo == null) {
                    throw new BmosException(WmsResponseCode.SUB_MATERIAL_NOT_EXIST);
                }
            }
        }
        String mergeCode = cargoCategory.getCargoCategoryMergeCode() + dto.getCargoCode();
        // 检查平台物合并料编码是否已存在
        checkPlatformMaterialCodeExisted(mergeCode);
        // 保存到平台
        ProductMaterialSaveDTO remoteDTO = new ProductMaterialSaveDTO();
        remoteDTO.setMaterialCategoryId(cargoCategory.getPlatformCategoryId());
        remoteDTO.setName(dto.getCargoName());
        remoteDTO.setCode(dto.getCargoCode());
        remoteDTO.setSpecification(dto.getSpecification());
        CacheUnit globalUnit = unitCache.getGlobalUnit(dto.getUnitId());
        if (globalUnit != null) {
            if (globalUnit.getExtend()) {
                remoteDTO.setUnitExtendId(globalUnit.getUnitId());
                remoteDTO.setUnitId(globalUnit.getParentUnitId());
            } else {
                remoteDTO.setUnitId(dto.getUnitId());
            }
        }
        remoteDTO.setSubMaterial(dto.getIsMember());
        remoteDTO.setRemark(dto.getRemark());
        remoteDTO.setBusinessRegister(true);
        remoteDTO.setBusinessName(CARGO.getName());
        if (dto.getIsMember()) {
            if (dto.getSubMaterialId() == null) {
                throw new ValidationException("");
            }
            Cargo sub = cargoMapper.selectById(dto.getSubMaterialId());
            if (sub == null) {
                throw new BmosException(WmsResponseCode.SUB_MATERIAL_NOT_EXIST);
            }
            remoteDTO.setPrincipalMaterialId(sub.getPlatformMaterialId());
        }
        Long platformMaterialId = FeignUtils.handleRequest(data -> platformMaterialFeignClient.saveMaterial(data), remoteDTO).getData();
        if (platformMaterialId == null) {
            throw new BmosException(WmsResponseCode.PLATFORM_MATERIAL_SAVE_FAIL);
        }

        // 平台新增分类成功后 保存本地
        Cargo cargo = new Cargo();
        cargo.setCargoCategoryId(dto.getCargoCategoryId());
        cargo.setCargoName(dto.getCargoName());
        cargo.setCargoCode(dto.getCargoCode());
        cargo.setMergeCode(mergeCode);
        cargo.setSpecification(dto.getSpecification());
        cargo.setUnitId(dto.getUnitId());
        cargo.setIsMember(dto.getIsMember());
        cargo.setSubMaterialId(dto.getSubMaterialId());
        cargo.setSingleQuantity(dto.getSingleQuantity());
        cargo.setSupplier(dto.getSupplier());
        cargo.setProducer(dto.getProducer());
        cargo.setRemark(dto.getRemark());
        cargo.setPlatformMaterialId(platformMaterialId);
        // 默认停用
        cargo.setEnable(false);
        cargoMapper.insert(cargo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(CargoEditDTO dto) {
        log.info("{}编辑货品:{}", LOG_PREFIX, dto);
        Cargo cargo = cargoMapper.selectById(dto.getId());
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        cargo.setUnitId(dto.getUnitId());
        cargo.setSingleQuantity(dto.getSingleQuantity());
        cargo.setSupplier(dto.getSupplier());
        cargo.setProducer(dto.getProducer());
        cargo.setRemark(dto.getRemark());
        cargoMapper.updateById(cargo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        log.info("{}启用货品:{}", LOG_PREFIX, id);
        Cargo cargo = cargoMapper.selectById(id);
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        if (cargo.getEnable()) {
            throw new BmosException(WmsResponseCode.CARGO_ALREADY_ENABLED);
        }
        cargo.setEnable(true);
        cargoMapper.updateById(cargo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        log.info("{}停用货品:{}", LOG_PREFIX, id);
        Cargo cargo = cargoMapper.selectById(id);
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        if (!cargo.getEnable()) {
            throw new BmosException(WmsResponseCode.CARGO_ALREADY_DISABLED);
        }
        // 查询是否被成员物料关联
        List<Cargo> subMaterialList = cargoMapper.selectBySubMaterialId(id);
        if (CollectionUtil.isNotEmpty(subMaterialList)) {
            throw new BmosException(WmsResponseCode.CARGO_HAS_SUB_MATERIAL_CAN_NOT_DISABLE);
        }
        cargo.setEnable(false);
        cargoMapper.updateById(cargo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("{}删除货品:{}", LOG_PREFIX, id);
        Cargo cargo = cargoMapper.selectById(id);
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        if (cargo.getEnable()) {
            throw new BmosException(WmsResponseCode.CARGO_ALREADY_ENABLED);
        }

        // 若该货品信息存在物料批次（不区分物料批次状态），无法删除该货品信息，提示货品信息已产生物料批次，无法删除
        List<InventoryBatch> batchList = inventoryBatchMapper.selectByCargoId(id);
        if (CollectionUtil.isNotEmpty(batchList)){
            throw new BmosException(WmsResponseCode.CARGO_HAS_BATCH_CAN_NOT_DELETE);
        }
        cargoMapper.deleteById(id);
        // 向平台取消注册
        FeignUtils.handleRequest(data -> platformMaterialFeignClient.unregisterMaterial(data), new UnregisterMaterialDTO(platformName, CARGO.getValue(), cargo.getPlatformMaterialId()));
    }

    @Nullable
    @Override
    public CargoVO queryInfoById(Long id) {
        if (id == null) {
            return null;
        }
        Cargo cargo = cargoMapper.selectById(id);
        if (cargo == null) {
            return null;
        }
        CargoVO cargoVO = CargoConvert.INSTANCE.convertToVO(cargo);
        if (Optional.ofNullable(cargoVO.getUnitId())
                .map(unitCache::getGlobalUnit)
                .map(CacheUnit::getExtend)
                .orElse(false)){
            CacheUnit basicUnit = unitCache.getGlobalUnit(unitCache.getBaseUnitId(cargoVO.getUnitId()));
            if (basicUnit != null){
                cargoVO.setParentUnitId(basicUnit.getUnitId());
                cargoVO.setParentUnitName(basicUnit.getUnitName());
            }
        }
        return cargoVO;
    }

    @Override
    public CommonPage<CargoPageVO> queryPage(CargoPageQuery pageQuery) {
        Long cargoCategoryId = pageQuery.getCargoCategoryId();
        List<Long> cargoCategoryIds = null;
        if (cargoCategoryId != null && cargoCategoryId != 0L) {
            cargoCategoryIds = cargoCategoryService.queryAllChildren(cargoCategoryId)
                    .stream()
                    .map(CargoCategory::getId)
                    .collect(Collectors.toList());
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<CargoPageVO> list = cargoMapper.queryPage(pageQuery, cargoCategoryIds);
        list.forEach(cargoPageVO -> cargoPageVO.setUnit(unitCache.getGlobalUnitName(cargoPageVO.getUnitId())));
        return CommonPage.convertPage(list);
    }

    @Override
    public List<SyncTreeNodeVO> getSyncTree(SyncTreeQueryDTO dto) {
        ResponseInfo<List<SyncTreeNodeVO>> syncTree;
        try {
            syncTree = platformMaterialFeignClient.getSyncTree(dto.getParentId(), dto.getKeyword());
        } catch (Exception e) {
            log.error("从平台获取同步信息feign调用异常:" + e.getCause() + e.getMessage());
            throw new BmosException(WmsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        if (syncTree.isSuccess()) {
            List<SyncTreeNodeVO> data = syncTree.getData();
            List<Long> platformIds = data.stream()
                    .filter(vo -> !vo.isCategoryFlag()).map(SyncTreeNodeVO::getId).collect(Collectors.toList());
            cargoMapper.selectByPlatformMaterialIds(platformIds).forEach(cargo -> {

            });
            List<Cargo> existed = cargoMapper.selectByPlatformMaterialIds(platformIds);
            Set<Long> longs = CollectionUtils.convertSet(existed, Cargo::getPlatformMaterialId);
            // 过滤已存在的货品
            recRemoveIfExisted(data, longs);
            // 若是关键字搜索则过滤掉空节点
            if (StrUtil.isNotBlank(dto.getKeyword())) {
                data.forEach(this::cleanTree);
                data.removeIf(node -> node.isCategoryFlag() && CollUtil.isEmpty(node.getChildren()));
            }
            return data;
        }
        throw new BmosException(WmsResponseCode.PLATFORM_GET_SYNC_ERROR, syncTree.getMessage());
    }

    @Override
    public List<SyncTreeNodeVO> getSyncTreeAll() {
        ResponseInfo<List<SyncTreeNodeVO>> res;
        try {
            res = platformMaterialFeignClient.getSyncTreeAll();
        } catch (Exception e) {
            log.error("从平台获取同步全量树feign调用异常:" + e.getCause() + e.getMessage());
            throw new BmosException(WmsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        if (res.isSuccess()) {
            return res.getData();
        }
        throw new BmosException(WmsResponseCode.PLATFORM_GET_SYNC_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncMaterialAndCategory(SyncCargoDTO dto) {
        MaterialIssueRequestDTO remoteDTO = new MaterialIssueRequestDTO();
        remoteDTO.setMaterialIds(dto.getMaterialIds());
        remoteDTO.setMaterialCategoryIds(dto.getMaterialCategoryIds());
        MaterialIssueBusinessDTO materialIssueBusinessDTO = new MaterialIssueBusinessDTO();
        materialIssueBusinessDTO.setChildCodeList(Collections.singletonList(CARGO.getValue()));
        materialIssueBusinessDTO.setPlatformName(platformName);
        remoteDTO.setBusinesses(Collections.singletonList(materialIssueBusinessDTO));
        FeignUtils.handleRequest(data -> platformMaterialFeignClient.issueMaterialAndCategory(data), remoteDTO);
    }

    @Override
    public List<CargoVO> queryNotMemberListByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return new ArrayList<>();
        }
        List<Cargo> list = cargoMapper.selectNorMemberListByCargoCategoryId(categoryId);
        return CargoConvert.INSTANCE.convertToVO(list);
    }

    @Override
    public void issueMaterialAndCategory(RemoteIssueFeignDTO dto) {
        // 处理分类下发
        cargoCategoryService.issueCategory(dto.getCategoryList());
        if (CollUtil.isEmpty(dto.getMaterialList())) {
            return;
        }
        // 物料下发前置处理 获取需要新增的物料
        List<Cargo> neededIssueMaterials = getNeededIssueMaterials(dto.getMaterialList());
        cargoMapper.insertBatch(neededIssueMaterials);
    }

    /**
     * 校验平台合并编码在平台是否存在
     *
     * @param code
     */
    private void checkPlatformMaterialCodeExisted(String code) {
        ResponseInfo<Boolean> checkRes = FeignUtils.handleRequest((data) -> platformMaterialFeignClient.checkMergeCodeExisted(code, null), null);
        if (checkRes.isError()) {
            throw new BmosException(WmsResponseCode.PLATFORM_CHECK_CODE_ERROR, checkRes.getMessage());
        }
        if (checkRes.getData()) {
            throw new BmosException(WmsResponseCode.PLATFORM_MATERIAL_CATEGORY_CODE_EXISTED);
        }
    }

    private void recRemoveIfExisted(List<SyncTreeNodeVO> treeNodes, Set<Long> existedIds) {
        treeNodes.removeIf(node -> existedIds.contains(node.getId()));
        treeNodes.forEach(node -> {
            if (node.isCategoryFlag() && CollUtil.isNotEmpty(node.getChildren())) {
                recRemoveIfExisted(node.getChildren(), existedIds);
            }
        });
    }

    private SyncTreeNodeVO cleanTree(SyncTreeNodeVO node) {
        if (CollUtil.isEmpty(node.getChildren())) {
            return node;
        }
        List<SyncTreeNodeVO> cleanedChildList = new ArrayList<>();
        for (SyncTreeNodeVO child : node.getChildren()) {
            if (child.isCategoryFlag()) {
                SyncTreeNodeVO cleanedChild = cleanTree(child);
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
     * 获取需要新增的物料列表
     */
    private List<Cargo> getNeededIssueMaterials(List<MaterialIssueFeignDTO> materialList) {
        List<Long> materialsIds = CollectionUtils.convertList(materialList, MaterialIssueFeignDTO::getId);
        List<Cargo> dbMaterials = cargoMapper.selectByPlatformMaterialIds(materialsIds);
        List<Cargo> allNewMaterials = new ArrayList<>();
        List<CargoCategory> categories = cargoCategoryService.selectAllList();
        Set<Long> dbPlatformMaterialIds = CollectionUtils.convertSet(dbMaterials, Cargo::getPlatformMaterialId);
        // 获取需要新增的物料
        List<Cargo> newMaterials = filterNeedInsert(dbPlatformMaterialIds, materialList, categories);
        // 处理所属物料关联关系
        // 本地已有的物料 id与平台关联id map
        Map<Long, Long> platformIdMap = CollectionUtils.convertMap(dbMaterials, Cargo::getPlatformMaterialId, Cargo::getId);
        // 新增的物料 id与平台关联id map
        Map<Long, Long> newPlatformIdMap = CollectionUtils.convertMap(newMaterials, Cargo::getPlatformMaterialId, Cargo::getId);
        for (Cargo cargo : newMaterials) {
            if (cargo.getIsMember()) {
                Long platformPrincipalId = cargo.getSubMaterialId();
                Long newPrincipalId = ObjectUtil.isNull(platformIdMap.get(platformPrincipalId)) ? newPlatformIdMap.get(platformPrincipalId) : platformIdMap.get(platformPrincipalId);
                cargo.setSubMaterialId(newPrincipalId);

            }
        }
        allNewMaterials.addAll(newMaterials);
        return allNewMaterials;
    }

    /**
     * 过滤出需要新增的物料
     *
     * @param dbPlatformMaterialIds 已有的id
     * @param materialList          下发的物料列表
     * @param businessCategory      当前业务类型下的分类列表
     * @return
     */
    private static List<Cargo> filterNeedInsert(Set<Long> dbPlatformMaterialIds, List<MaterialIssueFeignDTO> materialList, List<CargoCategory> businessCategory) {
        List<MaterialIssueFeignDTO> collect;
        collect = materialList;
        if (CollUtil.isNotEmpty(dbPlatformMaterialIds)) {
            collect = materialList.stream().filter(m -> !dbPlatformMaterialIds.contains(m.getId())).collect(Collectors.toList());
        }
        Map<Long, Long> idMap = CollectionUtils.convertMap(businessCategory, CargoCategory::getPlatformCategoryId, CargoCategory::getId);
        return collect.stream().map(m -> {
            Cargo cargo = new Cargo();
            cargo.setId(CustomIdGenerator.nextId());
            cargo.setPlatformMaterialId(m.getId());
            cargo.setCargoCategoryId(idMap.get(m.getMaterialCategoryId()));
            cargo.setCargoName(m.getName());
            cargo.setCargoCode(m.getCode());
            cargo.setMergeCode(m.getMergeCode());
            cargo.setSpecification(m.getSpecification());
            cargo.setUnitId(m.getUnitId());
            cargo.setIsMember(m.getSubMaterial());
            cargo.setSubMaterialId(m.getPrincipalMaterialId());
            if (m.getExpandInfo() != null) {
                cargo.setSupplier(m.getExpandInfo().getSupplier());
                cargo.setProducer(m.getExpandInfo().getProducer());
            }
            cargo.setRemark(m.getRemark());
            cargo.setEnable(false);
            return cargo;
        }).collect(Collectors.toList());
    }
}
