package com.bmos.mes.service.preparation.measure.vo;

import com.bmos.mes.common.enums.preparation.MeasureStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("配液单详情批次信息")
@Data
public class LiquidPreparationDetailBatchVO {

    @ApiModelProperty("计划配液批次id")
    private Long id;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("物料批次编码")
    private String materialBatchNo;

    @ApiModelProperty("配液量")
    private BigDecimal preparationQuantity;

    @ApiModelProperty("已量取")
    private BigDecimal measuredQuantity;

    @ApiModelProperty("未量取")
    private BigDecimal unmeasuredQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("称量状态")
    private MeasureStatusEnum measureStatus;


}
