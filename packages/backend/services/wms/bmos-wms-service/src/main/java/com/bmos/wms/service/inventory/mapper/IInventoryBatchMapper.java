package com.bmos.wms.service.inventory.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.inventory.dto.InventoryBatchPageQuery;
import com.bmos.wms.service.inventory.dto.InventoryBatchPageQueryWithCargoId;
import com.bmos.wms.service.inventory.dto.InventoryBatchQueryDTO;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.bmos.wms.service.inventory.vo.CargoInventoryBatchVO;
import com.bmos.wms.service.inventory.vo.InventoryBatchListVO;
import com.bmos.wms.service.inventory.vo.InventoryBatchVO;
import com.bmos.wms.service.sendout.vo.SendOrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 库存批次Mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 15:22
 */
@Mapper
public interface IInventoryBatchMapper extends BaseMapperX<InventoryBatch> {

    /**
     * 根据货品id和批次号查询库存批次
     *
     * @param cargoId 货品id
     * @param batchNo 货品批次号
     * @return 货品批次
     */
    default InventoryBatch selectByCargoIdAndBatchNo(Long cargoId, String batchNo) {
        if (cargoId == null || StrUtil.isBlank(batchNo)) {
            return null;
        }
        return selectOne(InventoryBatch::getCargoId, cargoId, InventoryBatch::getBatchNo, batchNo);
    }

    /**
     * 查询库存批次
     *
     * @param pageQuery            分页查询参数
     * @param inventoryBatchIdList 库存批次id集合
     * @return 库存批次
     */
    List<InventoryBatchVO> queryList(@Param("pageQuery") InventoryBatchPageQuery pageQuery, @Param("inventoryBatchIdList") Collection<Long> inventoryBatchIdList);

    /**
     * 查询库存批次
     *
     * @param pageQuery
     * @return
     */
    List<CargoInventoryBatchVO> queryBatchList(@Param("pageQuery") InventoryBatchPageQueryWithCargoId pageQuery);

    /**
     * 查询待刷新可用状态列表
     *
     * @param date 阈值日期
     * @return 待刷新批次列表
     */
    default List<InventoryBatch> queryPendingRefreshAvailableBatch(LocalDate date) {
        return selectList(Wrappers.lambdaQuery(InventoryBatch.class)
                .lt(InventoryBatch::getExpiredDate, date)
                .eq(InventoryBatch::getAvailable, true));
    }

    default List<InventoryBatch> listByCargoIdAndBatchNo(Long cargoId, String inventoryBatchNo) {
        return selectList(Wrappers.lambdaQuery(InventoryBatch.class)
                .eq(InventoryBatch::getCargoId, cargoId)
                .like(StrUtil.isNotEmpty(inventoryBatchNo), InventoryBatch::getBatchNo, inventoryBatchNo)
        );
    }

    /**
     * 根据物料查询可用批次
     * @param dto
     * @return
     */
    List<InventoryBatchListVO> queryBatchListByMaterial(InventoryBatchQueryDTO dto);

    /**
     * 根据批次id查询发货单明细
     *
     * @param batchIds 货品id列表
     * @return
     */
    List<SendOrderItemVO> selectSendOrderItemWithBatchId(@Param("batchIds") List<Long> batchIds);

    /**
     * 根据货品id查询货品批次列表
     * @param id
     * @return
     */
    default List<InventoryBatch> selectByCargoId(Long id){
        if (id == null){
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(InventoryBatch.class)
                .eq(InventoryBatch::getCargoId, id)
        );
    }
}
