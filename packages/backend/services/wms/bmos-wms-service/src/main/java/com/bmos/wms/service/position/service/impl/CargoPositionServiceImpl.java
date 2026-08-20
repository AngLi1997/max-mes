package com.bmos.wms.service.position.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.service.businessLog.mapper.ICargoLogMapper;
import com.bmos.wms.service.businessLog.mapper.IPositionLogMapper;
import com.bmos.wms.service.inventory.mapper.IInventoryMapper;
import com.bmos.wms.service.platform.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.wms.service.platform.permission.service.ResourcePermissionService;
import com.bmos.wms.service.platform.user.FeignUtils;
import com.bmos.wms.service.platform.user.feign.PlatformUserOpenFeign;
import com.bmos.wms.service.platform.user.vo.PlatformUserVO;
import com.bmos.wms.service.position.convert.CargoPositionConvert;
import com.bmos.wms.service.position.dto.CargoPositionCreateDTO;
import com.bmos.wms.service.position.dto.CargoPositionPageQuery;
import com.bmos.wms.service.position.mapper.ICargoPositionMapper;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.position.service.ICargoPositionService;
import com.bmos.wms.service.position.vo.CargoPositionVO;
import com.bmos.wms.service.storage.dto.MaterialPositionEditDTO;
import com.bmos.wms.service.storage.mapper.IStorageMapper;
import com.bmos.wms.service.storage.model.Storage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 暂存货位service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 17:53
 */
@Service
@Slf4j
public class CargoPositionServiceImpl implements ICargoPositionService {

    private static final String LOG_PREFIX = "[暂存货位]";

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private IStorageMapper storageMapper;

    @Resource
    private ResourcePermissionService resourcePermissionService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private PlatformUserOpenFeign platformUserOpenFeign;

    @Resource
    private IInventoryMapper iInventoryMapper;

