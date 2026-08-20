package com.bmos.wms.service.cargo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 创建货品dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:36
 */
@Data
@ApiModel("创建货品dto")
public class CargoCreateDTO {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", example = "1")
    @NotNull
    private Long cargoCategoryId;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "苹果")
    @NotBlank
    @Length(max = 100)
    private String cargoName;

    /**
     * 货品编码
     */
    @ApiModelProperty(value = "货品编码", example = "123456")
    @NotBlank
    @Length(max = 100)
    private String cargoCode;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格", example = "1kg/袋")
    @NotBlank
    @Length(max = 100)
    private String specification;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @NotNull
    private Long unitId;

    /**
     * 是否是成员物料
     */
    @ApiModelProperty(value = "是否是成员物料", example = "true")
    @NotNull
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
    @DecimalMin("0")
    private BigDecimal singleQuantity;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    @Length(max = 100)
    private String supplier;

    /**
     * 生产商
     */
    @ApiModelProperty(value = "生产商", example = "生产商")
    @Length(max = 100)
    private String producer;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;
}
