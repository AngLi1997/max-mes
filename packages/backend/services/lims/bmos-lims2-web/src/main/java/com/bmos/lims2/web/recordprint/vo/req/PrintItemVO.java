package com.bmos.lims2.web.recordprint.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 打印项VO
 * @Author: yigaohui
 * @Date: 2025/11/25 10:40
 */
@Getter
@Setter
@ApiModel("打印项VO")
public class PrintItemVO {

    @ApiModelProperty(value = "任务ID", required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
}


