package com.bmos.mes.service.ingredient.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel("物料批次绑定DTO")
@Data
public class BindMaterialBatchDTO {


    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("水分")
    private BigDecimal hydration;

    @ApiModelProperty("含量")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("有效日期")
    private LocalDate expiredDate;

    @ApiModelProperty("配料量")
    private BigDecimal ingredientQuantity;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("原产批号")
    private String originalBatchNo;

    @ApiModelProperty("原始编码")
    private String originalCode;

    @ApiModelProperty("报告单编号")
    private String reportNo;

    @ApiModelProperty("放行单编号")
    private String licenceNo;


}
