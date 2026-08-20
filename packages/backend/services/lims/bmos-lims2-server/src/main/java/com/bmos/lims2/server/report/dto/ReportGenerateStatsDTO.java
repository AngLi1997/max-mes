package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 模板版本生成统计
 * @Author: yigaohui
 * @Date: 2025/09/08 10:30
 */
@Getter
@Setter
@ApiModel("模板版本生成统计")
public class ReportGenerateStatsDTO {

    @ApiModelProperty("模板版本ID")
    private Long templateVersionId;

    @ApiModelProperty("生成成功次数")
    private Long generatedCount;

    @ApiModelProperty("最近一次生成完成时间")
    private LocalDateTime lastGeneratedTime;
}


