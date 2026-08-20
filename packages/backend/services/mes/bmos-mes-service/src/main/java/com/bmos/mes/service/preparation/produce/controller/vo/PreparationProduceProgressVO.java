package com.bmos.mes.service.preparation.produce.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配液产出流程VO
 */
@Data
@ApiModel("配液产出流程VO")
public class PreparationProduceProgressVO {

    /**
     * 流程id
     */
    @ApiModelProperty("流程id")
    private Long progressId;

    /**
     * 产出人id
     */
    @ApiModelProperty("产出人id")
    private String reCheckerId;

    /**
     * 产出复核人名
     */
    @ApiModelProperty("产出复核人名")
    private String reCheckerName;

    /**
     * 产出复核人登录名
     */
    @ApiModelProperty("产出复核人登录名")
    private String reCheckerLoginName;

    /**
     * 产出人id
     */
    @ApiModelProperty("产出人id")
    private String producerId;

    /**
     * 产出人名
     */
    @ApiModelProperty("产出人名")
    private String producerName;

    /**
     * 产出人登录名
     */
    @ApiModelProperty("产出人登录名")
    private String producerLoginName;

    /**
     * 配液单相关信息
     */
    @ApiModelProperty("配液单相关信息")
    private PreparationProducePlanVO planVO;

    /**
     * 是否产出物料件
     */
    @ApiModelProperty("是否产出物料件")
    private Boolean produceStorageMaterialFlg;

    /**
     * 物料批次信息
     */
    @ApiModelProperty("物料批次信息")
    private PreparationProduceMaterialBatchVO batchVO;

}
