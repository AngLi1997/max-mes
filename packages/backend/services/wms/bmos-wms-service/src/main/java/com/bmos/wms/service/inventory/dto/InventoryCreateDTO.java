package com.bmos.wms.service.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 新增库存dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 17:07
 */
@Data
@ApiModel("新增货品件dto")
public class InventoryCreateDTO {

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1772538333008891904", required = true)
    @NotNull
    private Long cargoId;

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id", example = "177253833300", required = true)
    @NotNull
    private Long batchId;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1", required = true)
    @NotNull
    private Long positionId;

    /**
     * 单件量
     */
    @ApiModelProperty(value = "单件量", example = "9999999999.999999999", required = true)
    @NotNull
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal singleQuantity;

    /**
     * 单件量单位id
     */
    @ApiModelProperty(value = "单件量单位id", example = "1760853376209391616", required = true)
    @NotNull
    private Long singleUnitId;

    /**
     * 新增件数
     */
    @ApiModelProperty(value = "新增件数", example = "99", required = true)
    @NotNull
    @Min(1)
    @Max(99)
    private Integer size;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id", example = "1", required = true)
    @NotBlank
    private String operatorId;
}
