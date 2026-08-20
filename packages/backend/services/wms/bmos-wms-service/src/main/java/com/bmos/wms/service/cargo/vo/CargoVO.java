package com.bmos.wms.service.cargo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 货品vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:36
 */
@Data
@ApiModel("货品vo")
public class CargoVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", example = "1")
    private Long cargoCategoryId;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "苹果")
    private String cargoName;

    /**
     * 货品编码
     */
    @ApiModelProperty(value = "货品编码", example = "123456")
    private String cargoCode;

    /**
     * 货品合并编码
     */
    @ApiModelProperty(value = "货品合并编码", example = "123456")
    private String mergeCode;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格", example = "1kg/袋")
    private String specification;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称", example = "g")
    private String unit;

    /**
     * 基本单位id
     */
    @ApiModelProperty(value = "基本单位id(unitId为扩展单位时非空)", example = "1")
    private Long parentUnitId;

    /**
     * 基本单位名称
     */
    @ApiModelProperty(value = "基本单位名称(unitId为扩展单位时非空)", example = "g")
    private String parentUnitName;

    /**
     * 是否是成员物料
     */
    @ApiModelProperty(value = "是否是成员物料", example = "true")
    private Boolean isMember;

    /**
     * 所属物料id
     */
    @ApiModelProperty(value = "所属物料id", example = "1")
    private Long subMaterialId;

    /**
     * 单件量
     */
    @ApiModelProperty(value = "单件量", example = "1")
    private BigDecimal singleQuantity;

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
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    /**
     * 平台物料id
     */
    @ApiModelProperty(value = "平台物料id", example = "1")
    private Long platformMaterialId;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enable;
}
