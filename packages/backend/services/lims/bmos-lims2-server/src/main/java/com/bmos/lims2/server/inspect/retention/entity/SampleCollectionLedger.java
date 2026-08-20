package com.bmos.lims2.server.inspect.retention.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 留样样品领用台账实体类
 *
 * @author yigaohui
 * @since 2026/02/09
 */
@Getter
@Setter
@TableName("lm_sample_collection_ledger")
public class SampleCollectionLedger extends BaseDO {

    /**
     * 样品ID
     */
    private Long sampleId;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品名称
     */
    private String materialName;

    /**
     * 检品编码
     */
    private String materialCode;

    /**
     * 规格
     */
    private String materialSpec;

    /**
     * 领用数量
     */
    private String collectQuantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 领用原因
     */
    private String collectReason;

    /**
     * 领用人ID
     */
    private String collectorId;

    /**
     * 领用人姓名
     */
    private String collectorName;

    /**
     * 领用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;
}
