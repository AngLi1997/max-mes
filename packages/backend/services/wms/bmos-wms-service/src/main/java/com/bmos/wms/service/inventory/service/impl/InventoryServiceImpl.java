package com.bmos.wms.service.inventory.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.bmos.wms.common.enums.inventory.CargoInventoryOperateLogType;
import com.bmos.wms.common.enums.inventory.PositionInventoryOperateLogType;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.cargo.model.CargoCategory;
import com.bmos.wms.service.cargo.service.ICargoCategoryService;
import com.bmos.wms.service.inventory.convert.InventoryConvert;
import com.bmos.wms.service.inventory.dto.*;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.mapper.IInventoryMapper;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.inventory.service.IInventoryService;
import com.bmos.wms.service.inventory.vo.*;
import com.bmos.wms.service.businessLog.service.ICargoLogService;
import com.bmos.wms.service.businessLog.service.IPositionLogService;
import com.bmos.wms.service.platform.code.PlatformCodeConstants;
import com.bmos.wms.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.wms.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.wms.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.wms.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.wms.service.platform.code.vo.NextCodeVO;
import com.bmos.wms.service.platform.user.FeignUtils;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.position.service.ICargoPositionService;
import com.bmos.wms.service.position.vo.CargoPositionVO;
import com.bmos.wms.service.reserve.mapper.IInventoryReserveMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 货品库存 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 14:23
 */
@Service
@Slf4j
public class InventoryServiceImpl implements IInventoryService {

    private static final String LOG_PREFIX = "[货品库存]";

    @Resource
    private IInventoryMapper inventoryMapper;

    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;

    @Resource
    private ICargoMapper cargoMapper;

    @Resource
    private ICargoCategoryService cargoCategoryService;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ICargoLogService cargoLogService;

    @Resource
    private IPositionLogService positionLogService;

