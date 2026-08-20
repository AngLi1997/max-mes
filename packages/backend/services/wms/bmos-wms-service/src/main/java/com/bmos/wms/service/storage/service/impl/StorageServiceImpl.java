package com.bmos.wms.service.storage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.wms.common.enums.inventory.StorageLevelEnum;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.service.platform.permission.service.ResourcePermissionService;
import com.bmos.wms.service.position.mapper.ICargoPositionMapper;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.storage.convert.StorageConvert;
import com.bmos.wms.service.storage.dto.StorageCreateDTO;
import com.bmos.wms.service.storage.dto.StorageEditDTO;
import com.bmos.wms.service.storage.mapper.IStorageMapper;
import com.bmos.wms.service.storage.model.Storage;
import com.bmos.wms.service.storage.service.IStorageService;
import com.bmos.wms.service.storage.vo.StorageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.wms.common.constants.StorageConstant.MAX_LEVEL;

/**
 * 存储区域 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:31
 */
@Service
@Slf4j
public class StorageServiceImpl implements IStorageService {

    private static final String LOG_PREFIX = "[存储区域配置]";

    @Resource
    private IStorageMapper storageMapper;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private ResourcePermissionService resourcePermissionService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createStorage(StorageCreateDTO dto) {

        Storage parentStorage = null;
        // id校验
        if (dto.getParentId() != null && (parentStorage = storageMapper.selectById(dto.getParentId())) == null) {
            log.error("{}父级存储区域id不存在", LOG_PREFIX);
            throw new BmosException(WmsResponseCode.STORAGE_NOT_EXIST);
        }

        // 校验同级存储区域名称是否重复
        if (storageMapper.exist(dto.getParentId(), dto.getName())) {
            throw new BmosException(WmsResponseCode.STORAGE_NAME_EXISTED);
        }

        Storage storage = new Storage();
        storage.setParentId(dto.getParentId());
        storage.setName(dto.getName());
        // 层级 = 父级层级 + 1
        storage.setLevel(Optional.ofNullable(parentStorage)
                .map(Storage::getLevel)
                .map(StorageLevelEnum::increaseLevel)
                .orElse(StorageLevelEnum.WORKSHOP));
        if (storage.getLevel().getValue() > MAX_LEVEL) {
            throw new BmosException(WmsResponseCode.STORAGE_OVER_LEVEL);
        }
        storageMapper.insert(storage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStorage(StorageEditDTO dto) {
        log.info("{}修改存储区域:{}", LOG_PREFIX, dto);
        Storage storage = storageMapper.selectById(dto.getId());
        // id校验
        if (storage == null) {
            throw new BmosException(WmsResponseCode.STORAGE_NOT_EXIST);
        }
        // 查询名称重复
        if (!StrUtil.equals(storage.getName(), dto.getName()) && storageMapper.exist(storage.getParentId(), dto.getName())) {
            throw new BmosException(WmsResponseCode.STORAGE_NAME_EXISTED);
        }
        storage.setName(dto.getName());
        storageMapper.updateById(storage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStorage(Long id) {
        log.info("{}删除存储区域id:{}", LOG_PREFIX, id);
        Storage storage = storageMapper.selectById(id);
        // id校验
        if (storage == null) {
            throw new BmosException(WmsResponseCode.STORAGE_NOT_EXIST);
        }
        // 有子节点不能删除
        if (CollectionUtil.isNotEmpty(storageMapper.queryListByParentId(id))) {
            throw new BmosException(WmsResponseCode.STORAGE_NOT_ALLOWED_DELETE_WITH_CHILDREN);
        }
        // 绑定了货位不能删除
        if (CollectionUtil.isNotEmpty(cargoPositionMapper.queryListByStorageId(id))) {
            throw new BmosException(WmsResponseCode.STORAGE_NOT_ALLOWED_DELETE_WITH_STORAGE);
        }
        storageMapper.deleteById(id);
    }

    @Override
    public List<StorageVO> queryTree(Long parentId) {
        List<Long> parentIds = new ArrayList<>();
        parentIds.add(parentId);
        List<Storage> list = storageMapper.queryAllChildren(parentIds);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<StorageVO> storageVOS = StorageConvert.INSTANCE.convertVO(list);
        return convertToForest(storageVOS, false);
    }

    @Override
    public List<StorageVO> queryTreeWithCargoPosition(Long parentId) {
        List<Long> parentIds = new ArrayList<>();
        parentIds.add(parentId);
        List<Storage> list = storageMapper.queryAllChildren(parentIds);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<StorageVO> storageVOS = StorageConvert.INSTANCE.convertVO(list);
        List<Long> storagesIds = storageVOS.stream()
                .map(StorageVO::getId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(storagesIds)) {
            return convertToForest(storageVOS, true);
        }
        // 获取部门和下级部门的id
        List<CargoPosition> cargoPositions = cargoPositionMapper.queryEnabledListByStorageIdsWithPermission(storagesIds, platformApiAdaptor.deptIds());
        Map<Long, List<CargoPosition>> collect = cargoPositions.stream()
                .collect(Collectors.groupingBy(CargoPosition::getStorageId));
        storageVOS.forEach(item -> {
            List<CargoPosition> cp = collect.get(item.getId());
            if (CollectionUtil.isNotEmpty(cp)) {
                List<StorageVO> result = cp.stream().map(c -> {
                    StorageVO storageVO = new StorageVO();
                    storageVO.setId(c.getId());
                    storageVO.setParentId(c.getStorageId());
                    storageVO.setPositionCode(c.getCode());
                    storageVO.setName(c.getCode() + "-" + c.getPosition());
                    storageVO.setLevel(StorageLevelEnum.POSITION);
                    return storageVO;
                }).collect(Collectors.toList());
                item.setChildren(result);
            }
        });
        return convertToForest(storageVOS, true);
    }

    private List<StorageVO> convertToForest(List<StorageVO> list, boolean hideEmpty) {
        Map<Long, StorageVO> nodeMap = list.stream()
                .collect(Collectors.toMap(StorageVO::getId, Function.identity(), (k1, k2) -> k1));
        List<StorageVO> forest = new ArrayList<>();
        // 遍历建立父子关系
        for (StorageVO node : list) {
            StorageVO parentNode = nodeMap.get(node.getParentId());
            if (parentNode != null) {
                parentNode.getChildren().add(node);
            } else {
                // 没有父节点的视为根节点
                forest.add(node);
            }
        }
        forest.forEach(this::sortChildren);
        if (hideEmpty){
            cleanEmpty(forest);
        }
        return forest;
    }

    /**
     * 只保留带货位的结点集合
     * @param forest
     */
    private void cleanEmpty(List<StorageVO> forest) {
        forest.removeIf(item -> !Objects.equals(item.getLevel(), StorageLevelEnum.POSITION) && CollectionUtil.isEmpty(item.getChildren()));
        forest.forEach(item -> {
            List<StorageVO> children = item.getChildren();
            if (CollectionUtil.isNotEmpty(children)) {
                cleanEmpty(children);
            }
        });
    }

    private void sortChildren(StorageVO node) {
        List<StorageVO> children = node.getChildren();
        if (!children.isEmpty()) {
            ArrayList<StorageVO> list = new ArrayList<>();
            list.addAll(children.stream()
                    .filter(item -> !Objects.equals(item.getLevel(), StorageLevelEnum.POSITION))
                    .collect(Collectors.toList()));
            list.addAll(children.stream()
                    .filter(item -> Objects.equals(item.getLevel(), StorageLevelEnum.POSITION))
                    .sorted(Comparator.comparing(StorageVO::getPositionCode))
                    .collect(Collectors.toList()));
            node.setChildren(list);
            list.forEach(this::sortChildren);
        }
    }
}
