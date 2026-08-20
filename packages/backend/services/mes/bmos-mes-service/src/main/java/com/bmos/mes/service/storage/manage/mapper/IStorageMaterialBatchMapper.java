package com.bmos.mes.service.storage.manage.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialBatchManagePageQuery;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialBatchPageQuery;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.vo.ReservedBatchInfo;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialBatchVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageBatchVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 16:07
 */
@Mapper
public interface IStorageMaterialBatchMapper extends BaseMapperX<StorageMaterialBatch> {

    List<StorageMaterialBatchVO> queryList(@Param("pageQuery") StorageMaterialBatchPageQuery pageQuery, @Param("storageMaterialBatchIdList") Collection<Long> storageMaterialBatchIdList);

    default List<StorageMaterialBatch> queryListByMaterialIdAndLikeBatchNo(Long materialId, String batchNo) {
        return selectList(Wrappers.lambdaQuery(StorageMaterialBatch.class)
                .eq(StorageMaterialBatch::getMaterialId, materialId)
                .like(StrUtil.isNotEmpty(batchNo), StorageMaterialBatch::getMaterialBatchNo, batchNo)
        );
    }

    default StorageMaterialBatch queryByMaterialIdAndBatchNo(Long materialId, String batchNo) {
        if (materialId == null || StrUtil.isBlank(batchNo)) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(StorageMaterialBatch.class)
                .eq(StorageMaterialBatch::getMaterialId, materialId)
                .eq(StorageMaterialBatch::getMaterialBatchNo, batchNo)
        );
    }

    default Boolean checkExistedBatchByMaterialId(Long materialId) {
        return exists(new LambdaQueryWrapperX<StorageMaterialBatch>()
                .eq(StorageMaterialBatch::getMaterialId, materialId));
    }

    /**
     * 查询可用的批次
     *
     * @param date
     * @return
     */
    default List<StorageMaterialBatch> queryAvailableBatch(LocalDate date) {
        return selectList(Wrappers.lambdaQuery(StorageMaterialBatch.class)
                .lt(StorageMaterialBatch::getExpiredDate, date)
                .eq(StorageMaterialBatch::getAvailable, true));
    }

    List<ReservedBatchInfo> selectReservedBatch(@Param("batchId") Long batchId, @Param("materialId") Long materialId);

    default StorageMaterialBatch selectByMaterialIdAndNo(Long materialId, String inventoryBatchNo) {
        return selectOne(new LambdaQueryWrapperX<StorageMaterialBatch>()
                .eq(StorageMaterialBatch::getMaterialId, materialId)
                .eq(StorageMaterialBatch::getMaterialBatchNo, inventoryBatchNo));
    }

    List<StorageMaterialManageBatchVO> queryBatchPage(@Param("pageQuery") StorageMaterialBatchManagePageQuery pageQuery);

    /**
     * 根据id查询批次
     *
     * @param id
     * @return
     */
    StorageMaterialManageBatchVO queryBatchById(@Param("id") Long id);

    /**
     * 根据生产计划id和物料批次id列表查询预定信息
     *
     * @param productPlanId       生产计划id
     * @param materialBatchIdList 物料批次id列表
     * @return
     */
    List<ReservedBatchInfo> selectReservedBatchByBatchId(@Param("productPlanId") Long productPlanId, @Param("materialBatchIdList") List<Long> materialBatchIdList);

    /**
     * 根据物料id和物料批次号查询物料批次
     *
     * @param materialId
     * @param materialBatchNo
     * @return
     */
    default StorageMaterialBatch queryMaterialBatchByNoAndMaterialId(Long materialId, String materialBatchNo) {
        return selectOne(new LambdaQueryWrapperX<StorageMaterialBatch>()
                .eq(StorageMaterialBatch::getMaterialId, materialId)
                .eq(StorageMaterialBatch::getMaterialBatchNo, materialBatchNo));
    }

    List<StorageMaterialBatchVO> queryBatchPageList(@Param("pageQuery") StorageMaterialBatchPageQuery pageQuery, @Param("positionIdList") List<Long> positionIdList);

    /**
     * 重置临期预警标识
     *
     * @param id 物料id
     */
    void resetExpireWarningFlag(@Param("materialId") Long id);

    /**
     * 查询需要临期提醒的物料批次
     *
     * @return 查询结果
     */
    List<StorageMaterialManageBatchVO> selectExpireWarningList();
}
