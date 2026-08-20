package com.bmos.wms.service.inventory.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.inventory.dto.*;
import com.bmos.wms.service.inventory.vo.*;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 14:23
 */
public interface IInventoryService {

    /**
     * 货品入库
     *
     * @param dto
     * @return 入库后创建的物料件列表
     */
    List<InventoryVO> inbound(InventoryInboundDTO dto);

    /**
     * 货品出库
     *
     * @param dto
     */
    void outbound(InventoryOutboundDTO dto);

    /**
     * 货品移库
     *
     * @param dto
     */
    void move(InventoryMoveDTO dto);

    /**
     * 查询库存批次分页
     *
     * @param pageQuery 分页查询
     * @return 分页数据
     */
    CommonPage<InventoryBatchVO> queryBatchPage(InventoryBatchPageQuery pageQuery);

    /**
     * 查询库存分页（货品批次维度）
     *
     * @param pageQuery 分页查询参数
     * @return 分页数据
     */
    CommonPage<InventoryVO> queryPageByBatchId(InventoryPageQueryWithBatchId pageQuery);


    /**
     * 查询库存分页（货品维度）
     *
     * @param pageQuery 分页查询参数
     * @return 分页数据
     */
    CommonPage<CargoInventoryVO> queryPageByCargoIds(InventoryPageQueryWithCargoId pageQuery);

    /**
     * 查询库存批次分页（货品维度）
     *
     * @param pageQuery
     * @return
     */
    CommonPage<CargoInventoryBatchVO> queryBatchPageByCargoIds(InventoryBatchPageQueryWithCargoId pageQuery);

    /**
     * 新增货品批次
     *
     * @param dto
     */
    void addInventoryBatch(InventoryBatchCreateDTO dto);

    /**
     * 编辑货品批次
     *
     * @param dto
     */
    void editInventoryBatch(InventoryBatchEditDTO dto);

    /**
     * 新增货品件
     *
     * @param dto
     */
    void addInventory(InventoryCreateDTO dto);

    /**
     * 盘点
     *
     * @param dto 盘点
     */
    void check(InventoryCheckDTO dto);

    /**
     * 根据货品id和批次号查询批次列表
     *
     * @param cargoId          货品id
     * @param inventoryBatchNo 批次号
     * @return
     */
    List<CargoInventoryBatchItemVO> listByCargoIdAndBatchNo(Long cargoId, String inventoryBatchNo);

    /**
     * 根据货品批次id和货位id查询
     *
     * @param inventoryBatchId 货品批次id
     * @param positionId       货位id
     * @return
     */
    List<InventoryVO> listByBatchIdAndPositionId(Long inventoryBatchId, Long positionId);

    /**
     * 根据批次id查询批次详细信息
     *
     * @param inventoryBatchId 批次id
     * @return
     */
    @Nullable
    CargoInventoryBatchDetailVO queryInventoryBatchById(Long inventoryBatchId);

    /**
     * 查询批次列表
     * @param dto
     * @return
     */
    List<InventoryBatchListVO> queryBatchByMaterial(InventoryBatchQueryDTO dto);

    List<InventoryAvailableQuantityListVO> queryAvailableQuantityList(InventoryAvailableQuantityQueryDTO dto);

    /**
     * 根据货品id和批次id查询
     *
     * @param cargoId          货品id
     * @param inventoryBatchId 批次id
     * @return
     */
    List<CargoInventoryItemVO> listByCargoIdAndBatchId(Long cargoId, Long inventoryBatchId);

    /**
     * 根据货品件id查询货品件
     * @param inventoryId 货品件id
     * @return
     */
    @Nullable
    InventoryVO queryInventoryById(Long inventoryId);
}
