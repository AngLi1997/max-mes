package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 报告操作时间线项（返回给前端，字段与原型一致）
 * @Author: yigaohui
 * @Date: 2025/10/20 16:25
 */
@Getter
@Setter
@ApiModel("报告操作时间线项")
public class ReportOperationTimelineItemDTO {
    @ApiModelProperty("时间，示例：2022-12-11 16:25:52")
    private LocalDateTime time;

    @ApiModelProperty("动作中文，例如：生成报告/重新生成/提交审批/审核通过/审核不通过/下载/作废")
    private String action;

    @ApiModelProperty("操作人中文名，例如：张三-zhangsan")
    private String operator;

    @ApiModelProperty("下载链接文字，固定为'下载报告'，无下载可为空")
    private String downloadText;

    @ApiModelProperty("下载路径，相对路径；无下载可为空")
    private String downloadPath;

    @ApiModelProperty("审核意见或备注，无则为空")
    private String remark;
}


