package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApiModel("仓库物料入库DTO")
@Data
public class InventoryMaterialInboundDTO {
    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1", required = true)
    @NotNull
    private Long materialId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001", required = true)
    @NotBlank
    @Length(max = 100)
    private String materialBatchNo;

    /**
     * 原始批号
     */
    @ApiModelProperty(value = "原始批号", example = "123")
    @Length(max = 100)
    private String originalBatchNo;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-02-06", required = true)
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;

    /**
     * 暂存货位id
     */
    @ApiModelProperty(value = "暂存货位id", example = "1", required = true)
    @NotNull
    private Long materialPositionId;

    /**
     * 来源/去向
     */
    @ApiModelProperty(value = "来源/去向", example = "123", required = true)
    @NotBlank
    @Length(max = 200)
    private String linkExplain;

    /**
     * 递交人id
     */
    @ApiModelProperty(value = "递交人id", example = "1", required = true)
    @NotBlank
    private String senderId;

    @ApiModelProperty("单位id")
    private Long unitId;

    /**
     *
     */
    @ApiModelProperty("水分(%)")
    private BigDecimal hydration;

    @ApiModelProperty("无水含量(%)")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

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
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id", example = "1", required = true)
    @NotBlank
    private String receiverId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品批号
     */
    private String productBatchNo;

    @ApiModelProperty("入库物料件")
    @NotEmpty
    private List<MaterialInboundDTO> inboundList;

    @Data
    @ApiModel("物料件")
    public static class MaterialInboundDTO {

        /**
         * 物料件号
         */
        @NotBlank
        @Length(max = 100)
        @ApiModelProperty(value = "物料件号", example = "123456")
        private String inventoryNo;

        /**
         * 发放物料量(标准单位量)
         */
        @DecimalMin("0.000000001")
        @DecimalMax("9999999999.999999999")
        @ApiModelProperty(value = "发放物料量(标准单位量)", example = "1")
        private BigDecimal quantity;

        /**
         * 单位id
         */
        @NotNull
        @ApiModelProperty(value = "单位id", example = "1")
        private Long unitId;

    }

}
