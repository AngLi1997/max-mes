package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel("已接收物料批次信息")
@Data
public class RequisitionReceivedBatchInfo {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("物料批次号")
    private String inventoryBatchNo;

    @ApiModelProperty("领料量")
    private BigDecimal receivedQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("原厂批号")
    private String factoryBatchNo;

    @ApiModelProperty("原始编码/仓库货品编码")
    private String cargoMergeCode;

    @ApiModelProperty("有效日期")
    private LocalDate expiredDate;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("该批次已领物料量")
    private BigDecimal quantity;

}
