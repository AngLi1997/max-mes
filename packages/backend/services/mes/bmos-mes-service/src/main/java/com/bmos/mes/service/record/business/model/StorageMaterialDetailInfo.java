package com.bmos.mes.service.record.business.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 物料预定组件预定的批次统计信息
 */
@Data
public class StorageMaterialDetailInfo {
    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 操作量 -> 基本单位量
     * 该操作量指示组件操作的物料量，预定时增加 取消预定减少
     */
    private BigDecimal operateQuantity;

    /**
     * 批次预定剩余量
     * 当前批次在整个生产计划下的预定的所有物料件的实时物料量 而非预订记录中的量
     */
    private BigDecimal remainingQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 水分
     */
    private String hydration;

    /**
     * 含量
     */
    private String noHydrationContent;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    /**
     * 原厂批号
     */
    private String factoryBatchNo;

    /**
     * 原始编码
     */
    private String originalBatchNo;

    /**
     * 报告单编号
     */
    private String reportNo;

    /**
     * 放行单编号
     */
    private String licenceNo;

    /**
     * 有效期至
     */
    private String expiredDate;

    /**
     * 合并编码
     */
    private String mergeCode;

    private Long minId;

    /**
     * 是否为组件操作过的数据
     * @return
     */
    public Boolean operateData() {
        return Objects.nonNull(operateQuantity);
    }

}
