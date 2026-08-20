package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 录入统计DTO
 *
 * @author system
 * @since 2025/08/26
 */
@Getter
@Setter
@ApiModel("录入统计信息")
public class EntryStatsDTO {

    @ApiModelProperty("未完成数量")
    private Long incompleteCount;

    @ApiModelProperty("判定异常数量")
    private Long abnormalCount;

    @ApiModelProperty("全部数量")
    private Long allCount;
}


