package com.bmos.mes.service.record.business.model.preparation;

import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配液投产出物料件信息
 */
@Getter
@Setter
public class PreparationProduceMaterialInfo {

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料合并编码
     */
    private String materialCode;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料批号
     */
    private String storageMaterialBatchNo;

    /**
     * 物料件号
     */
    private String storageMaterialNo;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 产出人id
     */
    private String producerId;

    /**
     * 产出人姓名
     */
    private String producerName;

    /**
     * 复核人姓名
     */
    private String reCheckerName;

    /**
     * 物料规格
     */
    private String specification;

    /**
     * 产出时间
     */
    private LocalDateTime produceTime;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 是否处理
     */
    private Boolean handle;

    /**
     * 签名状态
     */
    private PrepareSignStatusEnum signStatus;

}
