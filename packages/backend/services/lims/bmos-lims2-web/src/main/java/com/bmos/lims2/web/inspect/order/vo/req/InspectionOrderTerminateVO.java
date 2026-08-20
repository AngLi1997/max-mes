package com.bmos.lims2.web.inspect.order.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 检验单终止请求VO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("检验单终止请求")
public class InspectionOrderTerminateVO {

    @ApiModelProperty(value = "检验单ID", required = true)
    @NotNull(message = "检验单ID不能为空")
    private Long id;

    @ApiModelProperty(value = "终止原因", required = true)
    @NotBlank(message = "终止原因不能为空")
    private String terminateReason;
}