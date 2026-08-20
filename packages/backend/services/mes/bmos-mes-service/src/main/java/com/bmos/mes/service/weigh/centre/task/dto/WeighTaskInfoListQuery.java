package com.bmos.mes.service.weigh.centre.task.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 称量任务详情需求分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 17:40
 */
@Data
@ApiModel("称量任务详情需求列表查询参数")
public class WeighTaskInfoListQuery {

    @ApiModelProperty(value = "称量任务id", example = "1", required = true)
    @NotNull
    private Long taskId;
}
