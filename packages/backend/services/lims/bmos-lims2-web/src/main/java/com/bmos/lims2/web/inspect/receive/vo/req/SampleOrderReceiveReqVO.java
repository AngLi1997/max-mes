package com.bmos.lims2.web.inspect.receive.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 检验单样品接收请求VO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("检验单样品接收请求")
public class SampleOrderReceiveReqVO {

    @ApiModelProperty(value = "检验单ID", required = true)
    @NotNull(message = "检验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty("接收人")
    private String receiverName;
}
