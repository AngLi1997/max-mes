package com.bmos.mes.service.audit.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.common.convert.ExcelEnumConvert;
import com.bmos.mes.common.enums.audit.FlowStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@ApiModel(value = "流程追溯导出vo")
public class AuditHistoryExportVO {

    @ExcelProperty("流程名称")
    @ApiModelProperty(value = "流程名称")
    private String flowName;

    @ExcelProperty("实例业务名称")
    @ApiModelProperty(value = "业务名称")
    private String name;

    @ExcelProperty("实例业务编号")
    @ApiModelProperty("业务code")
    private String extField;

    @ExcelProperty("流程发起时间")
    @ApiModelProperty("发起时间")
    private LocalDateTime processStartTime;

    @ExcelProperty("发起人")
    @ApiModelProperty("发起人名称")
    private String startByName;

    @ExcelProperty("流程结束时间")
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ExcelProperty(value = "结束状态", converter = ExcelEnumConvert.class)
    @ApiModelProperty("结束状态")
    private FlowStateEnum processState;


}
