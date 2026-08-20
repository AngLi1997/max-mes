package com.bmos.mes.service.storage.manage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 暂存物料件预定信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/13 17:48
 */
@TableName(value = "bm_storage_material_reserve")
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageMaterialReserve extends BaseDO {

    /**
     * 暂存物料件id
     */
    private Long storageMaterialId;

    /**
     * 预定产品id
     */
    private Long productId;

    /**
     * 预定工艺id
     */
    private Long processId;

    /**
     * 预定生产批次id
     */
    private Long batchId;

    /**
     * 预定生产批次号
     */
    private String batchNo;

    /**
     * 预订量
     */
    private BigDecimal reserveQuantity;

    /**
     * 预定备注
     */
    private String reserveRemark;

    /**
     * 预定时间
     */
    private LocalDateTime reserveTime;

    /**
     * 预定人id（操作人id）
     */
    private String reserveUserId;
}
