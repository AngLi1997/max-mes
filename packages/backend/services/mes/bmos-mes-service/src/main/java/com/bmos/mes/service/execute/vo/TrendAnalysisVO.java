package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ApiModel("趋势分析VO")
public class TrendAnalysisVO {

    /**
     * 最大值
     */
    @ApiModelProperty("最大值")
    private BigDecimal max;

    /**
     * 最小值
     */
    @ApiModelProperty("最小值")
    private BigDecimal min;

    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private List<ComponentTrendAnalysisVO> dataList;
}
