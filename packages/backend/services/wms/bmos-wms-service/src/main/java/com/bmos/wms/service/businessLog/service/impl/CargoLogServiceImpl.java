package com.bmos.wms.service.businessLog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.wms.common.enums.inventory.CargoInventoryOperateLogType;
import com.bmos.wms.service.businessLog.convert.CargoLogConvert;
import com.bmos.wms.service.businessLog.dto.CargoLogPageQuery;
import com.bmos.wms.service.businessLog.mapper.ICargoLogMapper;
import com.bmos.wms.service.businessLog.model.CargoLog;
import com.bmos.wms.service.businessLog.service.ICargoLogService;
import com.bmos.wms.service.businessLog.vo.CargoLogVO;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.mapper.IInventoryMapper;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.position.mapper.ICargoPositionMapper;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.sendout.model.SendOutOrder;
import com.bmos.wms.service.utils.UserUtils;
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
public class CargoLogServiceImpl implements ICargoLogService {

    @Resource
    private ICargoLogMapper cargoLogMapper;

    @Resource
    private ICargoMapper cargoMapper;

    @Resource
    private IInventoryMapper inventoryMapper;

    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private UnitCache unitCache;

    @Override
    public CommonPage<CargoLogVO> queryPage(CargoLogPageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<CargoLog> cargoLogList = cargoLogMapper.queryPage(pageQuery);
        CommonPage<CargoLog> page = CommonPage.convertPage(cargoLogList);
        CommonPage<CargoLogVO> result = CargoLogConvert.INSTANCE.convertToVO(page);
        result.getList().forEach(item -> item.setUnit(unitCache.getGlobalUnitName(item.getUnitId())));
        PrecisionHelper.convertUnitRenderList(result.getList());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCargoLog(CargoInventoryOperateLogType logType, Inventory inventory, BigDecimal quantity, String[] operatorIds, SendOutOrder order, String remark) {
        if (inventory == null) {
            return;
        }
        saveCargoLog(logType, Collections.singletonList(inventory), Collections.singletonList(quantity), operatorIds, order, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCargoLog(CargoInventoryOperateLogType logType, List<Inventory> inventories, List<BigDecimal> quantities, String[] operatorIds, SendOutOrder order, String remark) {
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
            positionMap = cargoPositionMapper.selectBatchIds(positionIds)
                    .stream()
                    .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (k1, k2) -> k1));
        }

        List<CargoLog> list = new ArrayList<>();

        for (int i = 0; i < inventories.size(); i++) {
            Inventory inventory = inventories.get(i);
            InventoryBatch inventoryBatch = batchMap.get(inventory.getInventoryBatchId());
            Cargo cargo = cargoMap.get(inventory.getCargoId());
            CargoPosition cargoPosition = positionMap.get(inventory.getPositionId());
            for (int j = 0; j < logType.getOperateType().length; j++) {
                CargoLog cargoLog = new CargoLog();
                cargoLog.setOperateTime(LocalDateTime.now());
                cargoLog.setOperateType(logType.getOperateType()[j]);
                cargoLog.setOperateInfo(logType.getOperateInfo()[j]);
                cargoLog.setOperatorId(operatorIds[j]);
                cargoLog.setOperatorName(UserUtils.getUsername(operatorIds[j]));
                cargoLog.setInventoryNo(inventory.getNo());
                cargoLog.setReserveQuantity(inventory.getReserveQuantity());
                cargoLog.setAvailableQuantity(inventory.getAvailableQuantity());
                cargoLog.setUnitId(inventory.getUnitId());
                cargoLog.setRemark(remark);
                // 批次信息
                if (inventoryBatch != null) {
                    cargoLog.setInventoryBatchNo(inventoryBatch.getBatchNo());
                    cargoLog.setFactoryBatchNo(inventoryBatch.getFactoryBatchNo());
                    cargoLog.setAvailable(inventoryBatch.getAvailable());
                    cargoLog.setEffectiveDate(inventoryBatch.getExpiredDate());
                    cargoLog.setReportOrderNo(inventoryBatch.getReportNo());
                    cargoLog.setLicenseOrderNo(inventoryBatch.getLicenceNo());
                }
                // 货位信息
                if (cargoPosition != null) {
                    cargoLog.setPosition(cargoPosition.getPosition());
                    cargoLog.setPositionPath(cargoPositionMapper.getCargoPositionPath(cargoPosition.getId()));
                    cargoLog.setPositionCode(cargoPosition.getCode());
                }
                // 货品信息
                if (cargo != null) {
                    cargoLog.setCargoId(cargo.getId());
                    cargoLog.setCargoName(cargo.getCargoName());
                    cargoLog.setMergeCode(cargo.getMergeCode());
                    cargoLog.setSupplier(cargo.getSupplier());
                    cargoLog.setProducer(cargo.getProducer());
                }
                if (order != null) {
                    cargoLog.setProductName(order.getProductName());
                    cargoLog.setProductMergeCode(order.getProductCode());
                    cargoLog.setProductBatchNo(order.getBatchNo());
                    cargoLog.setProcessName(order.getProcessName());
                    cargoLog.setPullOrderNo(order.getPullOrderNo());
                }
                cargoLog.setValidateOrderNo(null);
                cargoLog.setCheckInfo(null);
                list.add(cargoLog);
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            cargoLogMapper.saveOrUpdateBatch(list);
        }
    }
}
