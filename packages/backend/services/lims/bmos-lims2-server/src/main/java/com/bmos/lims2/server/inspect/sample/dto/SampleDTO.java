package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 样品DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品数据对象")
public class SampleDTO {

    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("是否已取样")
    private Boolean sampled;

    @ApiModelProperty("是否已接收")
    private Boolean received;

    @ApiModelProperty("是否已分样")
    private Boolean divided;

    @ApiModelProperty("是否已领取")
    private Boolean collected;

    @ApiModelProperty("是否作废")
    private Boolean discarded;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("父样品ID")
    private Long parentSampleId;

    @ApiModelProperty("父样品编号")
    private String parentSampleNo;

    @ApiModelProperty("计划取样量")
    private String planQuantity;

    @ApiModelProperty("实际取样量")
    private String quantity;

    @ApiModelProperty("取样单位")
    private Long unitId;

    @ApiModelProperty("取样单位名称")
    private String unitName;

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

    @ApiModelProperty("领取人ID")
    private String collectorId;

    @ApiModelProperty("领取人")
    private String collectorName;

    @ApiModelProperty("领取时间")
    private LocalDateTime collectTime;

    @ApiModelProperty("作废人")
    private String discardedBy;

    @ApiModelProperty("作废时间")
    private LocalDateTime discardedTime;

    @ApiModelProperty("作废原因")
    private String discardedReason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    // ================== 以下字段用于样品回收/处理等状态与信息 ==================

    @ApiModelProperty("是否已回收")
    private Boolean recycled;

    @ApiModelProperty("回收时间")
    private LocalDateTime recycledTime;

    @ApiModelProperty("回收人")
    private String recycledBy;

    @ApiModelProperty("回收余量")
    private String recycleQuantity;

    @ApiModelProperty("回收余量单位ID")
    private Long recycleUnitId;

    @ApiModelProperty("回收备注")
    private String recycleRemark;

    @ApiModelProperty("是否已处理")
    private Boolean processed;

    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty("处理人")
    private String processBy;

    @ApiModelProperty("处理方式")
    private String processMethod;

    @ApiModelProperty("处理备注")
    private String processRemark;

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;
}