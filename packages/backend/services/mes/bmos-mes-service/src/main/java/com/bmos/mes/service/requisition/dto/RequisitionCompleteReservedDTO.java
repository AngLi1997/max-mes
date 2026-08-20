package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel("仓库领料:完成领料(物料批次DTO)")
public class RequisitionCompleteReservedDTO {

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批次id/物料id")
    private Long id;

    @ApiModelProperty("批次号")
    private String materialBatchNo;

    @ApiModelProperty("计划量")
    @NotNull
    private BigDecimal plannedQuantity;

    @ApiModelProperty("理论量")
    @NotNull
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty("物料编码")
    private String mergeCode;

    @ApiModelProperty("过期时间")
    private LocalDate expiredDate;

    @ApiModelProperty("物料规格")
    private String specification;

    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;

    @ApiModelProperty(value = "生产商", example = "生产商")
    private String producer;

    @ApiModelProperty("水分")
    private BigDecimal hydration;

    @ApiModelProperty("无水含量")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("原厂批号")
    private String originBatchNo;

    @ApiModelProperty("原厂编码")
    private String originCode;

    @ApiModelProperty("单位id")
    private Long unitId;


}
