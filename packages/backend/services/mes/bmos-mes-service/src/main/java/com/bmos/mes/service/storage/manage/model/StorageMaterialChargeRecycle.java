package com.bmos.mes.service.storage.manage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.storage.ChargeRecycleTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@TableName("bm_storage_material_charge_recycle")
public class StorageMaterialChargeRecycle extends BaseDO {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 物料规格
     */
    private String specification;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料件号
     */
    private String storageMaterialNo;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 操作类型
     */
    private ChargeRecycleTypeEnum operationType;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * charge_recycle_component表主键id
     */
    private Long chargeRecycleComponentId;

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备编号
     */
    private String equipmentCode;

}
