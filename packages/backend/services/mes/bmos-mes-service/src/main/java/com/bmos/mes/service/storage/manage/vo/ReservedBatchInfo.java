package com.bmos.mes.service.storage.manage.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel("物料预定批次信息")
public class ReservedBatchInfo {

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("水分")
    private BigDecimal hydration;

    @ApiModelProperty("含量")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("物料量")
    private BigDecimal materialQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("有效日期")
    private LocalDate expiredDate;

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
