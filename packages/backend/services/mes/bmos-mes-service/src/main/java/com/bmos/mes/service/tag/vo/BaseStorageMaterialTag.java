package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseStorageMaterialTag {

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 物料编码 物料合并编码
     */
    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialMergeCode;

    /**
     * 物料全称 物料合并编码-物料名称
     */
    @ApiModelProperty(value = "物料全称", example = "WH03-氯化钠")
    private String fullName;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030101221001")
    private String materialBatchNo;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "001")
    private String materialNo;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "25kg/袋")
    private String materialSpecification;

    /**
     * 物料量(带单位)
     */
    @ApiModelProperty(value = "物料量(带单位)", example = "15.780kg")
    private String quantityWithUnit;

    /**
     * 产品名称 生产批次的产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "氯化钠溶液")
    private String productName;

    /**
     * 产品编码 生产批次的产品合并编码
     */
    @ApiModelProperty(value = "产品编码", example = "C01")
    private String productMergeCode;

    /**
     * 产品规格 生产批次的产品规格
     */
    @ApiModelProperty(value = "产品规格", example = "0.9%")
    private String productSpecification;

    /**
     * 工艺名称 生产批次的工艺名称
     */
    @ApiModelProperty("工艺名称")
    private String processName;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "C01230101")
    private String batchNo;

    /**
     * 有效日期 物料件所属批次的有效日期，展示yyyy-MM-dd
     */
    @ApiModelProperty(value = "有效日期", example = "2025-10-31")
    private String expiredDate;

    /**
     * 暂存货位 暂存货位信息，展示为“货位编码-货位名称”
     */
    @ApiModelProperty(value = "暂存货位名称", example = "KQ10-01-氯化钠货位")
    private String positionFullName;

}
