package com.bmos.lims2.server.inspect.retention.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样接收台账导出VO
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@Data
@ApiModel("留样接收台账导出VO")
public class RetentionReceiveLedgerExportVO {

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

    @ExcelProperty("样品数量")
    @ApiModelProperty("样品数量")
    private String quantity;

    @ExcelProperty("单位")
    @ApiModelProperty("单位名称")
    private String unitName;

    @ExcelProperty("取样人")
    @ApiModelProperty("取样人名称")
    private String samplerName;

    @ExcelProperty("取样时间")
    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ExcelProperty("接收人")
    @ApiModelProperty("接收人名称")
    private String receiverName;

    @ExcelProperty("接收时间")
    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    @ExcelProperty("储存位置")
    @ApiModelProperty("储存位置")
    private String storageLocation;
}
