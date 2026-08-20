package com.bmos.lims2.server.inspect.retention.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样销毁台账导出VO
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@Data
@ApiModel("留样销毁台账导出VO")
public class RetentionDestructionLedgerExportVO {

    @ExcelProperty("样品编号")
    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ExcelProperty("批号")
    @ApiModelProperty("批号")
    private String batchNo;

    @ExcelProperty("检品名称")
    @ApiModelProperty("检品名称")
    private String materialName;

    @ExcelProperty("检品编码")
    @ApiModelProperty("检品编码")
    private String materialCode;

    @ExcelProperty("规格")
    @ApiModelProperty("规格")
    private String materialSpec;

    @ExcelProperty("销毁数量")
    @ApiModelProperty("销毁数量")
    private String quantity;

    @ExcelProperty("单位")
    @ApiModelProperty("单位名称")
    private String unitName;

    @ExcelProperty("销毁原因")
    @ApiModelProperty("销毁原因")
    private String destructionReason;

    @ExcelProperty("销毁方式")
    @ApiModelProperty("销毁方式")
    private String destructionMethod;

    @ExcelProperty("销毁地点")
    @ApiModelProperty("销毁地点")
    private String destructionLocation;

    @ExcelProperty("销毁时间")
    @ApiModelProperty("销毁时间")
    private LocalDateTime destructionTime;

    @ExcelProperty("备注")
    @ApiModelProperty("备注")
    private String remark;

    @ExcelProperty("销毁人")
    @ApiModelProperty("销毁人名称")
    private String destructorName;

    @ExcelProperty("监督人")
    @ApiModelProperty("监督人名称")
    private String supervisorName;
}
