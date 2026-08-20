package com.bmos.mes.service.plan.document.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 生产计划简单信息
 */
@Getter
@Setter
@ApiModel("生产计划简单信息")
public class PlanEasyVO {

    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long id;

    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;

}
