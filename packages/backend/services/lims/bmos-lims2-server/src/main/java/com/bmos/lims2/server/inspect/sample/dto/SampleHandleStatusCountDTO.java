package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 样品处理状态数量统计
 * @Author: yigaohui
 * @Date: 2025/09/10 10:30
 */
@Getter
@Setter
@ApiModel("样品处理状态数量统计")
public class SampleHandleStatusCountDTO {

    @ApiModelProperty("待回收数量")
    private Long toRecycleCount;

    @ApiModelProperty("待处理数量")
    private Long toProcessCount;

    @ApiModelProperty("已处理数量")
    private Long processedCount;
}


