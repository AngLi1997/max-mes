package com.bmos.lims2.server.inspect.retention.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样领用台账导出VO
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@Data
@ApiModel("留样领用台账导出VO")
public class SampleCollectionLedgerExportVO {

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

    @ExcelProperty("领用数量")
    @ApiModelProperty("领用数量")
    private String collectQuantity;

    @ExcelProperty("单位")
    @ApiModelProperty("单位名称")
    private String unitName;

    @ExcelProperty("领用原因")
    @ApiModelProperty("领用原因")
    private String collectReason;

    @ExcelProperty("领用人")
    @ApiModelProperty("领用人名称")
    private String collectorName;

    @ExcelProperty("领用时间")
    @ApiModelProperty("领用时间")
    private LocalDateTime collectTime;
}
