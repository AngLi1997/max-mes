package com.bmos.lims2.web.inspect.query.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 检验单状态标志位
 * @Author: yigaohui
 * @Date: 2025/09/11 10:50
 */
@Getter
@Setter
@ApiModel("检验单状态标志位")
public class OrderStatusFlagsVO {

    @ApiModelProperty("是否已请验")
    private boolean requested;

    @ApiModelProperty("是否已取样（样品全部接收）")
    private boolean sampled;

    @ApiModelProperty("是否已检验（样品审批完成）")
    private boolean inspected;

    @ApiModelProperty("是否已出报告（存在审批通过报告）")
    private boolean reported;
}


