package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("领料单物料批次VO")
@Data
public class RequisitionMaterialBatchVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("出库量")
    private BigDecimal outboundQuantity;

    @ApiModelProperty("暂存货位名称")
    private String cargoPositionName;

    @ApiModelProperty("货位id")
    private Long cargoPositionId;

    @ApiModelProperty("该批次是否领完")
    private Boolean receiveCompleted;



}
