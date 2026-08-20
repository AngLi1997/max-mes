package com.bmos.wms.service.sendout.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.wms.common.enums.inventory.CargoInventoryOperateLogType;
import com.bmos.wms.common.enums.inventory.PositionInventoryOperateLogType;
import com.bmos.wms.common.enums.sendout.SendOrderStatus;
import com.bmos.wms.common.enums.sendout.SendOrderType;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.service.businessLog.service.ICargoLogService;
import com.bmos.wms.service.businessLog.service.IPositionLogService;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.mapper.IInventoryMapper;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.inventory.vo.CargoInventoryRealQuantity;
import com.bmos.wms.service.mes.dto.SendOutFeignDTO;
import com.bmos.wms.service.mes.feigns.MesFeignClient;
import com.bmos.wms.service.platform.user.FeignUtils;
import com.bmos.wms.service.reserve.mapper.IInventoryReserveMapper;
import com.bmos.wms.service.reserve.model.InventoryReserve;
import com.bmos.wms.service.sendout.convert.SendOutOrderConvert;
import com.bmos.wms.service.sendout.dto.SendOutDTO;
import com.bmos.wms.service.sendout.dto.SendPageQuery;
import com.bmos.wms.service.sendout.dto.SendSubmitDTO;
import com.bmos.wms.service.sendout.mapper.ISendOutOrderItemMapper;
import com.bmos.wms.service.sendout.mapper.ISendOutOrderMapper;
import com.bmos.wms.service.sendout.model.SendOutOrder;
import com.bmos.wms.service.sendout.model.SendOutOrderItem;
import com.bmos.wms.service.sendout.service.ISendOutOrderService;
import com.bmos.wms.service.sendout.vo.SendOrderItemVO;
import com.bmos.wms.service.sendout.vo.SendOutOrderDetailVO;
import com.bmos.wms.service.sendout.vo.SendOutOrderVO;
import com.bmos.wms.service.utils.UserUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 09:33
 */
@Service
@Slf4j
public class SendOutOrderServiceImpl implements ISendOutOrderService {

    private static final String LOG_PREFIX = "[WMS发料]";

    @Resource
    private ISendOutOrderMapper sendOutOrderMapper;

    @Resource
    private ISendOutOrderItemMapper sendOutOrderItemMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;

    @Resource
    private IInventoryMapper inventoryMapper;

    @Resource
    private ICargoMapper cargoMapper;


    @Resource
    private IInventoryReserveMapper inventoryReserveMapper;

    @Resource
    private ICargoLogService cargoLogService;

    @Resource
    private IPositionLogService positionLogService;

