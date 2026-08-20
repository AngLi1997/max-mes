package com.bmos.mes.service.preparation.produce.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 配液产出物料批次信息
 */
@Getter
@Setter
@ApiModel("配液产出物料批次信息")
public class PreparationProduceMaterialBatchVO {

    /**
     * 配方物料id
     */
    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    /**
     * 物料批次id
     */
    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    /**
     * 物料批次编号
     */
    @ApiModelProperty("物料批次编号")
    private String materialBatchNo;

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    /**
     * 物料规格
     */
    @ApiModelProperty("物料规格")
    private String materialSpecification;

    /**
     * 有效期
     */
    @ApiModelProperty("有效期")
    private LocalDate expireDate;

    /**
     * 配方物料单位id
     */
    @ApiModelProperty("配方物料单位id")
    private Long unitId;

    /**
     * 配方物料单位名称
     */
    @ApiModelProperty("配方物料单位名称")
    private String unit;

}
