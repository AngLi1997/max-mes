package com.bmos.mes.service.tag.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("扫描物料公共校验VO")
public class ScanMaterialCommonVO {

    /**
     * 暂存物料件id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long materialBatchId;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    private String materialNo;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 扩展单位id
     */
    @ApiModelProperty(value = "扩展单位id", example = "1")
    private Long unitExtendId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private String availableQuantity;

    /**
     * 预订量
     */
    @ApiModelProperty(value = "预订量", example = "1.000")
    @PrecisionValue
    private String reserveQuantity;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "1.000")
    @PrecisionValue
    private String consumeQuantity;

    /**
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000")
    @PrecisionValue
    private String initQuantity;

    /**
     * 物料量（可用+预定）
     */
    @ApiModelProperty(value = "物料量（可用+预定）", example = "1.000")
    @PrecisionValue
    private String quantity;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;

    /**
     * 原始编码
     */
    @ApiModelProperty(value = "原始编码", example = "WH050101")
    private String originalCode;

    /**
     * 原厂批号 来源为领料接收（仓库来的信息）
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

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
     * 最终暴露的单位(有扩展单位优先显示扩展单位 否则显示标准单位)
     *
     * @return
     */
    @PrecisionUnitId
    public Long finalUnitId;

    public Long getFinalUnitId() {
        return unitExtendId == null ? unitId : unitExtendId;
    }

    /**
     * 单位是否为扩展单位
     *
     * @return
     */
    @JsonIgnore
    public boolean unitIsExtend() {
        return unitExtendId != null;
    }


    @ApiModelEnumProperty(value = "物料类别", enumClass = CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;
}