package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/15 10:39
 */
@Data
@ApiModel("物料标签vo")
public class StorageMaterialTag extends BaseStorageMaterialTag {

    /**
     * 皮重(带单位)
     */
    @ApiModelProperty(value = "皮重(带单位)", example = "15.780kg")
    private String tareWeightWithUnit;

    /**
     * 毛重(带单位)
     */
    @ApiModelProperty(value = "毛重(带单位)", example = "15.780kg")
    private String grossWeightWithUnit;

    /**
     * 净重(带单位)
     */
    @ApiModelProperty(value = "净重(带单位)", example = "15.780kg")
    private String netWeightWithUnit;

    /**
     * 称重人 物料件称量的称量人员
     */
    @ApiModelProperty(value = "称量人", example = "张三")
    private String weigherName;

    /**
     * 复核人 物料件称量的复核人员
     */
    @ApiModelProperty(value = "复核人", example = "李四")
    private String reCheckerName;

    /**
     * 称重时间 物料件的称量时间
     */
    @ApiModelProperty(value = "称重时间", example = "2024-02-02 14:36:42")
    private String weighTime;

    @ApiModelProperty("配液量取量取人")
    private String measurerName;

    @ApiModelProperty("量取时间")
    private String measureTime;

    @ApiModelProperty("产品名称[非预定，仅备注]")
    private String extProductName;

    @ApiModelProperty("产品编码[非预定，仅备注]")
    private String extProductMergeCode;

    @ApiModelProperty("产品全称[非预定，仅备注]")
    private String extProductFullName;

    @ApiModelProperty("生产批号[非预定，仅备注]")
    private String extBatchNo;

    // 以下字段来源于工单称量

    @ApiModelProperty("工单称量需求产品名称")
    private String requirementProductName;

    @ApiModelProperty("工单称量需求产品编码")
    private String requirementProductMergeCode;

    @ApiModelProperty("工单称量需求产品全称")
    private String requirementProductFullName;

    @ApiModelProperty("工单称量需求产品批号")
    private String requirementBatchNo;

    @ApiModelProperty("工单称量需求BOM")
    private String requirementBom;

    @ApiModelProperty("工单称量需求用途")
    private String requirementUsage;
}
