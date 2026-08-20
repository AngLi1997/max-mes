package com.bmos.wms.service.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 编辑库存批次dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 16:52
 */
@Data
@ApiModel("编辑库存批次dto")
public class InventoryBatchEditDTO {

    /**
     * 库存批次id
     */
    @ApiModelProperty(value = "库存批次id", example = "1", required = true)
    @NotNull
    private Long batchId;

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
     * 报告单编号
     */
    @ApiModelProperty(value = "报告单编号", example = "2024032901")
    @Length(max = 100)
    private String reportNo;

    /**
     * 放行单编号
     */
    @ApiModelProperty(value = "放行单编号", example = "2024032901")
    @Length(max = 100)
    private String licenceNo;
}
