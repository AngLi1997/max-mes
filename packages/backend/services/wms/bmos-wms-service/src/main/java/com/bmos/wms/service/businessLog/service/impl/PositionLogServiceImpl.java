package com.bmos.wms.service.businessLog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.wms.common.enums.inventory.PositionInventoryOperateLogType;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.businessLog.convert.PositionLogConvert;
import com.bmos.wms.service.businessLog.dto.PositionLogPageQuery;
import com.bmos.wms.service.businessLog.mapper.IPositionLogMapper;
import com.bmos.wms.service.businessLog.model.PositionLog;
import com.bmos.wms.service.businessLog.service.IPositionLogService;
import com.bmos.wms.service.businessLog.vo.PositionLogVO;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.position.service.ICargoPositionService;
import com.bmos.wms.service.position.vo.CargoPositionVO;
import com.bmos.wms.service.sendout.model.SendOutOrder;
import com.bmos.wms.service.storage.mapper.IStorageMapper;
import com.bmos.wms.service.storage.model.Storage;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:20
 */
@Service
public class PositionLogServiceImpl implements IPositionLogService {
    @Resource
    private IPositionLogMapper positionLogMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;

    @Resource
    private ICargoMapper cargoMapper;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private IStorageMapper storageMapper;


    @Override
    public CommonPage<PositionLogVO> queryPage(PositionLogPageQuery pageQuery) {
        List<Long> positionIdList = new ArrayList<>();
        Long positionId = pageQuery.getPositionOrStorageId();
        if (positionId == null || storageMapper.selectById(positionId) != null) {
            List<CargoPosition> cargoPositions = cargoPositionService.queryAllEnabledChildrenByStorageId(positionId);
            if (CollectionUtil.isEmpty(cargoPositions)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
            }
            List<Long> collect = cargoPositions.stream().map(CargoPosition::getId).collect(Collectors.toList());
            collect.add(positionId);
            positionIdList.addAll(collect);
        } else {
            positionIdList.add(positionId);
            // 传的是货位id
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<PositionLog> positionLogs = positionLogMapper.queryPage(pageQuery, positionIdList);
        CommonPage<PositionLog> page = CommonPage.convertPage(positionLogs);
        CommonPage<PositionLogVO> result = PositionLogConvert.INSTANCE.convertToVO(page);
        result.getList().forEach(item -> item.setUnit(unitCache.getGlobalUnitName(item.getUnitId())));
        PrecisionHelper.convertUnitRenderList(result.getList());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePositionLog(PositionInventoryOperateLogType logType, Inventory inventory, BigDecimal quantity, String[] operatorIds, SendOutOrder order, String remark) {
        if (inventory == null) {
            return;
        }
        savePositionLog(logType, Collections.singletonList(inventory), Collections.singletonList(quantity), operatorIds, order, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePositionLog(PositionInventoryOperateLogType logType, List<Inventory> inventories, List<BigDecimal> quantities, String[] operatorIds, SendOutOrder order, String remark) {
        Map<Long, InventoryBatch> batchMap = new HashMap<>();
        Map<Long, Cargo> cargoMap = new HashMap<>();
        Map<Long, CargoPosition> positionMap = new HashMap<>();

        List<Long> inventoryBatchIds = inventories.stream().map(Inventory::getInventoryBatchId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(inventoryBatchIds)) {
            batchMap = inventoryBatchMapper.selectBatchIds(inventoryBatchIds)
                    .stream()
                    .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k1));
        }
        List<Long> cargoIds = inventories.stream().map(Inventory::getCargoId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(cargoIds)) {
            cargoMap = cargoMapper.selectBatchIds(cargoIds)
                    .stream()
                    .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k1));
        }
        List<Long> positionIds = inventories.stream().map(Inventory::getPositionId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(positionIds)) {
            positionMap = cargoPositionService.selectBatchIds(positionIds)
                    .stream()
                    .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k1));
        }

        List<PositionLog> list = new ArrayList<>();

        for (int i = 0; i < inventories.size(); i++) {
            Inventory inventory = inventories.get(i);
            InventoryBatch inventoryBatch = batchMap.get(inventory.getInventoryBatchId());
            Cargo cargo = cargoMap.get(inventory.getCargoId());
            CargoPosition cargoPosition = positionMap.get(inventory.getPositionId());
            for (int j = 0; j < logType.getOperateType().length; j++) {
                PositionLog positionLog = new PositionLog();
                positionLog.setOperateTime(LocalDateTime.now());
                positionLog.setOperateType(logType.getOperateType()[j]);
                positionLog.setOperateInfo(logType.getOperateInfo()[j]);
                positionLog.setOperatorId(operatorIds[j]);
                positionLog.setOperatorName(SysUserHolder.getUser().getUserName());
                positionLog.setInventoryNo(inventory.getNo());
                positionLog.setQuantity(quantities.get(i));
                positionLog.setUnitId(inventory.getUnitId());
                positionLog.setRemark(remark);
                // 批次信息
                if (inventoryBatch != null) {
                    positionLog.setInventoryBatchNo(inventoryBatch.getBatchNo());
                }
                // 货位信息
                if (cargoPosition != null) {
                    positionLog.setPositionId(cargoPosition.getId());
                    positionLog.setPosition(cargoPosition.getPosition());
                    positionLog.setPositionPath(cargoPositionService.getCargoPositionPath(cargoPosition.getId()));
                    positionLog.setPositionCode(cargoPosition.getCode());
                }
                // 货品信息
                if (cargo != null) {
                    positionLog.setCargoId(cargo.getId());
                    positionLog.setCargoName(cargo.getCargoName());
                    positionLog.setMergeCode(cargo.getMergeCode());
                }
                if (order != null) {
                    positionLog.setProductName(order.getProductName());
                    positionLog.setProductMergeCode(order.getProductCode());
                    positionLog.setProductBatchNo(order.getBatchNo());
                    positionLog.setProcessName(order.getProcessName());
                    positionLog.setPullOrderNo(order.getPullOrderNo());
                }
                list.add(positionLog);
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            positionLogMapper.saveOrUpdateBatch(list);
        }
    }
}
