package com.bmos.mes.service.weigh.centre.task.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * WeighTaskInfoPageQuery
 * @author liang
 * @version 1.0.0
 * @date 2024/7/9 17:15
 */
@Data
@ApiModel("称量任务编辑保存参数")
public class WeighTaskEditDTO {

    @ApiModelProperty(value = "称量任务id", example = "1", required = true)
    @NotNull
    private Long taskId;

    @ApiModelProperty(value = "执行时间", example = "2024-07-09", required = true)
    @NotNull
    private LocalDate executeDate;

    @ApiModelProperty(value = "新增的称量需求id列表")
    private List<Long> addIds;

    @ApiModelProperty(value = "移除的称量需求id列表")
    private List<Long> removeIds;
}
