package com.bmos.lims2.web.stability.plan.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 取样页面页签数量响应VO
 */
@Data
@AllArgsConstructor
@ApiModel("取样页签数量")
public class SamplingTabCountRespVO {

    @ApiModelProperty("常规检验取样数量（已确认待取样的检验单数）")
    private long regularCount;

    @ApiModelProperty("稳定性考察取样数量（存在待取样样品的批次数）")
    private long stabilityCount;
}
