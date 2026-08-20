package com.bmos.lims2.server.inspect.entry.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: APP-ELN 任务完成请求
 * @Author: yigaohui
 * @Date: 2025/11/19 16:30
 */
@Getter
@Setter
@ApiModel("APP-ELN任务完成请求")
public class AppTaskCompleteReqVO {

    @ApiModelProperty(value = "任务ID", required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
}


