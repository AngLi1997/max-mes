package com.bmos.mes.service.storage.manage.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.service.storage.manage.dto.BatchReservedMaterialQueryDTO;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/13 17:54
 */
@Mapper
public interface IStorageMaterialReserveMapper extends BaseMapperX<StorageMaterialReserve> {

    /**
     * 物料件是否已被预定
     *
     * @param storageMaterialId 暂存物料件id
     * @return true:已被预定 false:未被预定
     */
    default boolean existReserve(Long storageMaterialId) {
        if (storageMaterialId == null) {
            return false;
        }
        return exists(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .eq(StorageMaterialReserve::getStorageMaterialId, storageMaterialId)
        );
    }

    /**
     * 物料件是否已被预定
     *
     * @param storageMaterialIds 暂存物料件ids
     * @return true:已被预定 false:未被预定
     */
    default boolean existReserves(List<Long> storageMaterialIds) {
        if (CollectionUtil.isEmpty(storageMaterialIds)) {
            return false;
        }
        return exists(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .in(StorageMaterialReserve::getStorageMaterialId, storageMaterialIds)
        );
    }

    /**
     * 根据预定产品信息查询预定信息
     *
     * @param productId 预定产品id
     * @param processId 工艺id
     * @param batchId   预定生产批次id
     * @return 预定信息列表
     */
    default List<StorageMaterialReserve> queryListByReserveProductInfo(Long productId, Long processId, Long batchId) {
        if (productId == null || batchId == null || processId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .eq(StorageMaterialReserve::getProductId, productId)
                .eq(StorageMaterialReserve::getProcessId, processId)
                .eq(StorageMaterialReserve::getBatchId, batchId)
        );
    }

    default void deleteByStorageMaterialIds(Collection<Long> storageMaterialIdList) {
        if (CollectionUtil.isEmpty(storageMaterialIdList)) {
            return;
        }
        delete(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .in(StorageMaterialReserve::getStorageMaterialId, storageMaterialIdList)
        );
    }

    default List<StorageMaterialReserve> queryByStorageMaterialIds(Collection<Long> storageMaterialIdList) {
        if (CollectionUtil.isEmpty(storageMaterialIdList)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .in(StorageMaterialReserve::getStorageMaterialId, storageMaterialIdList)
        );
    }

    default void deleteByStorageMaterialId(Long storageMaterialId) {
        if (storageMaterialId == null) {
            return;
        }
        delete(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .eq(StorageMaterialReserve::getStorageMaterialId, storageMaterialId)
        );
    }

    default StorageMaterialReserve queryByStorageMaterialId(Long storageMaterialId) {
        if (storageMaterialId == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(StorageMaterialReserve.class)
                .eq(StorageMaterialReserve::getStorageMaterialId, storageMaterialId)
        );
    }

    /**
     * 此处查询预定量查询的是物料件实时的量
     * 例如预定时是10 生产投料7
     * 查出来应为3
     * @param dto
     * @return
     */
    List<BatchReservedMaterialVO> queryBatchReservedMaterial(BatchReservedMaterialQueryDTO dto);

    List<StorageMaterialReserve> selectByBatchIdAndMaterialId(@Param("planId") Long planId, @Param("materialId") Long materialId);

    /**
     * 根据生产计划id查询物料的预定信息
     * @param productPlanId
     * @return
     */
    default List<StorageMaterialReserve> selectByProductPlanId(Long productPlanId){
        return selectList(new LambdaQueryWrapperX<StorageMaterialReserve>()
                .eq(StorageMaterialReserve::getBatchId, productPlanId));
    }

    default StorageMaterialReserve selectByBatchIdAndStorageMaterialId(Long productPlanId, Long id){
        return selectOne(new LambdaQueryWrapperX<StorageMaterialReserve>()
                .eq(StorageMaterialReserve::getBatchId, productPlanId)
                .eq(StorageMaterialReserve::getStorageMaterialId, id));
    }

    default List<StorageMaterialReserve> selectOtherBatchReserved(List<Long> ids, Long productPlanId){
        return selectList(new LambdaQueryWrapperX<StorageMaterialReserve>()
                .ne(StorageMaterialReserve::getBatchId, productPlanId)
                .in(StorageMaterialReserve::getStorageMaterialId, ids));
    }

    List<BatchReservedMaterialVO> queryBatchReservedMaterialByMaterialIds(@Param("productPlanId") Long productPlanId,
                                                                          @Param("materialIdList") List<Long> materialIdList);

    default StorageMaterialReserve selectByStorageMaterialId(Long storageMaterialId){
        return selectOne(new LambdaQueryWrapperX<StorageMaterialReserve>()
                .eq(StorageMaterialReserve::getStorageMaterialId, storageMaterialId));
    }
}
