package com.bmos.mes.service.ingredient.weigh.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel("称量日志保存DTO")
@Builder
@Getter
@Setter
public class WeighLogSaveDTO {

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
     * 称量类型
     */
    @ApiModelEnumProperty(value = "称量类型", enumClass = WeighType.class)
    @EnumValidate(WeighType.class)
    private WeighType weighType;

    /**
     * 称量人id
     */
    private String weigherId;


    /**
     * 复核人id
     */
    private String reCheckerId;


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

    /**
     * 产品id
     */
    private Long productId;



}
