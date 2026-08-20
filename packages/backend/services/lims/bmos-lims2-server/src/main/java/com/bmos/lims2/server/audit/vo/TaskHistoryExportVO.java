package com.bmos.lims2.server.audit.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@ApiModel(value = "查询任务历史导出vo")
public class TaskHistoryExportVO {

    @ExcelProperty("流程节点名称")
    @ApiModelProperty("节点名称")
    private String elementName;

    @ApiModelProperty("处理人名称")
    @ExcelProperty("处理人")
    private String completeName;

    @ExcelProperty("处理状态")
    @ApiModelProperty("处理状态")
    private String stateName;

    @ExcelProperty("处理时间")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ExcelProperty("处理意见")
    @ApiModelProperty("审批意见")
    private String comment;

    @ExcelProperty("备注")
    @ApiModelProperty("备注")
    private String remark;
}
