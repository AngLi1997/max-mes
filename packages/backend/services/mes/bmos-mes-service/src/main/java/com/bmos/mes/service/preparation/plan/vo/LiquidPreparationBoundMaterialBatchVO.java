package com.bmos.mes.service.preparation.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("配液计划已添加批次VO")
@Data
public class LiquidPreparationBoundMaterialBatchVO {

    @ApiModelProperty("物料批次")
    private String materialBatchId;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("有效日期")
    private String expiredDate;

    @ApiModelProperty("水分")
    private BigDecimal hydration;

    @ApiModelProperty("含量")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("物料批次号")
    private String materialBatchNo;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("原厂批号")
    private String originalBatchNo;

    @ApiModelProperty("原厂编码")
    private String originalCode;

    @ApiModelProperty("报告单编号")
    private String reportNo;

    @ApiModelProperty("放行单编号")
    private String licenceNo;

    @ApiModelProperty("配液量")
    private BigDecimal preparationQuantity;

}
