package com.bmos.lims2.server.inspect.entry.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: APP-分析项查询VO（支持按分析项ID查询）
 * @Author: yigaohui
 * @Date: 2025/11/17 00:45
 */
@Getter
@Setter
@ApiModel("APP-分析项查询VO")
public class AppAnalysisItemEntryQueryVO extends AnalysisItemEntryQueryVO {

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项ID集合")
    private List<Long> parameterIds;
}


