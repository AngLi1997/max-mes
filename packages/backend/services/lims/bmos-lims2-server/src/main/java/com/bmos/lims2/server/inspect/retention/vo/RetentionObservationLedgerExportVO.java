package com.bmos.lims2.server.inspect.retention.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样观察台账导出VO
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@Data
@ApiModel("留样观察台账导出VO")
public class RetentionObservationLedgerExportVO {

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

    @ExcelProperty("观察结果")
    @ApiModelProperty("观察结果")
    private String observationResult;

    @ExcelProperty("备注")
    @ApiModelProperty("备注")
    private String observationRemark;

    @ExcelProperty("观察人")
    @ApiModelProperty("观察人名称")
    private String observerName;

    @ExcelProperty("观察时间")
    @ApiModelProperty("观察时间")
    private LocalDateTime observationTime;
}
