package com.bmos.wms.service.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 货品入库参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 11:51
 */
@Data
@ApiModel("货品入库参数")
public class InventoryInboundDTO {

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1772538333008891904", required = true)
    @NotNull
    private Long cargoId;

    /**
     * 货品批次
     */
    @ApiModelProperty(value = "货品批次", example = "btc1", required = true)
    @NotBlank
    @Length(max = 100)
    private String batchNo;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2024032901")
    @Length(max = 100)
    private String factoryBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2024-03-29")
    private LocalDate produceDate;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-03-29", required = true)
    @NotNull
    private LocalDate expiredDate;

    /**
     * 水分(%) 默认 0
     */
    @ApiModelProperty(value = "水分(%)", example = "999.9999")
    @DecimalMin("0")
    @DecimalMax("999.9999")
    private BigDecimal hydration;

    /**
     * 无水含量(%) 默认 100
     */
    @ApiModelProperty(value = "无水含量(%)", example = "999.9999")
    @DecimalMin("0")
    @DecimalMax("999.9999")
    private BigDecimal noHydrationContent;

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
     * 入库件数
     */
    @ApiModelProperty(value = "入库件数", example = "99", required = true)
    @NotNull
    @Min(1)
    @Max(99)
    private Integer size;

    /**
     * 零头量
     */
    @ApiModelProperty(value = "零头量", example = "9999999999.999999999")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal oddQuantity;

    /**
     * 零头单位id
     */
    @ApiModelProperty(value = "零头单位id", example = "1760853321251426304")
    private Long oddUnitId;

    /**
     * 来源/去向
     */
    @ApiModelProperty(value = "来源/去向", example = "来源/去向", required = true)
    @NotBlank
    @Length(max = 200)
    private String linkExplain;

    /**
     * 递交人id
     */
    @ApiModelProperty(value = "递交人id", example = "1", required = true)
    @NotBlank
    private String senderId;

    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id", example = "1", required = true)
    @NotBlank
    private String receiverId;
}
