package com.bmos.mes.service.storage.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.storage.StorageLevelEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.storage.config.convert.StorageConvert;
import com.bmos.mes.service.storage.config.dto.StorageCreateDTO;
import com.bmos.mes.service.storage.config.dto.StorageEditDTO;
import com.bmos.mes.service.storage.config.mapper.ICargoPositionMapper;
import com.bmos.mes.service.storage.config.mapper.IStorageMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.model.Storage;
import com.bmos.mes.service.storage.config.service.IStorageConfigService;
import com.bmos.mes.service.storage.config.vo.StorageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.constant.StorageConstant.MAX_LEVEL;

/**
 * 暂存间 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:31
 */
@Service
@Slf4j
public class StorageConfigServiceImpl implements IStorageConfigService {

    private static final String LOG_PREFIX = "[暂存间配置]";

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
            log.error("{}父级暂存间id不存在", LOG_PREFIX);
            throw new BmosException(MesResponseCode.STORAGE_NOT_EXIST);
        }

        // 校验同级暂存间名称是否重复
        if (storageMapper.exist(dto.getParentId(), dto.getName())) {
            throw new BmosException(MesResponseCode.STORAGE_NAME_EXISTED);
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
            throw new BmosException(MesResponseCode.STORAGE_OVER_LEVEL);
        }
        storageMapper.insert(storage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStorage(StorageEditDTO dto) {
        log.info("{}修改暂存间:{}", LOG_PREFIX, dto);
        Storage storage = storageMapper.selectById(dto.getId());
        // id校验
        if (storage == null) {
            throw new BmosException(MesResponseCode.STORAGE_NOT_EXIST);
        }
        // 查询名称重复
        if (!StrUtil.equals(storage.getName(), dto.getName()) && storageMapper.exist(storage.getParentId(), dto.getName())) {
            throw new BmosException(MesResponseCode.STORAGE_NAME_EXISTED);
        }
        storage.setName(dto.getName());
        storageMapper.updateById(storage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStorage(Long id) {
        log.info("{}删除暂存间id:{}", LOG_PREFIX, id);
        Storage storage = storageMapper.selectById(id);
        // id校验
        if (storage == null) {
            throw new BmosException(MesResponseCode.STORAGE_NOT_EXIST);
        }
        // 有子节点不能删除
        if (CollectionUtil.isNotEmpty(storageMapper.queryListByParentId(id))) {
            throw new BmosException(MesResponseCode.STORAGE_NOT_ALLOWED_DELETE_WITH_CHILDREN);
        }
        // 绑定了货位不能删除
        if (CollectionUtil.isNotEmpty(cargoPositionMapper.queryListByStorageId(id))) {
            throw new BmosException(MesResponseCode.STORAGE_NOT_ALLOWED_DELETE_WITH_STORAGE);
        }
        storageMapper.deleteById(id);
    }

    @Override
    public List<StorageVO> queryList(Long parentId, String keyword) {
        List<Storage> list = storageMapper.queryListByParentId(parentId);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return StorageConvert.INSTANCE.convertVO(list).stream()
                .filter(item -> StrUtil.contains(item.getName(), keyword))
                .collect(Collectors.toList());
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
            return new ArrayList<>();
        }
        // 获取部门和下级部门的id
        List<CargoPosition> cargoPositions = cargoPositionMapper.queryEnabledListByStorageIdsWithPermission(storagesIds, platformApiAdaptor.deptIds());
        if (CollectionUtil.isEmpty(cargoPositions)){
            return new ArrayList<>();
        }
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

    @Override
    public Map<Long, Storage> selectBatchIds(Set<Long> storageIdList) {
        if (CollUtil.isEmpty(storageIdList)){
            return new HashMap<>();
        }
        return storageMapper.selectBatchIds(storageIdList).stream()
                .collect(Collectors.toMap(Storage::getId, item -> item));
    }

    private List<StorageVO> convertToForest(List<StorageVO> list, boolean hiddenEmpty) {
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
        if (hiddenEmpty){
            cleanEmpty(forest);
        }
        forest.forEach(this::sortChildren);
        return forest;
    }

    /**
     * 只保留带货位的结点集合
     * @param forest
     */
    private void cleanEmpty(List<StorageVO> forest) {
        if (CollectionUtil.isEmpty(forest)) {
            return;
        }
        Iterator<StorageVO> iterator = forest.iterator();
        while (iterator.hasNext()) {
            StorageVO item = iterator.next();
            List<StorageVO> children = item.getChildren();
            cleanEmpty(children);
            // 如果当前节点不是货位节点且没有有效的子节点，则删除当前节点
            if (!Objects.equals(item.getLevel(), StorageLevelEnum.POSITION) && CollectionUtil.isEmpty(children)) {
                iterator.remove();
            }
        }
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