    @Resource
    private MesFeignClient mesFeignClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitSendOutOrderByBatch(SendSubmitDTO dto) {

        if (sendOutOrderMapper.existRequisitionPlanId(dto.getRequisitionPlanId())) {
            log.error("该领料计划id已存在发料工单");
            throw new BmosException(WmsResponseCode.REQUISITION_PLAN_ID_EXIST);
        }

        // 校验库存是否足够
        validateQuantity(dto);

        List<InventoryReserve> inventoryReserveList = new ArrayList<>();

        // 保存领料计划订单
        SendOutOrder order = new SendOutOrder();
        order.setRequisitionPlanId(dto.getRequisitionPlanId());
        order.setProductId(dto.getProductId());
        order.setProductCode(dto.getProductCode());
        order.setProductName(dto.getProductName());
        order.setProductSpecification(dto.getProductSpecification());
        order.setProcessId(dto.getProcessId());
        order.setProcessName(dto.getProcessName());
        order.setBatchNo(dto.getBatchNo());
        order.setPullOrderNo(dto.getPullOrderNo());
        order.setSubmitterId(dto.getSubmitterId());
        order.setSubmitTime(LocalDateTime.now());
        order.setSendOrderType(SendOrderType.getByValue(dto.getSendOrderType()));
        order.setSendOrderStatus(SendOrderStatus.PENDING);
        sendOutOrderMapper.insert(order);

        boolean byBatch = Objects.equals(dto.getSendOrderType(), SendOrderType.BATCH.getValue());
        Map<Long, Long> batchCargoIdMap = byBatch ? inventoryBatchMapper.selectBatchIds(dto.getPendingSendList().stream()
                .map(SendSubmitDTO.SendSubmitQuantityDTO::getBusinessId)
                .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(InventoryBatch::getId, InventoryBatch::getCargoId)) : new HashMap<>();

        // 保存需要下发的物料
        List<SendOutOrderItem> list = new ArrayList<>();
        for (SendSubmitDTO.SendSubmitQuantityDTO itemDTO : dto.getPendingSendList()) {
            BigDecimal reserveQuantity = unitCache.toBasic(itemDTO.getTargetQuantity(), itemDTO.getUnitId());
            SendOutOrderItem item = new SendOutOrderItem();
            item.setSendOrderId(order.getId());
            item.setSendOrderType(SendOrderType.getByValue(dto.getSendOrderType()));
            if (byBatch) {
                item.setInventoryBatchId(itemDTO.getBusinessId());
                item.setCargoId(batchCargoIdMap.get(itemDTO.getBusinessId()));
            } else {
                item.setCargoId(itemDTO.getBusinessId());
            }
            item.setReserveQuantity(reserveQuantity);
            item.setUnitId(itemDTO.getUnitId());
            list.add(item);
            // 新增预定信息
            InventoryReserve reserve;
            if (Objects.equals(SendOrderType.getByValue(dto.getSendOrderType()), SendOrderType.BATCH)) {
                reserve = InventoryReserve.builder()
                        .inventoryBatchId(itemDTO.getBusinessId())
                        .cargoId(batchCargoIdMap.get(itemDTO.getBusinessId()))
                        .requisitionPlanId(dto.getRequisitionPlanId())
                        .reserveQuantity(reserveQuantity)
                        .reserveTime(LocalDateTime.now())
                        .build();

            } else {
                reserve = InventoryReserve.builder()
                        .cargoId(itemDTO.getBusinessId())
                        .requisitionPlanId(dto.getRequisitionPlanId())
                        .reserveQuantity(reserveQuantity)
                        .reserveTime(LocalDateTime.now())
                        .build();
            }
            inventoryReserveList.add(reserve);
        }
        sendOutOrderItemMapper.insertBatch(list);
        if (CollectionUtil.isNotEmpty(inventoryReserveList)) {
            inventoryReserveMapper.insertBatch(inventoryReserveList);
        }
    }

