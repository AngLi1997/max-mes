package com.bmos.wms.service.cargo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.wms.common.enums.inventory.CategoryInfoTypeEnum;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.service.cargo.convert.CargoCategoryConvert;
import com.bmos.wms.service.cargo.convert.CargoConvert;
import com.bmos.wms.service.cargo.dto.CargoCategoryCreateDTO;
import com.bmos.wms.service.cargo.dto.CategoryIssueFeignDTO;
import com.bmos.wms.service.cargo.mapper.ICargoCategoryMapper;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.cargo.model.CargoCategory;
import com.bmos.wms.service.cargo.service.ICargoCategoryService;
import com.bmos.wms.service.cargo.vo.CargoCategoryVO;
import com.bmos.wms.service.cargo.vo.CargoTreeVO;
import com.bmos.wms.service.platform.material.dto.ProductMaterialCategorySaveDTO;
import com.bmos.wms.service.platform.material.dto.UnregisterMaterialCategoryDTO;
import com.bmos.wms.service.platform.material.feign.PlatformMaterialFeignClient;
import com.bmos.wms.service.platform.user.FeignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.wms.common.exception.WmsResponseCode.CARGO_CATEGORY_PARENT_NOT_EXIST;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 17:37
 */
@Service
@Slf4j
public class CargoCategoryServiceImpl implements ICargoCategoryService {

    private static final String LOG_PREFIX = "[货品分类]";

    @Resource
    private ICargoCategoryMapper cargoCategoryMapper;

    @Resource
    private ICargoMapper cargoMapper;

    @Resource
    private PlatformMaterialFeignClient platformMaterialFeignClient;

    @Value("${spring.application.name}")
    private String platformName;

    @Override
    public List<CargoCategoryVO> queryTree() {
        List<CargoCategory> cargoCategories = cargoCategoryMapper.selectList();
        // 组装树
        List<CargoCategoryVO> list = CargoCategoryConvert.INSTANCE.convertToVO(cargoCategories);
        return TreeUtil.buildTree(list, false);
    }

    @Nullable
    @Override
    public CargoCategoryVO queryById(Long id) {
        if (id == null) {
            return null;
        }
        CargoCategory cargoCategory = cargoCategoryMapper.selectById(id);
        return CargoCategoryConvert.INSTANCE.convertToVO(cargoCategory);
    }

