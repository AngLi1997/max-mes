package com.bmos.lims2.server.inspect.entry.vo;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: APP-ELN 任务状态响应
 * @Author: yigaohui
 * @Date: 2026/02/06 10:30
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("APP-ELN任务状态响应")
public class AppTaskStatusRespVO {

    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;

    @ApiModelProperty(value = "任务状态", required = true)
    private TaskStatusEnum status;
}
