package com.bmos.wms.service.inventory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存批次
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 14:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bw_inventory_batch")
public class InventoryBatch extends BaseDO {

    /**
     * 货品id
     */
    private Long cargoId;

    /**
     * 货品批号
     */
    private String batchNo;

    /**
     * 原厂批号
     */
    private String factoryBatchNo;

    /**
     * 生产日期
     */
    private LocalDate produceDate;

    /**
     * 有效日期
     */
    private LocalDate expiredDate;

    /**
     * 水分(%)
     */
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    private BigDecimal noHydrationContent;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 报告单编号
     */
    private String reportNo;

    /**
     * 放行单编号
     */
    private String licenceNo;

    /**
     * 是否可用
     */
    private Boolean available;

    /**
     * 质量状态：QUARANTINE / QUALIFIED / UNQUALIFIED / SAMPLED / RESTRICTED_RELEASE
     * <p>与 MES MaterialQualityStatusEnum 同语义；新建批次默认 QUARANTINE，
     * 状态由检验流程驱动，入库 / 出库流程不应反向修改本字段。
     */
    private String qualityStatus;
}
