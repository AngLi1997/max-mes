package com.bmos.mes.service.storage.manage.vo;


import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import lombok.Data;

/**
 * 物料件详情VO
 */
@Data
public class StorageMaterialDetailVO {

    /**
     * 物料件信息
     */
    private StorageMaterial storageMaterial;

    /**
     * 物料批次信息
     */
    private StorageMaterialBatch storageMaterialBatch;

    /**
     * 物料件预订信息
     */
    private StorageMaterialReserve storageMaterialReserve;

    /**
     * 是否被非当前批次或非当前批次关联批次预定
     */
    private boolean orderByOthers;

    public void validateAll() {
        // 物料件有效校验
        storageMaterial.availableValidate();
        // 物料批次有效、质量校验
        storageMaterialBatch.availableValidate();
        // 预定状态校验
        if (orderByOthers) {
            throw new BmosException(MesResponseCode.RESERVED_BY_UNRELATED_BATCH);
        }
        // 出库状态校验
        storageMaterial.outboundValidate();
    }

}
