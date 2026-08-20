package com.bmos.lims2.server.inspect.query.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @Description: 检验单样品简要信息（用于详情页）
 * @Author: yigaohui
 * @Date: 2025/09/11 15:10
 */
@Getter
@Setter
@ApiModel("检验单样品简要信息")
public class OrderSampleDTO {

    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("父样品编号")
    private String parentSampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("取样单位ID")
    private Long unitId;

    @ApiModelProperty("取样单位名称")
    private String unitName;

    @ApiModelProperty("实际取样量")
    private String quantity;

    @ApiModelProperty("回收余量")
    private String recycleQuantity;

    @ApiModelProperty("消耗量 = 实际取样量 - 回收余量（小于0按0处理）")
    private String consumption;

    @ApiModelProperty("状态：0未取样，1已取样，2已接收，3已分样，4已领取，5已回收，6已处理")
    private Integer status;

    @ApiModelProperty("当前状态的操作人")
    private String operatorName;

    @ApiModelProperty("当前状态的操作时间")
    private LocalDateTime operateTime;

    @ApiModelProperty("检验单号")
    private String inspectionOrderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;
}