    @Resource
    private IPositionLogMapper positionLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCargoPosition(CargoPositionCreateDTO dto) {
        log.info("{} 新建暂存货位: {}", LOG_PREFIX, dto);
        Storage storage = storageMapper.selectById(dto.getStorageId());
        if (storage == null) {
            throw new BmosException(WmsResponseCode.STORAGE_NOT_EXIST);
        }
        // 校验全局暂存货位编码是否存在
        if (cargoPositionMapper.existCode(dto.getCode())) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_CODE_EXIST);
        }

        CargoPosition cargoPosition = new CargoPosition();
        cargoPosition.setStorageId(dto.getStorageId());
        cargoPosition.setIdPath(storageMapper.getStoragePath(dto.getStorageId()));
        cargoPosition.setPosition(dto.getPosition());
        cargoPosition.setCode(dto.getCode());
        cargoPosition.setRemark(dto.getRemark());
        // 默认停用
        cargoPosition.setEnable(false);
        cargoPositionMapper.insert(cargoPosition);
        // 保存数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .resourceId(cargoPosition.getId())
                .deptIds(dto.getDeptIds())
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCargoPosition(MaterialPositionEditDTO dto) {
        log.info("{} 编辑暂存货位: {}", LOG_PREFIX, dto);
        CargoPosition cargoPosition = cargoPositionMapper.selectById(dto.getId());
        if (cargoPosition == null) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_EXIST);
        }
        // 校验全局暂存货位编码是否存在
        if (!StrUtil.equals(dto.getCode(), cargoPosition.getCode())
                && cargoPositionMapper.existCode(dto.getCode())) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_CODE_EXIST);
        }
        cargoPosition.setPosition(dto.getPosition());
        cargoPosition.setCode(dto.getCode());
        cargoPosition.setRemark(dto.getRemark());
        cargoPositionMapper.updateById(cargoPosition);
        // 更新数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .resourceId(cargoPosition.getId())
                .deptIds(dto.getDeptIds())
                .build());
    }

    @Nullable
    @Override
    public CargoPositionVO queryInfoById(Long id) {
        return Optional.ofNullable(id)
                .map(cargoPositionMapper::selectById)
                .map(CargoPositionConvert.INSTANCE::convertToVO)
                .orElse(null);
    }

    @Override
    public CommonPage<CargoPositionVO> queryPage(CargoPositionPageQuery pageQuery) {

        // 数据权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtil.isEmpty(deptIds)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
        }

        List<Long> positionIds = storageMapper.queryAllChildren(pageQuery.getStorageId())
                .stream().map(Storage::getId).collect(Collectors.toList());
        positionIds.add(pageQuery.getStorageId());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<CargoPosition> list = cargoPositionMapper.queryList(pageQuery, deptIds, positionIds);
        CommonPage<CargoPosition> page = CommonPage.convertPage(list);
        CommonPage<CargoPositionVO> result = CargoPositionConvert.INSTANCE.convertToPageVO(page);
        // 填充路径
        if (CollectionUtil.isNotEmpty(result.getList())) {
            Set<Long> ids = page.getList().stream()
                    .map(CargoPosition::getIdPath)
                    .map(path -> StrUtil.split(path, ","))
                    .flatMap(List::stream)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            List<Storage> storages = storageMapper.queryListByIds(ids);
            if (CollectionUtil.isNotEmpty(storages)) {
                Map<String, String> map = storages.stream().collect(Collectors.toMap(item -> item.getId().toString(), Storage::getName, (k1, k2) -> k1));
                for (CargoPositionVO cargoPosition : result.getList()) {
                    String path = cargoPosition.getIdPath();
                    if (StrUtil.isBlank(path)) {
                        continue;
                    }
                    String[] split = path.split(",");
                    List<String> names = new ArrayList<>();
                    for (String id : split) {
                        names.add(map.get(id));
                    }
                    cargoPosition.setPath(StrUtil.join("/", names));
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableCargoPosition(Long id) {
        changeEnable(id, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableCargoPosition(Long id) {
        changeEnable(id, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCargoPosition(Long id) {
        CargoPosition cargoPosition = cargoPositionMapper.selectById(id);
        if (cargoPosition == null) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_EXIST);
        }
        // 已启用的货位不能删除
        if (Objects.equals(cargoPosition.getEnable(), true)) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_ENABLED);
        }
        boolean exist = positionLogMapper.existByCargoPositionId(id);
        if (exist){
            throw new BmosException(WmsResponseCode.CARGO_POSITION_HAS_LOG);
        }
        cargoPositionMapper.deleteById(id);
        // 删除资源权限的
        resourcePermissionService.deleteByResourceId(id);
    }

    @Override
    public List<PlatformUserVO> queryPositionBoundUserList(Long positionId) {
        List<Long> deptIds = resourcePermissionService.getDeptListByResourceId(positionId);
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        ResponseInfo<List<PlatformUserVO>> responseInfo = FeignUtils.handleRequest(data -> platformUserOpenFeign.listByDeptList(data), deptIds);
        return responseInfo.getData();
    }

    @Override
    public List<CargoPosition> queryAllEnabledChildrenByStorageId(Long storageId) {
        // 先查询是否为货位信息
        CargoPosition cargoPosition = cargoPositionMapper.selectById(storageId);
        if (cargoPosition != null) {
            ArrayList<CargoPosition> result = new ArrayList<>();
            result.add(cargoPosition);
            return result;
        }
        List<Storage> list = storageMapper.queryAllChildren(storageId);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<CargoPosition> cargoPositions = cargoPositionMapper.queryEnabledListByStorageIdsWithPermission(list.stream().map(Storage::getId).collect(Collectors.toList()), platformApiAdaptor.deptIds());
        if (CollectionUtil.isEmpty(cargoPositions)) {
            return new ArrayList<>();
        }
        return cargoPositions;
    }

    @Override
    @Nullable
    public CargoPositionVO queryInfoByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        CargoPosition cargoPosition = cargoPositionMapper.selectEnabledByCode(code);
        if (cargoPosition == null) {
            return null;
        }
        return CargoPositionConvert.INSTANCE.convertToVO(cargoPosition);
    }

    @Nullable
    @Override
    public CargoPosition getByIdWithPermission(Long positionId) {
        CargoPosition cargoPosition = cargoPositionMapper.selectById(positionId);
        if (cargoPosition == null) {
            return null;
        }
        List<Long> deptIds = resourcePermissionService.getDeptListByResourceId(positionId);
        if (CollectionUtil.isEmpty(deptIds) || !CollectionUtil.containsAny(deptIds, platformApiAdaptor.deptIds())) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_PERMISSION_DENIED);
        }
        return cargoPosition;
    }

    @Override
    public String getCargoPositionPath(Long id) {
        if (id == null) {
            return null;
        }
        return cargoPositionMapper.getCargoPositionPath(id);
    }

    @Override
    public Collection<CargoPosition> selectBatchIds(List<Long> positionIds) {
        return cargoPositionMapper.selectBatchIds(positionIds);
    }

    /**
     * 切换启用停用状态
     *
     * @param id     id
     * @param enable 启用停用状态
     */
    private void changeEnable(Long id, Boolean enable) {
        CargoPosition cargoPosition = cargoPositionMapper.selectById(id);
        if (cargoPosition == null) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_EXIST);
        }
        if (Objects.equals(enable, cargoPosition.getEnable())) {
            throw Objects.equals(enable, true)
                    ? new BmosException(WmsResponseCode.CARGO_POSITION_ENABLED)
                    : new BmosException(WmsResponseCode.CARGO_POSITION_DISABLED);
        }
        // 存储区域下存在可用量不为0的物料 不能停用
        if (Objects.equals(enable, false) && CollectionUtil.isNotEmpty(iInventoryMapper.queryListByPositionId(id))) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_ALLOWED_DISABLE_WITH_MATERIAL);
        }
        cargoPosition.setEnable(enable);
        cargoPositionMapper.updateById(cargoPosition);
    }
}
