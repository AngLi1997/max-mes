package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * wms通知mes发料
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/16 16:21
 */
@Data
@ApiModel("wms通知mes发料")
public class SendOutFeignDTO {

    /**
     * 领料计划id
     */
    @NotNull
    @ApiModelProperty(value = "领料计划id", example = "1")
    private Long requisitionPlanId;

    /**
     * 发料批次列表
     */
    @NotEmpty
    @Valid
    private List<SendOutBatch> sendOutBatchList;

    /**
     * 发料批次
     */
    @Data
    public static final class SendOutBatch {

        /**
         * 批次id
         */
        private Long inventoryBatchId;

        /**
         * 货品批次号
         */
        @NotBlank
        @Length(max = 100)
        @ApiModelProperty(value = "货品批次号", example = "123456")
        private String inventoryBatchNo;

        /**
         * 该批次总发放物料量(标准单位量)
         */
        @DecimalMin("0.000000001")
        @DecimalMax("9999999999.999999999")
        @ApiModelProperty(value = "该批次总发放物料量(标准单位量)", example = "1")
        private BigDecimal quantity;

        /**
         * 单位id
         */
        @NotNull
        @ApiModelProperty(value = "单位id", example = "1")
        private Long unitId;

        /**
         * 原厂批号
         */
        @ApiModelProperty(value = "原厂批号", example = "123456")
        private String factoryBatchNo;

        /**
         * 生产日期
         */
        @ApiModelProperty(value = "生产日期", example = "2023-04-01")
        private LocalDate produceDate;

        /**
         * 有效日期
         */
        @ApiModelProperty(value = "有效日期", example = "2023-04-01")
        @NotNull
        private LocalDate expiredDate;

        /**
         * 水分(%)
         */
        @ApiModelProperty(value = "水分(%)", example = "0")
        private BigDecimal hydration;

        /**
         * 无水含量(%)
         */
        @ApiModelProperty(value = "无水含量(%)", example = "100")
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
         * 货品合并编码
         */
        @ApiModelProperty(value = "货品合并编码", example = "123456")
        private String cargoMergeCode;

        @ApiModelProperty("货品名称")
        private String cargoName;

        /**
         * 批次下货品件
         */
        @NotEmpty
        @Valid
        private List<SendOutInventory> inventories;
    }

    /**
     * 发料货品
     */
    @Data
    public static final class SendOutInventory {

        /**
         * 物料平台id
         */
        @NotNull
        @ApiModelProperty(value = "物料平台id", example = "1")
        private Long platformMaterialId;

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
