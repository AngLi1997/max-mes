package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次追溯-生产批次执行信息
 */
@ApiModel("批次追溯-生产批次的物料信息")
@Data
public class PlanRetraceMaterialPageVO {

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty("物料编码")
    private String materialCode;

    /**
     * 物料规格
     */
    @ApiModelProperty("物料规格")
    private String materialSpecification;

    /**
     * 物料件号
     */
    @ApiModelProperty("物料件号")
    private String storageMaterialNo;

    /**
     * 物料批号
     */
    @ApiModelProperty("物料批号")
    private String storageMaterialBatchNo;

    /**
     * 物料量
     */
    @ApiModelProperty("物料量")
    private String materialQuantity;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位")
    private String unitName;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private String operationTime;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operationType;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人名称")
    private String operateUserName;
}
