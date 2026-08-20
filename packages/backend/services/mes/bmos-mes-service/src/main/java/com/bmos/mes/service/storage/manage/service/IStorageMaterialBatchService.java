package com.bmos.mes.service.storage.manage.service;

import com.bmos.mes.service.storage.manage.dto.StorageMaterialBatchPageQuery;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchListVO;
import com.bmos.mes.service.storage.manage.vo.ReservedBatchInfo;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialBatchDetailVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialBatchVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 15:44
 */
public interface IStorageMaterialBatchService {

    CommonPage<StorageMaterialBatchVO> queryPage(StorageMaterialBatchPageQuery pageQuery);
    /**
     * 根据物料id查询暂存物料批次信息
     *
     * @param materialId
     * @return
     */
    List<MaterialBatchListVO> queryMaterialBatchListByMaterialId(Long materialId, String batchNo);

    Boolean checkExistedBatchByMaterialId(Long id);

    List<ReservedBatchInfo> queryReservedBatch(Long batchId, Long materialId);

    List<StorageMaterialBatch> queryListByIds(List<Long> longs);

    StorageMaterialBatch getById(Long storageMaterialBatchId);

    /**
     * 根据生产计划id和物料批次id列表查询详细预订信息
     * @param productPlanId 生产计划id
     * @param materialBatchIdList 物料批次id列表
     * @return
     */
    List<ReservedBatchInfo> queryReservedBatch(Long productPlanId, List<Long> materialBatchIdList);

    /**
     * 查询物料批次详情信息及自定义字段信息
     * @param materialBatchId
     * @return
     */
    StorageMaterialBatchDetailVO queryMaterialBatchDetail(Long materialBatchId);

    /**
     * 根据物料id以及物料批次编号查询物料批次信息
     * @param materialId：物料id
     * @param materialBatchNo：全量匹配
     * @return
     */
    StorageMaterialBatch queryMaterialBatchByNoAndMaterialId(Long materialId, String materialBatchNo);

    /**
     * 产出生成新的批次
     * @param storageMaterialBatch
     */
    void createMaterialBatch(StorageMaterialBatch storageMaterialBatch);

    /**
     * 更新物料批次
     * @param storageMaterialBatch
     */
    void updateById(StorageMaterialBatch storageMaterialBatch);
}
