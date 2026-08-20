package com.bmos.lims2.web.inspect.sample.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 样品处理状态数量统计响应
 * @Author: yigaohui
 * @Date: 2025/09/10 10:36
 */
@Getter
@Setter
@ApiModel("样品处理状态数量统计响应")
public class SampleHandleStatusCountRespVO {

    @ApiModelProperty("待回收数量")
    private Long toRecycleCount=0L;

    @ApiModelProperty("待处理数量")
    private Long toProcessCount=0L;

    @ApiModelProperty("已处理数量")
    private Long processedCount=0L;
}


