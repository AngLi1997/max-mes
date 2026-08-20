package com.bmos.mes.service.ingredient.weigh.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.ingredient.WeighType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("bm_weigh_log")
public class WeighLog {

    /**
     * id
     */
    private Long id;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String materialNo;

    /**
     * 净重
     */
    private BigDecimal netWeight;

    /**
     * 皮重
     */
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    private BigDecimal grossWeight;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 称量类型
     */
    private WeighType weighType;

    /**
     * 称量人id
     */
    private String weigherId;

    /**
     * 称量人 名称
     */
    private String weigherName;

    /**
     * 称量人 loginName
     */
    private String weigherLoginName;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 复核人名称
     */
    private String reCheckerName;

    /**
     * 复核人loginName
     */
    private String reCheckerLoginName;

    /**
     * 称量时间
     */
    private LocalDateTime weighTime;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料类型
     */
    private Integer materialType;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

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

    /**
     * 校准状态
     */
    private Boolean equipmentStatus;

    /**
     * 校准效期
     */
    private LocalDate equipmentExpireDate;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productMergeCode;

    /**
     * 生产批号
     */
    private String productBatchNo;

    /**
     * 生产批次id
     */
    private Long productPlanId;
}