    @Resource
    private IInventoryReserveMapper inventoryReserveMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InventoryVO> inbound(InventoryInboundDTO dto) {
        log.info("{}货品库存入库:{}", LOG_PREFIX, dto);
        // 判断货品id是否存在
        Cargo cargo = cargoMapper.selectById(dto.getCargoId());
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        CargoPosition cargoPosition = cargoPositionService.getByIdWithPermission(dto.getPositionId());
        if (cargoPosition == null) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_EXIST);
        }
        // 根据货品id和批次号查询是否已存在库存批次
        InventoryBatch existBatch = inventoryBatchMapper.selectByCargoIdAndBatchNo(dto.getCargoId(), dto.getBatchNo());
        Long batchId;
        if (existBatch != null) {
            // 比对有效期、原始批号、是否匹配
            if (!(Objects.equals(existBatch.getFactoryBatchNo(), dto.getFactoryBatchNo())
                    && Objects.equals(existBatch.getExpiredDate(), dto.getExpiredDate()))) {
                throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_EXIST);
            }
            // 沿用批次
            batchId = existBatch.getId();
        } else {
            // 新增批次
            InventoryBatch inventoryBatch = new InventoryBatch();
            inventoryBatch.setCargoId(dto.getCargoId());
            inventoryBatch.setBatchNo(dto.getBatchNo());
            inventoryBatch.setFactoryBatchNo(dto.getFactoryBatchNo());
            inventoryBatch.setProduceDate(dto.getProduceDate());
            inventoryBatch.setExpiredDate(dto.getExpiredDate());
            inventoryBatch.setHydration(dto.getHydration());
            inventoryBatch.setNoHydrationContent(dto.getNoHydrationContent());
            inventoryBatch.setUnitId(cargo.getUnitId());
            inventoryBatch.setAvailable(!LocalDate.now().isAfter(dto.getExpiredDate()));
            inventoryBatch.setQualityStatus(com.bmos.wms.common.enums.inspect.MaterialQualityStatusEnum.QUARANTINE.getValue());
            inventoryBatchMapper.insert(inventoryBatch);
            batchId = inventoryBatch.getId();
        }
        List<Inventory> list = new ArrayList<>();
        boolean withOdd = false;
        // 零头
        if (dto.getOddQuantity() != null) {
            withOdd = true;
        }
        BatchNextCodeVO batchCode = FeignUtils.handleRequest(data -> platformCodeFeign.getBatchNextUseNo(data), BatchNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.WMS_INVENTORY_SERIAL)
                .fields(new HashMap<>())
                .num(withOdd ? dto.getSize() + 1 : dto.getSize())
                .build()).getData();
        List<String> nos = batchCode.getNos().stream().map(NextCodeVO::getNo).collect(Collectors.toList());
        // 保存货品件
        for (int i = 0; i < dto.getSize(); i++) {
            Inventory inventory = new Inventory();
            inventory.setCargoId(dto.getCargoId());
            inventory.setInventoryBatchId(batchId);
            inventory.setPositionId(dto.getPositionId());
            inventory.setNo(nos.get(i));
            BigDecimal initQuantity = unitCache.toBasic(dto.getSingleQuantity(), dto.getSingleUnitId());
            inventory.setInitQuantity(initQuantity);
            inventory.setAvailableQuantity(initQuantity);
            inventory.setConsumeQuantity(BigDecimal.ZERO);
            inventory.setReserveQuantity(BigDecimal.ZERO);
            inventory.setUnitId(dto.getSingleUnitId());
            list.add(inventory);
        }
        if (withOdd) {
            Inventory inventory = new Inventory();
            inventory.setCargoId(dto.getCargoId());
            inventory.setInventoryBatchId(batchId);
            inventory.setPositionId(dto.getPositionId());
            inventory.setNo(nos.get(dto.getSize()));
            BigDecimal oddQuantity = unitCache.toBasic(dto.getOddQuantity(), dto.getOddUnitId());
            inventory.setInitQuantity(oddQuantity);
            inventory.setAvailableQuantity(oddQuantity);
            inventory.setConsumeQuantity(BigDecimal.ZERO);
            inventory.setReserveQuantity(BigDecimal.ZERO);
            inventory.setUnitId(dto.getOddUnitId());
            list.add(inventory);
        }
        if (CollectionUtil.isNotEmpty(list)) {
            inventoryMapper.insertBatch(list);
        }
        // 确认编码
        FeignUtils.handleRequest(data -> platformCodeFeign.batchConfirmNo(data), BatchConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.WMS_INVENTORY_SERIAL)
                .fields(new HashMap<>())
                .fullNos(nos)
                .build());

        List<BigDecimal> quantities = list.stream()
                .map(Inventory::getInitQuantity)
                .collect(Collectors.toList());
        cargoLogService.saveCargoLog(CargoInventoryOperateLogType.INBOUND, list, quantities,
                new String[]{dto.getSenderId(), dto.getReceiverId()}, null, dto.getLinkExplain());

        positionLogService.savePositionLog(PositionInventoryOperateLogType.INBOUND, list, quantities,
                new String[]{dto.getSenderId(), dto.getReceiverId()}, null, dto.getLinkExplain());

        return list.stream().map(inventory -> queryInventoryById(inventory.getId())).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outbound(InventoryOutboundDTO dto) {
        log.info("{}货品库存出库:{}", LOG_PREFIX, dto);
        Long batchId = dto.getInventoryBatchId();
        InventoryBatch inventoryBatch = inventoryBatchMapper.selectById(batchId);
        if (inventoryBatch == null) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        Map<Long, BigDecimal> map = dto.getInventories().stream()
                .collect(Collectors.toMap(InventoryOutboundDTO.OutBoundDTO::getId, InventoryOutboundDTO.OutBoundDTO::getQuantity, (k1, k2) -> k2));
        Set<Long> inventoryIds = map.keySet();
        List<Inventory> inventories = inventoryMapper.selectBatchIds(inventoryIds);
        if (inventories.size() != inventoryIds.size()) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        // 批次校验
        Set<Long> batchIds = inventories.stream().map(Inventory::getInventoryBatchId).collect(Collectors.toSet());
        if (batchIds.size() > 1) {
            // 存在多个物料批次 仅可以同时出库同一个批次的物料
            throw new BmosException(WmsResponseCode.INVENTORY_TOO_MANY_BATCH);
        }

        // 判断出库后剩余量是否满足批次预定量
        BigDecimal availableQuantity = inventoryMapper.queryAvailableQuantityByBatchId(batchId);
        BigDecimal reserveQuantity = inventoryReserveMapper.queryReserveQuantityByBatchId(batchId);
        // 差值
        List<BigDecimal> quantities = new ArrayList<>();
        // 开始扣除库存
        for (Inventory inventory : inventories) {
            BigDecimal out = unitCache.toBasic(map.get(inventory.getId()), inventory.getUnitId());
            if (out.compareTo(inventory.getAvailableQuantity()) > 0) {
                throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_OUTBOUND_NOT_ENOUGH);
            }
            quantities.add(out);
            inventory.setConsumeQuantity(inventory.getConsumeQuantity().add(out));
            inventory.setAvailableQuantity(inventory.getAvailableQuantity().subtract(out));
        }
        BigDecimal consumeQuantity = quantities.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (consumeQuantity.add(reserveQuantity).compareTo(availableQuantity) > 0){
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_OUTBOUND_NOT_ENOUGH);
        }
        inventoryMapper.updateBatch(inventories);
        cargoLogService.saveCargoLog(CargoInventoryOperateLogType.OUTBOUND, inventories, quantities,
                new String[]{dto.getSenderId(), dto.getReceiverId()}, null, dto.getLinkExplain());
        positionLogService.savePositionLog(PositionInventoryOperateLogType.OUTBOUND, inventories, quantities,
                new String[]{dto.getSenderId(), dto.getReceiverId()}, null, dto.getLinkExplain());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move(InventoryMoveDTO dto) {
        log.info("{}货品库存移库:{}", LOG_PREFIX, dto);
        Long batchId = dto.getInventoryBatchId();
        InventoryBatch inventoryBatch = inventoryBatchMapper.selectById(batchId);
        if (inventoryBatch == null) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        List<Long> inventoryIds = dto.getInventoryIds();
        List<Inventory> inventories = inventoryMapper.selectBatchIds(inventoryIds);
        if (inventories.size() != inventoryIds.size()) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        Long targetPositionId = dto.getTargetPositionId();
        // 带权限查询货位信息
        CargoPosition cargoPosition = cargoPositionService.getByIdWithPermission(targetPositionId);
        if (cargoPosition == null) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_EXIST);
        }
        if (!cargoPosition.getEnable()) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_DISABLED);
        }
        for (Inventory inventory : inventories) {
            inventory.setPositionId(targetPositionId);
        }
        inventoryMapper.updateBatch(inventories);

        List<BigDecimal> quantities = inventories.stream().map(Inventory::getAvailableQuantity).collect(Collectors.toList());
        cargoLogService.saveCargoLog(CargoInventoryOperateLogType.MOVE, inventories, quantities,
                new String[]{dto.getMoverId(), dto.getMoverId()}, null, dto.getLinkExplain());
        positionLogService.savePositionLog(PositionInventoryOperateLogType.MOVE, inventories, quantities,
                new String[]{dto.getMoverId(), dto.getMoverId()}, null, dto.getLinkExplain());
    }

    @Override
    public CommonPage<InventoryBatchVO> queryBatchPage(InventoryBatchPageQuery pageQuery) {
        List<Inventory> list;
        List<Long> positionIdList = new ArrayList<>();
        Long positionId = pageQuery.getPositionId();
        if (positionId == null || cargoPositionService.queryInfoById(positionId) == null) {
            List<CargoPosition> cargoPositions = cargoPositionService.queryAllEnabledChildrenByStorageId(positionId);
            if (CollectionUtil.isEmpty(cargoPositions)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
            }
            List<Long> collect = cargoPositions.stream().map(CargoPosition::getId).collect(Collectors.toList());
            collect.add(positionId);
            positionIdList.addAll(collect);
            list = inventoryMapper.queryListByPositionIds(collect);
        } else {
            positionIdList.add(positionId);
            // 传的是货位id
            list = inventoryMapper.queryListByPositionId(positionId);
        }
        Set<Long> storageMaterialBatchIdList = list.stream()
                .map(Inventory::getInventoryBatchId)
                .collect(Collectors.toSet());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<InventoryBatchVO> result = inventoryBatchMapper.queryList(pageQuery, storageMaterialBatchIdList);
        CommonPage<InventoryBatchVO> page = CommonPage.convertPage(result);
        List<Long> batchIds = page.getList().stream()
                .map(InventoryBatchVO::getId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(batchIds)) {
            return page;
        }
        Map<Long, List<Inventory>> group = inventoryMapper.queryListByBatchIdsAndPositionId(batchIds, positionIdList)
                .stream()
                .collect(Collectors.groupingBy(Inventory::getInventoryBatchId));

        for (InventoryBatchVO vo : page.getList()) {
            CacheUnit unit = unitCache.getGlobalUnit(vo.getUnitId());
            if (unit != null) {
                vo.setUnit(unit.getUnitName());
                vo.setRate(unit.getRate());
            }

            List<Inventory> inventories = group.get(vo.getId()) == null ? new ArrayList<>() : group.get(vo.getId());
            // 统计件数
            vo.setSize(inventories.size());
            BigDecimal available = inventories.stream().map(Inventory::getAvailableQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal reserve = inventories.stream().map(Inventory::getReserveQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setAvailableQuantity(available);
            vo.setReserveQuantity(reserve);
            vo.setQuantity(available.add(reserve));
        }
        page.getList().forEach(item -> {
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
        });
        PrecisionHelper.convertUnitRenderList(page.getList());
        return page;
    }

    @Override
    public CommonPage<InventoryVO> queryPageByBatchId(InventoryPageQueryWithBatchId pageQuery) {
        List<Long> positionIdList = new ArrayList<>();
        Long positionId = pageQuery.getPositionId();
        if (positionId == null || cargoPositionService.queryInfoById(positionId) == null) {
            List<CargoPosition> cargoPositions = cargoPositionService.queryAllEnabledChildrenByStorageId(positionId);
            if (CollectionUtil.isEmpty(cargoPositions)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, pageQuery);
            }
            List<Long> collect = cargoPositions.stream().map(CargoPosition::getId).collect(Collectors.toList());
            collect.add(positionId);
            positionIdList.addAll(collect);
        } else {
            // 传的是货位id
            positionIdList.add(positionId);
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<InventoryVO> list = inventoryMapper.queryPageWithPositionIds(pageQuery, positionIdList);
        CommonPage<InventoryVO> page = CommonPage.convertPage(list);
        page.getList().forEach(item -> {
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
        });
        PrecisionHelper.convertUnitRenderList(page.getList());
        return page;
    }

    @Override
    public CommonPage<CargoInventoryVO> queryPageByCargoIds(InventoryPageQueryWithCargoId pageQuery) {
        List<Long> cargoIds;
        Long cargoId = pageQuery.getCargoId();
        if (cargoId != null) {
            Cargo cargo = cargoMapper.selectById(cargoId);
            if (cargo == null) {
                throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
            }
            cargoIds = Collections.singletonList(cargoId);
        } else if (pageQuery.getCargoCategoryId() != null) {
            List<CargoCategory> allCargoCategoryList = cargoCategoryService.queryAllChildren(pageQuery.getCargoCategoryId());
            if (CollectionUtil.isEmpty(allCargoCategoryList)) {
                cargoIds = new ArrayList<>();
            } else {
                List<Cargo> list = cargoMapper.selectByCargoCategoryIds(allCargoCategoryList.stream()
                        .map(CargoCategory::getId)
                        .collect(Collectors.toList()));
                cargoIds = list.stream().map(Cargo::getId).collect(Collectors.toList());
            }
        } else {
            cargoIds = null;
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<CargoInventoryVO> list = inventoryMapper.queryPageWithCargoIds(pageQuery, cargoIds);
        CommonPage<CargoInventoryVO> page = CommonPage.convertPage(list);
        // 统计每行的总量
        List<Inventory> inventories = inventoryMapper.queryListByCargoIds(cargoIds);
        List<Long> inventoriesCargoIds = inventories.stream().map(Inventory::getCargoId).collect(Collectors.toList());
        Map<Long, BigDecimal> reserveQuantitMap = inventoryReserveMapper.getReserveQuantityByCargoIdList(inventoriesCargoIds);
        if (CollectionUtil.isNotEmpty(inventories)) {
            Map<Long, List<Inventory>> group = inventories.stream().collect(Collectors.groupingBy(Inventory::getCargoId));
            for (CargoInventoryVO inventory : page.getList()) {
                List<Inventory> groupList = group.get(inventory.getId());
                if (groupList == null) {
                    inventory.setSize(0);
                    inventory.setAvailableQuantity(BigDecimal.ZERO);
                    inventory.setReserveQuantity(BigDecimal.ZERO);
                    inventory.setQuantity(BigDecimal.ZERO);
                } else {
                    inventory.setSize(groupList.size());
                    // 求和
                    BigDecimal total = groupList.stream().map(Inventory::getAvailableQuantity)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal reserve = reserveQuantitMap.getOrDefault(inventory.getId(), BigDecimal.ZERO);
                    inventory.setQuantity(total);
                    inventory.setReserveQuantity(reserve);
                    inventory.setAvailableQuantity(total.subtract(reserve));
                }
            }
        } else {
            page.getList().forEach(inventory -> {
                inventory.setSize(0);
                inventory.setAvailableQuantity(BigDecimal.ZERO);
                inventory.setReserveQuantity(BigDecimal.ZERO);
                inventory.setQuantity(BigDecimal.ZERO);
            });
        }
        page.getList().forEach(item -> {
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
        });
        PrecisionHelper.convertUnitRenderList(page.getList());
        return page;
    }

    @Override
    public CommonPage<CargoInventoryBatchVO> queryBatchPageByCargoIds(InventoryBatchPageQueryWithCargoId pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<CargoInventoryBatchVO> list = inventoryBatchMapper.queryBatchList(pageQuery);
        List<Long> inventoryBatchIds = list.stream().map(CargoInventoryBatchVO::getId).collect(Collectors.toList());
        Map<Long, BigDecimal> reserveMap = inventoryReserveMapper.getReserveQuantityByInventoryBatchIdList(inventoryBatchIds);
        CommonPage<CargoInventoryBatchVO> page = CommonPage.convertPage(list);
        page.getList().forEach(item -> {
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
            BigDecimal total = item.getAvailableQuantity();
            BigDecimal reserve = reserveMap.getOrDefault(item.getId(), BigDecimal.ZERO);
            item.setQuantity(total);
            item.setReserveQuantity(reserve);
            item.setAvailableQuantity(total.subtract(reserve));
        });
        PrecisionHelper.convertUnitRenderList(page.getList());
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addInventoryBatch(InventoryBatchCreateDTO dto) {

        Long cargoId = dto.getCargoId();
        Cargo cargo = cargoMapper.selectById(cargoId);
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }

        // 根据货品id和批次号查询是否已存在库存批次
        InventoryBatch existBatch = inventoryBatchMapper.selectByCargoIdAndBatchNo(dto.getCargoId(), dto.getBatchNo());
        if (existBatch != null) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_EXIST);
        }
        // 新增批次
        InventoryBatch inventoryBatch = new InventoryBatch();
        inventoryBatch.setCargoId(dto.getCargoId());
        inventoryBatch.setBatchNo(dto.getBatchNo());
        inventoryBatch.setFactoryBatchNo(dto.getFactoryBatchNo());
        inventoryBatch.setProduceDate(dto.getProduceDate());
        inventoryBatch.setExpiredDate(dto.getExpiredDate());
        inventoryBatch.setHydration(dto.getHydration());
        inventoryBatch.setNoHydrationContent(dto.getNoHydrationContent());
        inventoryBatch.setUnitId(cargo.getUnitId());
        inventoryBatch.setAvailable(!LocalDate.now().isAfter(dto.getExpiredDate()));
        inventoryBatch.setReportNo(dto.getReportNo());
        inventoryBatch.setLicenceNo(dto.getLicenceNo());
        inventoryBatchMapper.insert(inventoryBatch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editInventoryBatch(InventoryBatchEditDTO dto) {
        Long batchId = dto.getBatchId();
        InventoryBatch inventoryBatch = inventoryBatchMapper.selectById(batchId);
        if (inventoryBatch == null) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        inventoryBatchMapper.update(null, Wrappers.lambdaUpdate(InventoryBatch.class)
                .eq(InventoryBatch::getId, batchId)
                .set(InventoryBatch::getFactoryBatchNo, dto.getFactoryBatchNo())
                .set(InventoryBatch::getProduceDate, dto.getProduceDate())
                .set(InventoryBatch::getExpiredDate, dto.getExpiredDate())
                .set(InventoryBatch::getAvailable, !LocalDate.now().isAfter(dto.getExpiredDate()))
                .set(InventoryBatch::getHydration, dto.getHydration())
                .set(InventoryBatch::getNoHydrationContent, dto.getNoHydrationContent())
                .set(InventoryBatch::getReportNo, dto.getReportNo())
                .set(InventoryBatch::getLicenceNo, dto.getLicenceNo())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addInventory(InventoryCreateDTO dto) {
        Long cargoId = dto.getCargoId();
        Cargo cargo = cargoMapper.selectById(cargoId);
        if (cargo == null) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        Long batchId = dto.getBatchId();
        InventoryBatch inventoryBatch = inventoryBatchMapper.selectById(batchId);
        if (inventoryBatch == null) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        Long positionId = dto.getPositionId();
        CargoPositionVO cargoPosition = cargoPositionService.queryInfoById(positionId);
        if (cargoPosition == null) {
            throw new BmosException(WmsResponseCode.CARGO_POSITION_NOT_EXIST);
        }

        BatchNextCodeVO batchCode = FeignUtils.handleRequest(data -> platformCodeFeign.getBatchNextUseNo(data), BatchNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.WMS_INVENTORY_SERIAL)
                .fields(new HashMap<>())
                .num(dto.getSize())
                .build()).getData();
        List<String> nos = batchCode.getNos().stream().map(NextCodeVO::getNo).collect(Collectors.toList());
        List<Inventory> list = new ArrayList<>();
        for (int i = 0; i < dto.getSize(); i++) {
            Inventory inventory = new Inventory();
            inventory.setCargoId(dto.getCargoId());
            inventory.setInventoryBatchId(batchId);
            inventory.setPositionId(dto.getPositionId());
            inventory.setNo(nos.get(i));
            BigDecimal initQuantity = unitCache.toBasic(dto.getSingleQuantity(), dto.getSingleUnitId());
            inventory.setInitQuantity(initQuantity);
            inventory.setAvailableQuantity(initQuantity);
            inventory.setConsumeQuantity(BigDecimal.ZERO);
            inventory.setReserveQuantity(BigDecimal.ZERO);
            inventory.setUnitId(dto.getSingleUnitId());
            list.add(inventory);
        }
        inventoryMapper.insertBatch(list);
        // 确认编码
        FeignUtils.handleRequest(data -> platformCodeFeign.batchConfirmNo(data), BatchConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.WMS_INVENTORY_SERIAL)
                .fields(new HashMap<>())
                .fullNos(nos)
                .build());
        // 仅生成货品日志
        cargoLogService.saveCargoLog(CargoInventoryOperateLogType.ADD, list, list.stream().map(Inventory::getInitQuantity).collect(Collectors.toList()),
                new String[]{dto.getOperatorId()}, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void check(InventoryCheckDTO dto) {
        log.info("{}物料盘库:{}", LOG_PREFIX, dto);
        Inventory inventory = inventoryMapper.selectById(dto.getInventoryId());
        if (inventory == null) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_NOT_EXIST);
        }
        // 盘库前
        BigDecimal before = inventory.getAvailableQuantity();
        // 本次消耗
        BigDecimal toConsume = unitCache.toBasic(dto.getConsumeQuantity(), inventory.getUnitId());
        inventory.setInitQuantity(unitCache.toBasic(dto.getInitQuantity(), inventory.getUnitId()));
        if (dto.getUseUp()) {
            inventory.setAvailableQuantity(BigDecimal.ZERO);
            inventory.setConsumeQuantity(inventory.getInitQuantity());
        } else {
            inventory.setAvailableQuantity(inventory.getInitQuantity().subtract(toConsume));
            inventory.setConsumeQuantity(toConsume);
        }
        // 盘库后
        BigDecimal after = inventory.getAvailableQuantity();
        if (inventory.getInitQuantity().compareTo(inventory.getAvailableQuantity().add(inventory.getConsumeQuantity())) != 0) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_CHECK_QUANTITY_ERROR);
        }
        inventoryMapper.updateById(inventory);
        // 增量
        BigDecimal change;
        PositionInventoryOperateLogType operateType;
        if (after.compareTo(before) > 0) {
            // 盘增
            change = after.subtract(before).abs();
            operateType = PositionInventoryOperateLogType.CHECK_PLUS;
        } else {
            // 盘减
            change = before.subtract(after).abs();
            operateType = PositionInventoryOperateLogType.CHECK_MINUS;
        }
        cargoLogService.saveCargoLog(CargoInventoryOperateLogType.CHECK, inventory, change, new String[]{dto.getCheckerId(), dto.getReCheckerId()}, null, dto.getRemark());
        positionLogService.savePositionLog(operateType, inventory, change, new String[]{dto.getCheckerId(), dto.getReCheckerId()}, null, dto.getRemark());
    }

    @Override
    public List<CargoInventoryBatchItemVO> listByCargoIdAndBatchNo(Long cargoId, String inventoryBatchNo) {
        List<InventoryBatch> list = inventoryBatchMapper.listByCargoIdAndBatchNo(cargoId, inventoryBatchNo);
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(item -> new CargoInventoryBatchItemVO(item.getId(), item.getBatchNo(), item.getFactoryBatchNo(), item.getExpiredDate(), item.getProduceDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryVO> listByBatchIdAndPositionId(Long inventoryBatchId, Long positionId) {
        if (inventoryBatchId == null || positionId == null) {
            return new ArrayList<>();
        }
        List<InventoryVO> list = inventoryMapper.queryListByBatchIdAndPositionId(inventoryBatchId, positionId);
        list.forEach(item -> item.setUnit(unitCache.getGlobalUnitName(item.getUnitId())));
        PrecisionHelper.convertUnitRenderList(list);
        return list;
    }

    @Nullable
    @Override
    public CargoInventoryBatchDetailVO queryInventoryBatchById(Long inventoryBatchId) {
        if (inventoryBatchId == null) {
            return null;
        }
        InventoryBatch inventoryBatch = inventoryBatchMapper.selectById(inventoryBatchId);
        if (inventoryBatch == null) {
            return null;
        }
        Long cargoId = inventoryBatch.getCargoId();
        Cargo cargo = cargoMapper.selectById(cargoId);
        CargoInventoryBatchDetailVO detailVO = InventoryConvert.INSTANCE.convertToDetailVO(inventoryBatch);
        detailVO.setUnit(unitCache.getGlobalUnitName(inventoryBatch.getUnitId()));
        if (cargo != null) {
            detailVO.setCargoName(cargo.getCargoName());
            detailVO.setMergeCode(cargo.getMergeCode());
            detailVO.setSpecification(cargo.getSpecification());
            detailVO.setSupplier(cargo.getSupplier());
            detailVO.setProducer(cargo.getProducer());
        }
        List<Inventory> inventories = inventoryMapper.queryListByBatchId(inventoryBatchId);
        detailVO.setAvailableSize(inventories.size());
        BigDecimal reservedQuantity = inventoryReserveMapper.getReserveQuantityByInventoryBatchId(inventoryBatchId);
        BigDecimal aq = inventories.stream().map(Inventory::getAvailableQuantity).reduce(BigDecimal.ZERO, BigDecimal::add).subtract(reservedQuantity);
        BigDecimal rq = inventories.stream().map(Inventory::getReserveQuantity).reduce(BigDecimal.ZERO, BigDecimal::add).add(reservedQuantity);
        detailVO.setAvailableQuantity(PrecisionHelper.precision(unitCache.toExt(aq, detailVO.getUnitId()), detailVO.getUnitId()));
        detailVO.setReserveQuantity(PrecisionHelper.precision(unitCache.toExt(rq, detailVO.getUnitId()), detailVO.getUnitId()));
        detailVO.setQuantity(PrecisionHelper.precision(unitCache.toExt(aq.add(rq), detailVO.getUnitId()), detailVO.getUnitId()));
        return detailVO;
    }

    @Override
    public List<InventoryBatchListVO> queryBatchByMaterial(InventoryBatchQueryDTO dto) {
        List<InventoryBatchListVO> result = inventoryBatchMapper.queryBatchListByMaterial(dto);
        Map<Long, BigDecimal> reserveMap = inventoryReserveMapper.getReserveQuantityByInventoryBatchIdList(CollectionUtils.convertList(result, InventoryBatchListVO::getId));
        result.forEach(item -> {
            BigDecimal total = item.getAvailableQuantity();
            BigDecimal reserve = reserveMap.getOrDefault(item.getId(), BigDecimal.ZERO);
            item.setAvailableQuantity(total.subtract(reserve));
        });
        return result.stream().filter(e-> e.getAvailableQuantity().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
    }

    @Override
    public List<InventoryAvailableQuantityListVO> queryAvailableQuantityList(InventoryAvailableQuantityQueryDTO dto) {
        List<InventoryAvailableQuantityListVO> result = inventoryMapper.queryAvailableQuantityList(dto);
        result.stream().filter(e -> e.getInventoryQuantity()
                .compareTo(BigDecimal.ZERO) < 0).forEach(inventoryAvailableQuantityListVO -> inventoryAvailableQuantityListVO.setInventoryQuantity(BigDecimal.ZERO));
        return result;
    }

    @Override
    public List<CargoInventoryItemVO> listByCargoIdAndBatchId(Long cargoId, Long inventoryBatchId) {
        List<CargoInventoryItemVO> result = inventoryMapper.listByCargoIdAndBatchId(cargoId, inventoryBatchId);
        result.forEach(item -> {
            item.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
        });
        PrecisionHelper.convertUnitRenderList(result);
        return result;
    }

    @Nullable
    @Override
    public InventoryVO queryInventoryById(Long inventoryId) {
        if (inventoryId == null) {
            return null;
        }
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            return null;
        }
        InventoryVO result = InventoryConvert.INSTANCE.convertToVO(inventory);
        result.setInventoryNo(inventory.getNo());
        Optional.ofNullable(inventory.getPositionId())
                .map(cargoPositionService::queryInfoById).ifPresent(position -> result.setPosition(position.getPosition()));
        Optional.ofNullable(inventory.getCargoId())
                .map(cargoMapper::selectById)
                .ifPresent(cargo -> {
                    result.setCargoCode(cargo.getCargoCode());
                    result.setMergeCode(cargo.getMergeCode());
                    result.setCargoName(cargo.getCargoName());
                    result.setSpecification(cargo.getSpecification());
                });
        Optional.ofNullable(inventory.getInventoryBatchId())
                .map(inventoryBatchMapper::selectById)
                .ifPresent(inventoryBatch -> {
                    result.setBatchNo(inventoryBatch.getBatchNo());
                    result.setExpiredDate(inventoryBatch.getExpiredDate());
                });
        result.setUnit(unitCache.getGlobalUnitName(inventory.getUnitId()));
        result.setInitQuantity(unitCache.toExt(inventory.getInitQuantity(), inventory.getUnitId()));
        result.setAvailableQuantity(unitCache.toExt(inventory.getAvailableQuantity(), inventory.getUnitId()));
        result.setReserveQuantity(unitCache.toExt(inventory.getReserveQuantity(), inventory.getUnitId()));
        return result;
    }
}
