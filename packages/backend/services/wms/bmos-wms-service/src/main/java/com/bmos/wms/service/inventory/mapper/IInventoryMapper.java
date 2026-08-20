package com.bmos.wms.service.inventory.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.inventory.dto.InventoryAvailableQuantityQueryDTO;
import com.bmos.wms.service.inventory.dto.InventoryPageQueryWithBatchId;
import com.bmos.wms.service.inventory.dto.InventoryPageQueryWithCargoId;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.inventory.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 14:14
 */
@Mapper
public interface IInventoryMapper extends BaseMapperX<Inventory> {

    /**
     * 根据货位货位id查询可用量不为0的库存
     *
     * @param positionId 库存id
     * @return 库存列表
     */
    default List<Inventory> queryListByPositionId(Long positionId) {
        if (positionId == null) {

        }
        return selectList(Wrappers.lambdaQuery(Inventory.class)
                .isNull(positionId == null, Inventory::getPositionId)
                .eq(positionId != null, Inventory::getPositionId, positionId)
                .ne(positionId != null, Inventory::getAvailableQuantity, BigDecimal.ZERO)
        );
    }

    default List<Inventory> queryListByPositionIds(List<Long> positionIds) {
        return selectList(Wrappers.lambdaQuery(Inventory.class)
                .in(Inventory::getPositionId, positionIds)
                .ne(Inventory::getAvailableQuantity, 0)
        );
    }

    /**
     * 根据批次id和货位id查询可用量不为0的物料
     *
     * @param batchIds
     * @param positionIds
     * @return
     */
    default List<Inventory> queryListByBatchIdsAndPositionId(List<Long> batchIds, Collection<Long> positionIds) {
        return selectList(Wrappers.lambdaQuery(Inventory.class)
                .in(Inventory::getInventoryBatchId, batchIds)
                .in(CollectionUtil.isNotEmpty(positionIds), Inventory::getPositionId, positionIds)
                .ne(Inventory::getAvailableQuantity, 0)
        );
    }

    /**
     * 分页查询货品
     *
     * @param pageQuery
     * @param positionIds
     * @return
     */
    List<InventoryVO> queryPageWithPositionIds(@Param("query") InventoryPageQueryWithBatchId pageQuery, @Param("positionIds") List<Long> positionIds);

    /**
     * 分页查询货品
     *
     * @param pageQuery
     * @param cargoIds
     * @return
     */
    List<CargoInventoryVO> queryPageWithCargoIds(@Param("query") InventoryPageQueryWithCargoId pageQuery, @Param("cargoIds") List<Long> cargoIds);

    /**
     * 根据货品id列表查询所有可用量不为0的库存列表
     *
     * @param cargoIds 货品id list（为nul查询所有）
     * @return 库存列表
     */
    default List<Inventory> queryListByCargoIds(List<Long> cargoIds) {
        if (cargoIds == null) {
            // 查询所有
            return selectList(Wrappers.lambdaQuery(Inventory.class).ne(Inventory::getAvailableQuantity, 0));
        } else if (CollectionUtil.isEmpty(cargoIds)) {
            return new ArrayList<>();
        } else {
            return selectList(Wrappers.lambdaQuery(Inventory.class)
                    .in(Inventory::getCargoId, cargoIds)
                    .ne(Inventory::getAvailableQuantity, 0)
            );
        }
    }

    List<InventoryVO> queryListByBatchIdAndPositionId(@Param("inventoryBatchId") Long inventoryBatchId, @Param("positionId") Long positionId);

    default List<Inventory> queryListByBatchId(Long inventoryBatchId) {
        if (inventoryBatchId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Inventory.class)
                .eq(Inventory::getInventoryBatchId, inventoryBatchId)
                .ne(Inventory::getAvailableQuantity, 0)
        );
    }

    default List<Inventory> queryListByBatchIds(List<Long> inventoryBatchIds) {
        if (inventoryBatchIds == null) {
            return selectList();
        }
        if (CollectionUtil.isEmpty(inventoryBatchIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Inventory.class)
                .in(Inventory::getInventoryBatchId, inventoryBatchIds)
                .ne(Inventory::getAvailableQuantity, 0)
        );
    }

    BigDecimal queryAvailableQuantityByBatchId(@Param("inventoryBatchId") Long inventoryBatchId);

    List<InventoryAvailableQuantityListVO> queryAvailableQuantityList(InventoryAvailableQuantityQueryDTO dto);

    List<CargoInventoryItemVO> listByCargoIdAndBatchId(@Param("cargoId") Long cargoId, @Param("inventoryBatchId") Long inventoryBatchId);

    List<CargoInventoryRealQuantity> queryRealQuantityListByCargoId();

    List<CargoInventoryRealQuantity> queryRealQuantityListByInventoryBatchId();
}
