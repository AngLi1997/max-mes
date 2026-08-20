package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("投料回收物料件VO")
@Data
public class ChargeRecycleMaterialVO {
    @ApiModelProperty
    private Long storageMaterialId;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料批次id")
    private Long storageMaterialBatchId;

    @ApiModelProperty("物料件号")
    private String storageMaterialNo;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位")
    private String unitName;

    @ApiModelProperty("预定量/物料量")
    private BigDecimal quantity;



}
