package com.bmos.lims2.web.inspect.sampling.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 样品信息响应VO
 *
 * @author yigaohui
 * @since 2025/01/29 16:00
 */
@Data
@ApiModel("样品信息响应")
public class SampleRespVO {

    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("父样品编号")
    private String parentSampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("计划取样量")
    private String planQuantity;

    @ApiModelProperty("实际取样量")
    private String quantity;

    @ApiModelProperty("取样单位")
    private String unitId;

    @ApiModelProperty("取样单位名称")
    private String unitName;

    @ApiModelProperty("是否已取样")
    private Boolean sampled;

    @ApiModelProperty("是否已分样")
    private Boolean divided;

    @ApiModelProperty("是否已接收")
    private Boolean received;

    @ApiModelProperty("是否已分样")
    private Boolean subSampled;

    @ApiModelProperty("是否已领取")
    private Boolean collected;

    @ApiModelProperty("是否作废")
    private Boolean discarded;

    @ApiModelProperty("父样品ID")
    private Long parentSampleId;

    @ApiModelProperty("取样人")
    private String samplerName;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人")
    private String receiverName;

    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("分样人")
    private String dividerName;

    @ApiModelProperty("分样时间")
    private LocalDateTime divideTime;

    @ApiModelProperty("领取人")
    private String collectorName;

    @ApiModelProperty("领取时间")
    private LocalDateTime collectTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;
}
