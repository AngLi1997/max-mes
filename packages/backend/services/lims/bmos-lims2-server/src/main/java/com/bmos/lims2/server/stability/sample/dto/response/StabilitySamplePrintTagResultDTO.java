package com.bmos.lims2.server.stability.sample.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性整体样品打印标签结果DTO
 */
@Data
@ApiModel("稳定性整体样品打印标签结果数据对象")
public class StabilitySamplePrintTagResultDTO {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品信息（编码-名称）")
    private String fullMaterialName;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("实际取样量")
    private String actualSampleAmount;

    @ApiModelProperty("取样单位")
    private String sampleUnit;

    @ApiModelProperty("样品数量（数量+单位）")
    private String quantityWithUnit;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("取样人")
    private String samplerName;

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("试验储存条件")
    private String storageCondition;

    @ApiModelProperty("储存位置")
    private String storageLocation;

    @ApiModelProperty("生产日期")
    private LocalDate productionDate;

    @ApiModelProperty("样品接收日期")
    private LocalDateTime receiveDate;

    @ApiModelProperty("稳定性考察编号")
    private String planCode;
}
