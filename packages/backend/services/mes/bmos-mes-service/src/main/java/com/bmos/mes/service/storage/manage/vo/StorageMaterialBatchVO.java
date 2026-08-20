package com.bmos.mes.service.storage.manage.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class StorageMaterialBatchVO {

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialCode;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "WH030102231001")
    private String mergeCode;

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
     * 原始批号
     */
    @ApiModelProperty(value = "原始批号", example = "123")
    private String originalBatchNo;

    /**
     * 物料信息类别
     */
    @ApiModelEnumProperty(value = "物料信息类别", required = true, enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @ApiModelProperty(value = "物料信息类别", example = "1")
    private CategoryInfoTypeEnum categoryType;

    /**
     * 件数
     */
    @ApiModelProperty(value = "件数", example = "1")
    private Integer size;

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
     * 单位转换率
     */
    @ApiModelProperty(value = "单位转换率", example = "1000")
    private String rate;

    /**
     * 预订量
     */
    @ApiModelProperty(value = "预订量", example = "1.000")
    @PrecisionValue
    private String reserveQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private String availableQuantity;

    /**
     * 物料量（可用+预定）
     */
    @ApiModelProperty(value = "物料量(可用+预定)", example = "1.000")
    @PrecisionValue
    private String quantity;

    /**
     * 扩展信息json
     */
    @ApiModelProperty(value = "扩展信息json", hidden = true)
    @JsonIgnore
    private String expandInfoJson;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;

    /**
     * 扩展信息
     */
    @ApiModelProperty(value = "扩展信息")
    private MaterialExpandInfo expandInfo;

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
}
