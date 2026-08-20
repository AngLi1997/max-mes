package com.bmos.wms.service.cargo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 更新货品dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:36
 */
@Data
@ApiModel("更新货品dto")
public class CargoEditDTO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    @NotNull
    private Long id;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @NotNull
    private Long unitId;

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