    @Nullable
    @Override
    public CargoCategoryVO queryByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        CargoCategory cargoCategory = cargoCategoryMapper.selectByCode(code);
        return CargoCategoryConvert.INSTANCE.convertToVO(cargoCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCargoCategory(CargoCategoryCreateDTO dto) {
        log.info("{}新增货品分类:{}", LOG_PREFIX, dto);
        // 校验
        CargoCategory parent = null;
        long platformParentId = 0L;
        if (dto.getParentId() != 0L) {
            parent = cargoCategoryMapper.selectById(dto.getParentId());
            if (parent == null) {
                throw new BmosException(CARGO_CATEGORY_PARENT_NOT_EXIST);
            }
            platformParentId = parent.getPlatformCategoryId();
        }
        CargoCategory cargoCategory = new CargoCategory(parent, dto.getCargoCategoryName(), dto.getCargoCategoryCode());
        // 校验物料平台合并编码是否存在
        checkPlatformMaterialCodeExisted(cargoCategory.getCargoCategoryMergeCode());
        ProductMaterialCategorySaveDTO remoteDTO = new ProductMaterialCategorySaveDTO();
        remoteDTO.setParentId(platformParentId);
        remoteDTO.setCode(cargoCategory.getCargoCategoryCode());
        remoteDTO.setName(cargoCategory.getCargoCategoryName());
        remoteDTO.setBusinessRegister(true);
        remoteDTO.setBusinessName(CategoryInfoTypeEnum.CARGO.getName());
        Long platformId = FeignUtils.handleRequest(data -> platformMaterialFeignClient.saveMaterialCategory(data), remoteDTO).getData();
        if (platformId == null) {
            throw new BmosException(WmsResponseCode.PLATFORM_MATERIAL_CATEGORY_SAVE_FAIL);
        }
        // 平台新增分类成功后 保存本地
        cargoCategory.setPlatformCategoryId(platformId);
        cargoCategoryMapper.insert(cargoCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCargoCategory(Long id) {
        log.info("{}删除货品分类:{}", LOG_PREFIX, id);
        CargoCategory cargoCategory = cargoCategoryMapper.selectById(id);
        if (cargoCategory == null) {
            throw new BmosException(WmsResponseCode.CARGO_CATEGORY_NOT_EXIST);
        }
        // 查询是否有下级分类
        List<CargoCategory> children = cargoCategoryMapper.selectByParentId(id);
        if (CollectionUtil.isNotEmpty(children)) {
            throw new BmosException(WmsResponseCode.CARGO_CATEGORY_HAS_CHILDREN);
        }
        // 查询分类下是否有货品信息
        List<Cargo> cargos = cargoMapper.selectByCargoCategoryId(id);
        if (CollectionUtil.isNotEmpty(cargos)) {
            throw new BmosException(WmsResponseCode.CARGO_CATEGORY_HAS_CARGO);
        }
        cargoCategoryMapper.deleteById(id);
        // 向平台取消注册
        FeignUtils.handleRequest(data -> platformMaterialFeignClient.unregisterCategory(data), new UnregisterMaterialCategoryDTO(platformName, CategoryInfoTypeEnum.CARGO.getValue(), cargoCategory.getPlatformCategoryId()));
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

    /**
     * 根据父级id递归查询所有子节点
     *
     * @param parentIds 父级id列表
     * @return
     */
    private List<CargoCategory> queryAllChildren(List<Long> parentIds) {
        if (CollectionUtil.isEmpty(parentIds)) {
            return new ArrayList<>();
        }
        if (parentIds.contains(null)) {
            // 查询所有
            return cargoCategoryMapper.selectList(Wrappers.lambdaQuery());
        }
        List<CargoCategory> children = cargoCategoryMapper.queryListByParentId(parentIds);
        if (CollectionUtil.isEmpty(children)) {
            return new ArrayList<>(cargoCategoryMapper.queryListByIds(parentIds));
        }
        List<Long> childrenIds = children.stream().map(CargoCategory::getId).collect(Collectors.toList());
        List<CargoCategory> allChildren = queryAllChildren(childrenIds);
        children.addAll(allChildren);
        children.addAll(cargoCategoryMapper.queryListByIds(parentIds));
        return children;
    }

    @Override
    public List<CargoCategory> queryAllChildren(Long parentId) {
        if (parentId == null) {
            return cargoCategoryMapper.selectList(Wrappers.lambdaQuery());
        }
        List<Long> list = new ArrayList<>();
        list.add(parentId);
        return queryAllChildren(list);
    }

    @Nullable
    @Override
    public CargoCategory selectById(Long cargoCategoryId) {
        return cargoCategoryMapper.selectById(cargoCategoryId);
    }

    @Override
    public List<CargoTreeVO> queryTreeWithCargo() {
        List<CargoCategory> cargoCategories = cargoCategoryMapper.selectList();
        List<CargoTreeVO> treeList = CargoCategoryConvert.INSTANCE.convertToTreeVO(cargoCategories);
        List<Cargo> list = cargoMapper.selectEnableList();
        List<CargoTreeVO> treeList2 = CargoConvert.INSTANCE.convertToTreeVO(list);
        if (CollectionUtil.isNotEmpty(treeList)) {
            treeList.addAll(treeList2);
        }
        return TreeUtil.buildTree(treeList, false);
    }

    @Override
    public List<CargoCategory> selectAllList() {
        return cargoCategoryMapper.selectList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issueCategory(List<CategoryIssueFeignDTO> categoryList) {
        // 查询出存在的全量数据
        List<CargoCategory> categories = cargoCategoryMapper.selectList();
        // 需要新增的列表
        List<CargoCategory> allNewCategories = new ArrayList<>();
        // 过滤需要新增的分类
        List<CargoCategory> needInsert = filterNeedInsert(categoryList, categories);
        // 处理父级id
        handleParentId(categories, needInsert);
        allNewCategories.addAll(needInsert);
        cargoCategoryMapper.insertBatch(allNewCategories);
    }

    private static List<CargoCategory> filterNeedInsert(List<CategoryIssueFeignDTO> categoryList, List<CargoCategory> cargoCategories) {
        List<CategoryIssueFeignDTO> collect;
        collect = categoryList;
        if (CollUtil.isNotEmpty(cargoCategories)) {
            collect = collect.stream()
                    .filter(c -> !cargoCategories.stream()
                            .map(CargoCategory::getPlatformCategoryId)
                            .collect(Collectors.toList())
                            .contains(c.getId()))
                    .collect(Collectors.toList());
        }
        return collect.stream().map(platformCategory -> {
            CargoCategory cargoCategory = new CargoCategory();
            cargoCategory.setParentId(platformCategory.getParentId());
            cargoCategory.setCargoCategoryName(platformCategory.getName());
            cargoCategory.setCargoCategoryCode(platformCategory.getCode());
            cargoCategory.setCargoCategoryMergeCode(platformCategory.getMergeCode());
            cargoCategory.setPlatformCategoryId(platformCategory.getId());
            cargoCategory.setId(CustomIdGenerator.nextId());
            return cargoCategory;
        }).collect(Collectors.toList());
    }

    /**
     * 处理
     *
     * @param currentCategories 当前业务所有分类
     * @param needInsert        需要新增
     */
    private static void handleParentId(List<CargoCategory> currentCategories, List<CargoCategory> needInsert) {
        Map<Long, CargoCategory> dbMap = CollectionUtils.convertMap(currentCategories, CargoCategory::getPlatformCategoryId);
        Map<Long, CargoCategory> newMap = CollectionUtils.convertMap(needInsert, CargoCategory::getPlatformCategoryId);
        for (CargoCategory insert : needInsert) {
            if (!ObjectUtil.equal(insert.getParentId(), 0L)) {
                Long parentId = insert.getParentId();
                CargoCategory dbParent = dbMap.get(parentId);
                if (ObjectUtil.isNull(dbParent) && ObjectUtil.isNull(newMap.get(parentId))) {
                    throw new BmosException(WmsResponseCode.MATERIAL_SYNC_ERROR_CHOSE_NOT_PARENT);
                }
                insert.setParentId(ObjectUtil.isNull(dbParent) ? newMap.get(parentId).getId() : dbParent.getId());
            }
        }
    }
}
