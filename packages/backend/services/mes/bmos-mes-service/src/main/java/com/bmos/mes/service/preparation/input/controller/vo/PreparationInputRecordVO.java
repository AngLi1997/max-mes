package com.bmos.mes.service.preparation.input.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.preparation.PrepareInputStatusEnum;
import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 待投料的物料件
 */
@Data
@ApiModel("待投料物料件")
public class PreparationInputRecordVO {

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long materialId;

    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1")
    private Long storageMaterialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "001")
    private String materialMergeCode;

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long StorageMaterialBatchId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "001")
    private String storageMaterialBatchNo;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "1")
    private String specification;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "001")
    private String storageMaterialNo;

    /**
     * 物料量
     */
    @ApiModelProperty(value = "物料量", example = "1")
    @PrecisionValue
    private BigDecimal quantity;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 投料状态
     */
    @ApiModelEnumProperty(value = "投入状态", enumClass = PrepareInputStatusEnum.class)
    @EnumValidate(PrepareInputStatusEnum.class)
    private PrepareInputStatusEnum inputStatus;

    /**
     * 投料人id
     */
    @ApiModelProperty(value = "投入人id", example = "1")
    private String importerId;

    /**
     * 投料人姓名
     */
    @ApiModelProperty(value = "投入人姓名", example = "张三")
    private String importerName;

    /**
     * 投料人显示名称
     */
    @ApiModelProperty(value = "投入人显示名称", example = "张三")
    private String importShowName;

    /**
     * 投料时间
     */
    @ApiModelProperty(value = "投入时间", example = "2024-04-25 00:00:00")
    private LocalDateTime inputTime;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", example = "1")
    private Long deviceId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", example = "不锈钢盆儿")
    private String deviceName;

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号", example = "01")
    private String deviceCode;

    /**
     * 投入顺序
     */
    @ApiModelProperty(value = "投入排序", example = "1")
    private Integer sort;

    /**
     * 签名状态
     */
    @ApiModelEnumProperty(value = "签名状态", enumClass = PrepareSignStatusEnum.class)
    @EnumValidate(PrepareSignStatusEnum.class)
    private PrepareSignStatusEnum inputSignStatus;

    /**
     * 配方物料id
     */
    @ApiModelProperty(value = "配方物料id")
    private Long formulaMaterialId;

}
