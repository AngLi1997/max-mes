package com.bmos.wms.service.reserve.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.reserve.model.InventoryReserve;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 14:26
 */
@Mapper
public interface IInventoryReserveMapper extends BaseMapperX<InventoryReserve> {

    /**
     * 根据批次id查询库存预订量
     *
     * @param inventoryBatchId
     * @return
     */
    default BigDecimal getReserveQuantityByInventoryBatchId(Long inventoryBatchId) {
        if (inventoryBatchId == null) {
            return BigDecimal.ZERO;
        }
        List<InventoryReserve> inventoryReserves = selectList(Wrappers.lambdaQuery(InventoryReserve.class).eq(InventoryReserve::getInventoryBatchId, inventoryBatchId));
        if (CollectionUtil.isEmpty(inventoryReserves)) {
            return BigDecimal.ZERO;
        }
        return inventoryReserves.stream()
                .map(InventoryReserve::getReserveQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 根据批次id查询库存预订量
     *
     * @param inventoryBatchIdList
     * @return
     */
    default Map<Long, BigDecimal> getReserveQuantityByInventoryBatchIdList(List<Long> inventoryBatchIdList) {
        if (CollectionUtil.isEmpty(inventoryBatchIdList)) {
            return new HashMap<>();
        }
        List<InventoryReserve> inventoryReserves = selectList(Wrappers.lambdaQuery(InventoryReserve.class)
                .in(InventoryReserve::getInventoryBatchId, inventoryBatchIdList));
        if (CollectionUtil.isEmpty(inventoryReserves)) {
            return new HashMap<>();
        }
        return inventoryReserves.stream()
                .collect(Collectors.groupingBy(InventoryReserve::getInventoryBatchId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(InventoryReserve::getReserveQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
    }

    /**
     * 根据货品id查询库存预订量
     *
     * @param cargoIdList
     * @return
     */
    default Map<Long, BigDecimal> getReserveQuantityByCargoIdList(List<Long> cargoIdList) {
        if (CollectionUtil.isEmpty(cargoIdList)) {
            return new HashMap<>();
        }
        List<InventoryReserve> inventoryReserves = selectList(Wrappers.lambdaQuery(InventoryReserve.class)
                .in(InventoryReserve::getCargoId, cargoIdList));
        if (CollectionUtil.isEmpty(inventoryReserves)) {
            return new HashMap<>();
        }
        return inventoryReserves.stream()
                .collect(Collectors.groupingBy(InventoryReserve::getCargoId, Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .map(InventoryReserve::getReserveQuantity)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))));
    }

    default void deleteByRequisitionPlanId(Long requisitionPlanId) {
        if (requisitionPlanId == null) {
            return;
        }
        delete(Wrappers.lambdaQuery(InventoryReserve.class)
                .eq(InventoryReserve::getRequisitionPlanId, requisitionPlanId)
        );
    }

    /**
     * 根据领料计划单id查询预订量
     *
     * @param requisitionPlanId 领料计划单id
     * @return
     */
    default List<InventoryReserve> getListByRequisitionPlanId(Long requisitionPlanId) {
        return selectList(Wrappers.lambdaQuery(InventoryReserve.class)
                .eq(InventoryReserve::getRequisitionPlanId, requisitionPlanId)
        );
    }

    BigDecimal queryReserveQuantityByBatchId(@Param("batchId") Long batchId);
}
