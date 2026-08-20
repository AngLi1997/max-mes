package com.bmos.mes.service.storage.manage.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@Data
@ApiModel("暂存物料批次信息")
public class StorageMaterialManageBatchVO {

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id", example = "1", required = true)
    private Long storageMaterialBatchId;

    /**
     * 物料类型
     */
    @ApiModelProperty("物料类型")
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料合并编码
     */
    @ApiModelProperty(value = "物料合并编码", example = "WH03-001")
    private String mergeCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialCode;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "25kg/袋")
    private String materialSpecification;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2023-04-01")
    private LocalDate expiredDate;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2023-04-01")
    private LocalDate produceDate;

    /**
     * 水分(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "1.000")
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    @ApiModelProperty(value = "无水含量(%)", example = "1.000")
    private BigDecimal noHydrationContent;

    /**
     * 报告单编号
     */
    @ApiModelProperty(value = "报告单编号", example = "123456")
    private String reportNo;

    /**
     * 放行单编号
     */
    @ApiModelProperty(value = "放行单编号", example = "123456")
    private String licenceNo;

    /**
     * 原始编码
     */
    @ApiModelProperty(value = "原始编码", example = "WH050101")
    private String originalBatchNo;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;

    /**
     * 生产商
     */
    @ApiModelProperty(value = "生产商", example = "生产商")
    private String producer;

    /**
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000")
    @PrecisionValue
    private BigDecimal initQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private BigDecimal availableQuantity;

    /**
     * 预定量
     */
    @ApiModelProperty(value = "预定量", example = "1.000")
    @PrecisionValue
    private BigDecimal reserveQuantity;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "1.000")
    @PrecisionValue
    private BigDecimal consumeQuantity;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位信息
     */
    @ApiModelProperty(value = "单位信息", example = "g")
    private String unit;

    /**
     * 基本单位id
     */
    @ApiModelProperty(value = "基本单位id", example = "1")
    private Long basicUnitId;

    /**
     * 基本单位
     */
    @ApiModelProperty(value = "基本信息", example = "g")
    private String basicUnit;

    /**
     * 过期标识
     */
    @ApiModelProperty(value = "过期标识")
    private boolean expireFlag;

    /**
     * 是否需要临期提醒标志
     */
    @ApiModelProperty(value = "是否需要临期提醒标志", example = "2024-06-20")
    private Boolean dyingFlag;

    /**
     * 临期天数
     */
    @ApiModelProperty(value = "临期天数", example = "1")
    private Integer dyingPeriod;

    @ApiModelEnumProperty(value = "物料批次质量状态", enumClass = MaterialQualityStatusEnum.class)
    private String qualityStatus;

    public MaterialQualityStatusEnum getQualityStatus() {
        return MaterialQualityStatusEnum.getEnumByValue(qualityStatus);
    }
}