    @Override
    public CommonPage<SendOutOrderVO> queryPage(SendPageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<SendOutOrder> list = sendOutOrderMapper.queryPendingPage(pageQuery);
        CommonPage<SendOutOrder> page = CommonPage.convertPage(list);
        CommonPage<SendOutOrderVO> result = SendOutOrderConvert.INSTANCE.convertToVO(page);
        result.getList().forEach(item -> {
            BaseUserDO user = UserUtils.getUser(item.getSubmitterId());
            if (user != null) {
                item.setSubmitterName(user.getUserName() + "-" + user.getLoginName());
            }
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSendOut(Long id) {
        SendOutOrder order = sendOutOrderMapper.selectById(id);
        if (order == null) {
            throw new BmosException(WmsResponseCode.SEND_ORDER_NOT_EXIST);
        }
        SendOrderStatus sendOrderStatus = order.getSendOrderStatus();
        if (!Objects.equals(sendOrderStatus, SendOrderStatus.PENDING)) {
            // 非待发放状态无法取消
            throw new BmosException(WmsResponseCode.SEND_ORDER_NOT_ALLOWED_CANCEL);
        }
        order.setSendOrderStatus(SendOrderStatus.CANCELED);
        order.setCancelTime(LocalDateTime.now());
        sendOutOrderMapper.updateById(order);

        // 处理预定信息
        Long requisitionPlanId = order.getRequisitionPlanId();
        inventoryReserveMapper.deleteByRequisitionPlanId(requisitionPlanId);

        log.info("{}通知mes取消发料:requisitionPlanId:{}", LOG_PREFIX, order.getRequisitionPlanId());
        // 通知mes 取消发料
        FeignUtils.handleRequest((rpId) -> mesFeignClient.cancelSendOut(rpId), order.getRequisitionPlanId());
    }

    @Nullable
    @Override
    public SendOutOrderDetailVO queryDetail(Long id) {
        SendOutOrder order = sendOutOrderMapper.selectById(id);
        if (order == null) {
            return null;
        }
        boolean byBatch = Objects.equals(order.getSendOrderType(), SendOrderType.BATCH);
        SendOutOrderDetailVO result = SendOutOrderConvert.INSTANCE.convertToDetailVO(order);
        BaseUserDO user = UserUtils.getUser(result.getSubmitterId());
        if (user != null) {
            result.setSubmitterName(user.getUserName() + "-" + user.getLoginName());
        }
        List<SendOutOrderItem> list = sendOutOrderItemMapper.queryListBySendOrderId(id);
        List<Long> ids = list.stream().map(byBatch ? SendOutOrderItem::getInventoryBatchId : SendOutOrderItem::getCargoId).collect(Collectors.toList());
        Map<Long, SendOrderItemVO> map = byBatch
                ? inventoryBatchMapper.selectSendOrderItemWithBatchId(ids)
                .stream()
                .collect(Collectors.toMap(SendOrderItemVO::getBusinessId, Function.identity(), (v1, v2) -> v1))
                : cargoMapper.selectSendOrderItemWithCargoIds(ids)
                .stream()
                .collect(Collectors.toMap(SendOrderItemVO::getBusinessId, Function.identity(), (v1, v2) -> v1));
        result.setList(list.stream()
                .map(item -> {
                    SendOutOrderDetailVO.Item it = new SendOutOrderDetailVO.Item();
                    it.setCargoId(item.getCargoId());
                    it.setInventoryBatchId(item.getInventoryBatchId());
                    it.setTargetQuantity(PrecisionHelper.precision(unitCache.toExt(item.getReserveQuantity(), item.getUnitId()), item.getUnitId()));
                    it.setUnitId(item.getUnitId());
                    it.setUnit(unitCache.getGlobalUnitName(item.getUnitId()));
                    SendOrderItemVO sendOrder = map.get(byBatch ? item.getInventoryBatchId() : item.getCargoId());
                    if (sendOrder != null) {
                        it.setCargoName(sendOrder.getCargoName());
                        it.setCargoCode(sendOrder.getCargoCode());
                        it.setCargoSpecification(sendOrder.getCargoSpecification());
                        it.setBatchNo(sendOrder.getBatchNo());
                    }
                    return it;
                }).collect(Collectors.toList()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOut(SendOutDTO dto) {
        Long id = dto.getId();
        SendOutOrder order = sendOutOrderMapper.selectById(id);
        if (order == null) {
            throw new BmosException(WmsResponseCode.SEND_ORDER_NOT_EXIST);
        }
        if (!Objects.equals(order.getSendOrderStatus(), SendOrderStatus.PENDING)) {
            throw new BmosException(WmsResponseCode.SEND_ORDER_NOT_ALLOWED_SEND);
        }
        boolean byBatch = Objects.equals(order.getSendOrderType(), SendOrderType.BATCH);
        List<SendOutOrderItem> sendOutOrderItems = sendOutOrderItemMapper.queryListBySendOrderId(order.getId());
        List<Long> inventoryIds = dto.getSendList().stream()
                .flatMap(sendOutItemDTO -> sendOutItemDTO.getInventoryIds().stream())
                .collect(Collectors.toList());
        SendOutFeignDTO sendOutFeignDTO = null;
        // 处理库存
        if (CollectionUtil.isNotEmpty(inventoryIds)) {
            List<Inventory> inventoryList = inventoryMapper.selectBatchIds(inventoryIds);
            if (inventoryList.size() != inventoryIds.size()) {
                throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_NOT_EXIST);
            }
            // 校验可用性
            inventoryList.stream()
                    .filter(item -> !item.isAvailable())
                    .findAny()
                    .ifPresent(item -> {
                        throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_QUANTITY_ZERO);
                    });
            List<InventoryReserve> reserveList = inventoryReserveMapper.selectList()
                    .stream().filter(item -> Objects.equals(order.getRequisitionPlanId(), item.getRequisitionPlanId())).collect(Collectors.toList());

            // 检查货品发料数量(批次也要)
            validateCargoQuantity(sendOutOrderItems, inventoryList, reserveList);
            if (byBatch) {
                // 检查批次发料数量
                validateBatchQuantity(sendOutOrderItems, inventoryList, reserveList);
            }

            // 统计发给mes的物料信息
            sendOutFeignDTO = buildSendOutFeign(order, inventoryList);
            // 记录差值 用来存日志
            List<BigDecimal> diff = new ArrayList<>();
            // 开始扣除库存
            for (Inventory inventory : inventoryList) {
                diff.add(inventory.getAvailableQuantity());
                inventory.setConsumeQuantity(inventory.getConsumeQuantity().add(inventory.getAvailableQuantity()));
                inventory.setAvailableQuantity(BigDecimal.ZERO);
            }
            inventoryMapper.updateBatch(inventoryList);
            // 保存发料的货品日志、货位日志
            cargoLogService.saveCargoLog(CargoInventoryOperateLogType.SEND_OUT, inventoryList, diff,
                    new String[]{dto.getSenderId(), dto.getReCheckerId()}, order, null);
            positionLogService.savePositionLog(PositionInventoryOperateLogType.SEND_OUT, inventoryList, diff,
                    new String[]{dto.getSenderId(), dto.getReCheckerId()}, order, null);
        }

        // 处理工单
        order.setSendOrderStatus(SendOrderStatus.FINISHED);
        order.setSendTime(LocalDateTime.now());
        sendOutOrderMapper.updateById(order);

        // 处理预定信息
        Long requisitionPlanId = order.getRequisitionPlanId();
        inventoryReserveMapper.deleteByRequisitionPlanId(requisitionPlanId);

        if (sendOutFeignDTO != null) {
            // 通知 mes 领料单已发料
            log.info("{}通知mes领料单已发料:{}", LOG_PREFIX, sendOutFeignDTO);
            FeignUtils.handleRequest((s) -> mesFeignClient.sendOut(s), sendOutFeignDTO);
        }
    }

    /**
     * 构造通知mes的参数
     *
     * @param order         领料单
     * @param inventoryList 发料库存列表
     * @return
     */
    private SendOutFeignDTO buildSendOutFeign(SendOutOrder order, List<Inventory> inventoryList) {
        SendOutFeignDTO sendOutFeignDTO;
        sendOutFeignDTO = new SendOutFeignDTO();
        List<SendOutFeignDTO.SendOutBatch> batch = new ArrayList<>();
        sendOutFeignDTO.setRequisitionPlanId(order.getRequisitionPlanId());
        Map<Long, List<Inventory>> batchMap = inventoryList.stream().collect(Collectors.groupingBy(Inventory::getInventoryBatchId));
        Set<Long> batchIds = inventoryList.stream().map(Inventory::getInventoryBatchId).collect(Collectors.toSet());
        Set<Long> cargoIds = inventoryList.stream().map(Inventory::getCargoId).collect(Collectors.toSet());
        List<InventoryBatch> inventoryBatches = inventoryBatchMapper.selectBatchIds(batchIds);
        Map<Long, Cargo> cargoMap = cargoMapper.selectBatchIds(cargoIds).stream().collect(Collectors.toMap(Cargo::getId, Function.identity(), (v1, v2) -> v1));
        inventoryBatches.forEach(item -> {
            SendOutFeignDTO.SendOutBatch sendOutBatch = new SendOutFeignDTO.SendOutBatch();
            List<Inventory> inventories = batchMap.get(item.getId());
            sendOutBatch.setInventoryBatchId(item.getId());
            sendOutBatch.setInventoryBatchNo(item.getBatchNo());
            sendOutBatch.setQuantity(unitCache.toExt(inventories.stream().map(Inventory::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add), item.getUnitId()));
            sendOutBatch.setUnitId(item.getUnitId());
            sendOutBatch.setFactoryBatchNo(item.getFactoryBatchNo());
            sendOutBatch.setProduceDate(item.getProduceDate());
            sendOutBatch.setExpiredDate(item.getExpiredDate());
            sendOutBatch.setHydration(item.getHydration());
            sendOutBatch.setNoHydrationContent(item.getNoHydrationContent());
            sendOutBatch.setReportNo(item.getReportNo());
            sendOutBatch.setLicenceNo(item.getLicenceNo());
            sendOutBatch.setCargoName(Optional.ofNullable(item.getCargoId())
                    .map(cargoMap::get)
                    .map(Cargo::getCargoName)
                    .orElse(null));
            sendOutBatch.setCargoMergeCode(Optional.ofNullable(item.getCargoId())
                    .map(cargoMap::get)
                    .map(Cargo::getMergeCode)
                    .orElse(null));
            sendOutBatch.setInventories(inventories.stream().map(i -> {
                SendOutFeignDTO.SendOutInventory inventory = new SendOutFeignDTO.SendOutInventory();
                inventory.setPlatformMaterialId(Optional.ofNullable(i.getCargoId())
                        .map(cargoMap::get)
                        .map(Cargo::getPlatformMaterialId)
                        .orElse(null));
                inventory.setInventoryNo(i.getNo());
                inventory.setQuantity(i.getQuantity());
                inventory.setUnitId(i.getUnitId());
                return inventory;
            }).collect(Collectors.toList()));
            batch.add(sendOutBatch);
        });
        sendOutFeignDTO.setSendOutBatchList(batch);
        return sendOutFeignDTO;
    }

    private void validateQuantity(SendSubmitDTO dto) {
        List<Long> businessIds = dto.getPendingSendList()
                .stream()
                .map(SendSubmitDTO.SendSubmitQuantityDTO::getBusinessId)
                .collect(Collectors.toList());
        List<Inventory> inventories;
        boolean byBatch;
        List<InventoryBatch> inventoryBatches = new ArrayList<>();
        if (Objects.equals(SendOrderType.getByValue(dto.getSendOrderType()), SendOrderType.BATCH)) {
            byBatch = true;
            // 按批次量
            inventoryBatches = inventoryBatchMapper.selectBatchIds(businessIds);
            if (inventoryBatches.size() != dto.getPendingSendList().size()) {
                throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
            }
            inventories = inventoryMapper.queryListByBatchIds(businessIds);
        } else if (Objects.equals(SendOrderType.getByValue(dto.getSendOrderType()), SendOrderType.CARGO)) {
            byBatch = false;
            // 按物料量
            List<Cargo> cargos = cargoMapper.selectBatchIds(businessIds);
            if (cargos.size() != dto.getPendingSendList().size()) {
                throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_NOT_EXIST);
            }
            inventories = inventoryMapper.queryListByCargoIds(businessIds);
        } else {
            throw new IllegalArgumentException("参数错误");
        }
        // 批次id/货品id -> 可用量
        Map<Long, BigDecimal> quantityMap = inventories.stream()
                .collect(Collectors.groupingBy(byBatch ? Inventory::getInventoryBatchId : Inventory::getCargoId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(Inventory::getAvailableQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        Map<Long, BigDecimal> reserveMap = byBatch
                ? inventoryReserveMapper.getReserveQuantityByInventoryBatchIdList(businessIds)
                : inventoryReserveMapper.getReserveQuantityByCargoIdList(businessIds);
        quantityMap.entrySet().forEach(e -> {
            BigDecimal reserve = reserveMap.getOrDefault(e.getKey(), BigDecimal.ZERO);
            // 剩余可用量 = 可用量 - 预订量
            if (e.getValue().compareTo(reserve) > 0) {
                e.setValue(e.getValue().subtract(reserve));
            } else {
                e.setValue(BigDecimal.ZERO);
            }
        });
        for (SendSubmitDTO.SendSubmitQuantityDTO item : dto.getPendingSendList()) {
            BigDecimal residue = quantityMap.getOrDefault(item.getBusinessId(), BigDecimal.ZERO);
            if (residue.compareTo(unitCache.toBasic(item.getTargetQuantity(), item.getUnitId())) < 0) {
                String cargoName = inventories.stream()
                        .filter(s -> Objects.equals(byBatch ? s.getInventoryBatchId() : s.getCargoId(), item.getBusinessId()))
                        .findFirst()
                        .map(Inventory::getCargoId)
                        .map(cargoId -> cargoMapper.selectById(cargoId))
                        .map(Cargo::getCargoName)
                        .orElse(null);
                log.info("{}根据{}不满足计划领料量:{},剩余:{},需要:{}", LOG_PREFIX, byBatch ? "批次量" : "物料量", cargoName, residue, item.getTargetQuantity());
                throw new BmosException(byBatch ? WmsResponseCode.INVENTORY_QUANTITY_NOT_ENOUGH_BY_BATCH : WmsResponseCode.INVENTORY_QUANTITY_NOT_ENOUGH_BY_CARGO, cargoName);
            }
        }
        // 按批次领料时 还需校验 按物料量的预定是否让该批次量不够了
        if (byBatch) {
            validateByBatchQuantity(dto, inventoryBatches);
        }

    }

    /**
     * 批次量领料不仅要满足批次量是否足够
     * 还要校验物料没有被以物料量预定后不够的情况
     *
     * @param dto
     * @param inventoryBatches
     */
    private void validateByBatchQuantity(SendSubmitDTO dto, List<InventoryBatch> inventoryBatches) {
        if (CollectionUtil.isEmpty(inventoryBatches)) {
            return;
        }
        Map<Long, Long> idMap = CollectionUtils.convertMap(inventoryBatches, InventoryBatch::getId, InventoryBatch::getCargoId);
        Map<Long, BigDecimal> planQuantityMap = new HashMap<>();
        dto.getPendingSendList().forEach(e -> {
            BigDecimal targetQuantity = unitCache.toBasic(e.getTargetQuantity(), e.getUnitId());
            planQuantityMap.merge(Optional.ofNullable(idMap.get(e.getBusinessId()))
                    .orElseThrow(() -> new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST)),
                    targetQuantity, BigDecimal::add);
        });
        List<Long> cargoIds = new ArrayList<>(planQuantityMap.keySet());
        List<Inventory> inventories = inventoryMapper.queryListByCargoIds(CollectionUtils.convertList(inventoryBatches, InventoryBatch::getCargoId));
        Map<Long, BigDecimal> reserveQuantityMap = inventoryReserveMapper.getReserveQuantityByCargoIdList(cargoIds);
        Map<Long, BigDecimal> quantityMap = inventories.stream()
                .collect(Collectors.groupingBy(Inventory::getCargoId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(Inventory::getAvailableQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        for (Map.Entry<Long, BigDecimal> entry : planQuantityMap.entrySet()) {
            Long cargoId = entry.getKey();
            if (quantityMap.get(cargoId).subtract(reserveQuantityMap.getOrDefault(cargoId, BigDecimal.ZERO)).compareTo(entry.getValue()) < 0) {
                Cargo cargo = cargoMapper.selectById(cargoId);
                throw new BmosException(WmsResponseCode.INVENTORY_QUANTITY_NOT_ENOUGH_BY_CARGO, cargo.getCargoName());
            }
        }

    }

    /**
     * 校验按照货品发料量
     *
     * @param sendOutOrderItems          发料计划目标量列表
     * @param inventoryList              实际发料列表
     * @param requisitionPlanReserveList 批次预定列表
     */
    private void validateCargoQuantity(List<SendOutOrderItem> sendOutOrderItems, List<Inventory> inventoryList, List<InventoryReserve> requisitionPlanReserveList) {
        // 货品id -> 计划目标量
        Map<Long, BigDecimal> sendOutReserveQuantityMap = sendOutOrderItems.stream()
                .collect(Collectors.groupingBy(SendOutOrderItem::getCargoId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(SendOutOrderItem::getReserveQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        //  货品id -> 本计划实际预订量
        Map<Long, BigDecimal> reserveQuantityMap = requisitionPlanReserveList.stream()
                .collect(Collectors.groupingBy(InventoryReserve::getCargoId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(InventoryReserve::getReserveQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        // 货品id -> 实际发料量
        Map<Long, BigDecimal> inventoryQuantityMap = inventoryList.stream()
                .collect(Collectors.groupingBy(Inventory::getCargoId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(Inventory::getAvailableQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));

        // 货品id -> 查询物料剩余的未被预定的量
        Map<Long, CargoInventoryRealQuantity> realQuantityMap = inventoryMapper.queryRealQuantityListByCargoId()
                .stream()
                .collect(Collectors.toMap(CargoInventoryRealQuantity::getCargoId, Function.identity(), (k1, k2) -> k1));

        for (Map.Entry<Long, BigDecimal> entry : inventoryQuantityMap.entrySet()) {
            BigDecimal sendOut = sendOutReserveQuantityMap.getOrDefault(entry.getKey(), BigDecimal.ZERO);
            // 货品的发料量不满足计划量
            if (sendOut.compareTo(entry.getValue()) > 0) {
                throw new BmosException(WmsResponseCode.SEND_ORDER_ADD_NOT_FINISHED_CARGO);
            }

            // 未被预定量
            BigDecimal unReserveQuantity = Optional.ofNullable(entry.getKey())
                    .map(realQuantityMap::get)
                    .map(q -> checkZero(q.getAvailableQuantity().subtract(q.getReserveQuantity())))
                    .orElse(BigDecimal.ZERO);
            if (entry.getValue().compareTo(reserveQuantityMap.getOrDefault(entry.getKey(), BigDecimal.ZERO).add(unReserveQuantity)) > 0) {
                // 货品的发料量大于该领料计划的预定量与货品的可用量之和
                throw new BmosException(WmsResponseCode.SEND_ORDER_NOT_ALLOWED_SEND_OTHER_CARGO, Optional.ofNullable(entry.getKey())
                        .map(cargoMapper::selectById)
                        .map(cargo -> cargo.getCargoName() + "-" + cargo.getMergeCode())
                        .orElse(null));
            }
        }
    }

    private void validateBatchQuantity(List<SendOutOrderItem> sendOutOrderItems, List<Inventory> inventoryList, List<InventoryReserve> requisitionPlanReserveList) {
        // 批次id -> 计划目标量
        Map<Long, BigDecimal> sendOutReserveQuantityMap = sendOutOrderItems.stream()
                .collect(Collectors.groupingBy(SendOutOrderItem::getInventoryBatchId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(SendOutOrderItem::getReserveQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        //  批次id -> 本计划实际预订量
        Map<Long, BigDecimal> reserveQuantityMap = requisitionPlanReserveList.stream()
                .collect(Collectors.groupingBy(InventoryReserve::getInventoryBatchId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(InventoryReserve::getReserveQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
        // 批次id -> 实际发料量
        Map<Long, BigDecimal> inventoryQuantityMap = inventoryList.stream()
                .collect(Collectors.groupingBy(Inventory::getInventoryBatchId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(Inventory::getAvailableQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));

        // 批次id -> 查询物料剩余的未被预定的量
        Map<Long, CargoInventoryRealQuantity> realQuantityMap = inventoryMapper.queryRealQuantityListByInventoryBatchId()
                .stream()
                .collect(Collectors.toMap(CargoInventoryRealQuantity::getInventoryBatchId, Function.identity(), (k1, k2) -> k1));

        for (Map.Entry<Long, BigDecimal> entry : inventoryQuantityMap.entrySet()) {
            BigDecimal sendOut = sendOutReserveQuantityMap.getOrDefault(entry.getKey(), BigDecimal.ZERO);
            // 货品批次的发料量不满足计划量
            if (sendOut.compareTo(entry.getValue()) > 0) {
                throw new BmosException(WmsResponseCode.SEND_ORDER_ADD_NOT_FINISHED_BATCH);
            }
            // 未被预定量
            BigDecimal unReserveQuantity = Optional.ofNullable(entry.getKey())
                    .map(realQuantityMap::get)
                    .map(q -> checkZero(q.getAvailableQuantity().subtract(q.getReserveQuantity())))
                    .orElse(BigDecimal.ZERO);
            if (entry.getValue().compareTo(reserveQuantityMap.getOrDefault(entry.getKey(), BigDecimal.ZERO).add(unReserveQuantity)) > 0) {
                // 货品批次的发料量大于该领料计划的预定量与货品批次的可用量之和
                throw new BmosException(WmsResponseCode.SEND_ORDER_NOT_ALLOWED_SEND_OTHER_BATCH, inventoryBatchMapper.selectById(entry.getKey()).getBatchNo());
            }
        }
    }

    private BigDecimal checkZero(BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return value;
    }
}
