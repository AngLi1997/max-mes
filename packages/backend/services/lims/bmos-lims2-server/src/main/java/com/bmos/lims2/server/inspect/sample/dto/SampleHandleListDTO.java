package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 样品处理列表DTO
 * @Author: yigaohui
 * @Date: 2025/02/01 10:35
 */
@Getter
@Setter
@ApiModel("样品处理列表")
public class SampleHandleListDTO {

    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检验结束时间")
    private LocalDateTime orderEndTime;

    @ApiModelProperty("计划取样量")
    private String planQuantity;

    @ApiModelProperty("样品余量")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("状态：1-待回收，2-待处理，3-已处理")
    private Integer status;

    @ApiModelProperty("回收余量")
    private String recycleQuantity;

    @ApiModelProperty("回收单位ID")
    private Long recycleUnitId;

    @ApiModelProperty("回收单位名称")
    private String recycleUnitName;

    @ApiModelProperty("回收时间")
    private LocalDateTime recycledTime;

    @ApiModelProperty("处理方式")
    private String processMethod;

    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;
}


