package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 数值组件趋势分析VO
 */
@Getter
@Setter
@ApiModel("数值组件趋势分析VO")
@Accessors(chain = true)
public class ComponentTrendAnalysisVO {

    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;

    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private String planId;

    /**
     * 同一生产计划下的的序号
     */
    @ApiModelProperty("同生产计划下的的序号")
    private Integer serialNo;

    /**
     * 数值
     */
    @ApiModelProperty("数值")
    private BigDecimal value;


}
